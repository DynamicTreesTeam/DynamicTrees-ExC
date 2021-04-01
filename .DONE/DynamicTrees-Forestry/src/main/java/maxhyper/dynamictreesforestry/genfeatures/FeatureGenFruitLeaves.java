package maxhyper.dynamictreesforestry.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesforestry.ModConfigs;
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
    protected int fruitingRadius = 8;
    protected int fruitAttempts = 10;

    public FeatureGenFruitLeaves(int size, int height, IBlockState leafType1, IBlockState leafType2, float proportionForWorldgen){
        boxSize = size;
        boxHeight = height;
        leaf1 = leafType1;
        leaf2 = leafType2;
        worldGenProportion = proportionForWorldgen;
    }

    public FeatureGenFruitLeaves setFruitingRadius(int fruitingRadius) {
        this.fruitingRadius = fruitingRadius;
        return this;
    }

    public FeatureGenFruitLeaves setfruitAttempts(int fruitAttempts) {
        this.fruitAttempts = fruitAttempts;
        return this;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if (ModConfigs.fruityLeaves && (TreeHelper.getRadius(world, rootPos.up()) >= fruitingRadius) && natural)
            changeRandLeaf(world, rootPos, fruitAttempts, species);
        return false;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        if (ModConfigs.fruityLeaves)
            changeLeaves(world, rootPos, worldGenProportion, species, safeBounds);
        return true;
    }

    private float getSeasonValue(World world, BlockPos pos){
        Float value = SeasonHelper.getSeasonValue(world, pos);
        if (value == null) return 1;
        return value;
    }

    private boolean isLeaves1Valid(World world, BlockPos pos){
        return world.getBlockState(pos).getBlock() == leaf1.getBlock() &&
                world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE).equals(leaf1.getValue(BlockDynamicLeaves.TREE)) &&
                world.getBlockState(pos).getValue(BlockDynamicLeaves.HYDRO) == 1;
    }

    private boolean isLeaves2Valid(World world, BlockPos pos){
        return world.getBlockState(pos).getBlock() == leaf2.getBlock();
    }

    private void attemptLeafChange(World world, BlockPos pos, boolean worldGen, Species species, SafeChunkBounds safeBounds){
        if (worldGen){
            if (safeBounds.inBounds(pos, true) && isLeaves1Valid(world, pos) && world.rand.nextFloat() <= species.seasonalFruitProductionFactor(world, pos)){
                int age = world.rand.nextInt(4);
                world.setBlockState(pos, leaf2.withProperty(BlockDynamicLeaves.TREE,  age));
            }
        }
        else {
            if (isLeaves1Valid(world, pos)){
                world.setBlockState(pos, leaf2.withProperty(BlockDynamicLeaves.TREE, 0));
            }
            if (isLeaves2Valid(world, pos)){
                int growthStage = world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE);
                if (growthStage == 3){
                    world.setBlockState(pos, leaf1);
                    return;
                }
                if (world.rand.nextFloat() <= species.seasonalFruitProductionFactor(world, pos)) {
                    switch (growthStage) {
                        case 0:
                            world.setBlockState(pos, leaf2.withProperty(BlockDynamicLeaves.TREE, 1));
                            break;
                        case 1:
                            world.setBlockState(pos, leaf2.withProperty(BlockDynamicLeaves.TREE, 2));
                            break;
                        case 2:
                            world.setBlockState(pos, leaf2.withProperty(BlockDynamicLeaves.TREE, 3));
                            break;
                    }
                }
            }
        }
    }

    private void changeRandLeaf(World world, BlockPos rootPos, int attempts, Species species){
        Random rand = new Random();
        do {
            int randX = rootPos.getX() + rand.nextInt((2*boxSize)+1) - boxSize;
            int randZ = rootPos.getZ() + rand.nextInt((2*boxSize)+1) - boxSize;
            int randY = rootPos.getY() + rand.nextInt(boxHeight + 1);
            attemptLeafChange(world, new BlockPos(randX,randY,randZ), false, species, null);
        } while ((attempts--)>0);
    }

    private void changeLeaves(World world, BlockPos rootPos, float proportion, Species species, SafeChunkBounds safeBounds){
        if (proportion <= 0 || proportion > 1) return;
        Random rand = new Random();
        for(int x=rootPos.getX()-boxSize; x<rootPos.getX()+2*boxSize; x++){
            for(int z=rootPos.getZ()-boxSize; z<rootPos.getZ()+2*boxSize; z++){
                for(int y=rootPos.getY(); y<rootPos.getY()+boxHeight; y++){
                    if (rand.nextFloat()*(1/proportion) <= 1){
                        attemptLeafChange(world, new BlockPos(x,y,z), true, species, safeBounds);
                    }
                }
            }
        }
    }

}
