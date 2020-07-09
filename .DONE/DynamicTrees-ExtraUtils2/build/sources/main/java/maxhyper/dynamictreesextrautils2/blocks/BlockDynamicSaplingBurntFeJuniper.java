package maxhyper.dynamictreesextrautils2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesextrautils2.DynamicTreesExtraUtils2;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDynamicSaplingBurntFeJuniper extends BlockDynamicSapling {

    public BlockDynamicSaplingBurntFeJuniper() {
        super(new ResourceLocation(DynamicTreesExtraUtils2.MODID,"ferrousjuniperburntsapling").toString());
        setTickRandomly(false);
    }

    @Override public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return false;
    }

    public static boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
        Block soilBlock = soilBlockState.getBlock();
        return soilBlock == Blocks.GRASS || soilBlock == Blocks.DIRT || soilBlock instanceof BlockRooty;
    }

    public static boolean canSaplingStay(World world, Species species, BlockPos pos) {
        //Ensure there are no adjacent branches or other saplings
        for(EnumFacing dir: EnumFacing.HORIZONTALS) {
            IBlockState blockState = world.getBlockState(pos.offset(dir));
            Block block = blockState.getBlock();
            if(TreeHelper.isBranch(block) || block instanceof BlockDynamicSapling) {
                return false;
            }
        }
        //Air above and acceptable soil below
        return world.isAirBlock(pos.up()) && isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()));
    }
}
