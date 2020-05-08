package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDynamicSaplingBloodwood extends BlockDynamicSapling {

    public BlockDynamicSaplingBloodwood() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"bloodwoodsapling").toString());
    }
    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return canSaplingStay(world, getSpecies(world, pos, state), pos);
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
        return world.isAirBlock(pos.down()) && species.isAcceptableSoil(world, pos.up(), world.getBlockState(pos.up()));
    }
}
