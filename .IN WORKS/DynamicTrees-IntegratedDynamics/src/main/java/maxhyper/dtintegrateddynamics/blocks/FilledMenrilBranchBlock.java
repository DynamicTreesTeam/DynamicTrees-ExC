package maxhyper.dtintegrateddynamics.blocks;

import com.ferreusveritas.dynamictrees.blocks.branches.ThickBranchBlock;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NetVolumeNode;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class FilledMenrilBranchBlock extends ThickBranchBlock {

    public FilledMenrilBranchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getLogDrops(World world, BlockPos pos, Species species, NetVolumeNode.Volume volume, ItemStack handStack) {
        return super.getLogDrops(world, pos, species, volume, handStack);
    }
}
