package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDynamicBranchFusewood extends BlockBranchBasic {

    public BlockDynamicBranchFusewood() {
        super(Material.WOOD, new ResourceLocation(DynamicTreesNatura.MODID,"fusewoodbranch").toString());
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    };

    @Override public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }
    @Override public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

    @Override public boolean removedByEntity(IBlockState state, World world, BlockPos cutPos, EntityLivingBase entity) {
        float radiusMultiplier = state.getValue(RADIUS)/8f;
        if (world.getDifficulty() == EnumDifficulty.HARD)
        {
            world.createExplosion(null, cutPos.getX(), cutPos.getY(), cutPos.getZ(), 2.0f *radiusMultiplier, false);
        }
        else if (world.getDifficulty() == EnumDifficulty.NORMAL || world.getDifficulty() == EnumDifficulty.EASY)
        {
            world.createExplosion(null, cutPos.getX(), cutPos.getY(), cutPos.getZ(), 1.75f *radiusMultiplier, false);
        }
//        else if (world.getDifficulty() == EnumDifficulty.PEACEFUL)
//        {
//            //Do nothing
//        }
        return super.removedByEntity(state,world,cutPos,entity);
    }
}