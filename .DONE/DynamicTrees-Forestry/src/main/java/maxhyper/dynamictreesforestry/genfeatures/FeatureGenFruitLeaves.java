package maxhyper.dynamictreesforestry.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class FeatureGenFruitLeaves implements IPostGenFeature, IPostGrowFeature {
    public int boxSize, boxHeight;
    public IBlockState leaf1, leaf2;
    public float worldGenProportion;

    public FeatureGenFruitLeaves(int size, int height, IBlockState leafType1, IBlockState leafType2, float proportionForWorldgen){
        boxSize = size;
        boxHeight = height;
        leaf1 = leafType1;
        leaf2 = leafType2;
        worldGenProportion = proportionForWorldgen;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        changeRandLeaf(world, rootPos, 1);
        return false;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        changeLeaves(world, rootPos, worldGenProportion);
        return true;
    }

    private void attemptLeafChange(World world, BlockPos pos, boolean worldGen){
        int growthStage = 0;
        if (!world.isBlockLoaded(pos)){
            return;
        }
        if (worldGen){
            growthStage = world.rand.nextInt(4);
        }
        if (world.getBlockState(pos).getBlock() == leaf1.getBlock() &&
                world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE).equals(leaf1.getValue(BlockDynamicLeaves.TREE)) &&
                world.getBlockState(pos).getValue(BlockDynamicLeaves.HYDRO) == 1){
            if (world.rand.nextFloat() <= SeasonHelper.globalSeasonalFruitProductionFactor(world, pos)){
                world.setBlockState(pos, leaf2.withProperty(BlockDynamicLeaves.TREE, growthStage));
            }
        } else if (world.getBlockState(pos).getBlock() == leaf2.getBlock()){
            world.setBlockState(pos, leaf1);
        }
    }

    private void changeRandLeaf(World world, BlockPos rootPos, int attempts){
        Random rand = new Random();
        do {
            int randX = rootPos.getX() + rand.nextInt((2*boxSize)+1) - boxSize;
            int randZ = rootPos.getZ() + rand.nextInt((2*boxSize)+1) - boxSize;
            int randY = rootPos.getY() + rand.nextInt(boxHeight + 1);
            attemptLeafChange(world, new BlockPos(randX,randY,randZ), false);
        } while ((attempts--)>0);
    }

    private void changeLeaves(World world, BlockPos rootPos, float proportion){
        if (proportion <= 0 || proportion > 1) return;
        Random rand = new Random();
        for(int x=rootPos.getX()-boxSize; x<rootPos.getX()+2*boxSize; x++){
            for(int z=rootPos.getZ()-boxSize; z<rootPos.getZ()+2*boxSize; z++){
                for(int y=rootPos.getY(); y<rootPos.getY()+boxHeight; y++){
                    if (rand.nextFloat()*(1/proportion) <= 1){
                        attemptLeafChange(world, new BlockPos(x,y,z), true);
                    }
                }
            }
        }
    }

}
