package maxhyper.dynamictreesatum.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;


public class FeatureGenFruitPod implements IPostGenFeature, IPostGrowFeature {

    BlockDate fruitPod;

    public FeatureGenFruitPod(BlockDate fruitPod){
        this.fruitPod = fruitPod;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if(world.rand.nextFloat() <= (1/16f)) {
            addDate(world, rootPos, getLeavesHeight(rootPos, world), false);
            return true;
        }
        return false;
    }

    private BlockPos getLeavesHeight (BlockPos rootPos, World world){
        for (int y= 1; y < 20; y++){
            BlockPos testPos = rootPos.up(y);
            if ((world.getBlockState(testPos).getBlock() instanceof BlockLeaves)){
                return testPos;
            }
        }
        return rootPos;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        boolean placed = false;
        for (int i=0;i<8;i++){
            if(world.rand.nextFloat() <= 0.25) {
                addDate(world, rootPos, getLeavesHeight(rootPos, world),true);
                placed = true;
            }
        }
        return placed;
    }

    private void addDate(World world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()){
            return;
        }
        EnumFacing placeDir = EnumFacing.HORIZONTALS[world.rand.nextInt(4)];
        if (world.isAirBlock(leavesPos.offset(placeDir))){
            world.setBlockState(leavesPos.offset(placeDir), fruitPod.getDefaultState().withProperty(BlockDate.AGE, worldGen?(world.rand.nextInt(7)):0)); //.withProperty(BlockCocoa.FACING, placeDir.getOpposite())
        }
    }

}