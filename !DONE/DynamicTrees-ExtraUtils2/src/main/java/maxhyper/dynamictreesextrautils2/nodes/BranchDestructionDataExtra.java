package maxhyper.dynamictreesextrautils2.nodes;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;
import java.util.Map;

public class BranchDestructionDataExtra extends BranchDestructionData {
    public final float secondaryWoodVolume;
    public BranchDestructionDataExtra() {
        super();
        secondaryWoodVolume = 0;
    }

    public BranchDestructionDataExtra(Species species, Map<BlockPos, IExtendedBlockState> branches, Map<BlockPos, IBlockState> leaves, List<BlockBranch.BlockItemStack> leavesDrops, List<BlockPos> ends, float volume, float secondaryVolume, BlockPos cutPos, EnumFacing cutDir, EnumFacing toolDir, int trunkHeight) {
        super(species, branches, leaves, leavesDrops, ends, volume, cutPos, cutDir, toolDir, trunkHeight);
        this.secondaryWoodVolume = secondaryVolume;

    }

    public BranchDestructionDataExtra(NBTTagCompound nbt) {
        super(nbt);
        this.secondaryWoodVolume = nbt.getFloat("volumesecondary");
    }

    @Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setFloat("volumesecondary", secondaryWoodVolume);
        return super.writeToNBT(tag);
    }
}
