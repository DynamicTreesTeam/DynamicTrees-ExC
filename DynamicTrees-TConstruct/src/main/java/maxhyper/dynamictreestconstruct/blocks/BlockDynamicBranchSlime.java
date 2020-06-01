package maxhyper.dynamictreestconstruct.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDynamicBranchSlime extends BlockBranchBasic {

    public BlockDynamicBranchSlime(String name) {
        super(Material.GROUND, new ResourceLocation(DynamicTreesTConstruct.MODID,name).toString());
        setSoundType(SoundType.SLIME);
    }

    @Override public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }
    @Override public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

}