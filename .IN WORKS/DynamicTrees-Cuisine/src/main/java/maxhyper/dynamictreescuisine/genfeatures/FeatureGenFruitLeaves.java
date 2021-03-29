package maxhyper.dynamictreescuisine.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreescuisine.ModConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class FeatureGenFruitLeaves implements IPostGenFeature, IPostGrowFeature {
    public int boxSize, boxHeight;
    public Block leaf;
    public float worldGenProportion;
    protected int fruitingRadius = 8;

    public FeatureGenFruitLeaves(int size, int height, ILeavesProperties[] leafType, float proportionForWorldgen){
        boxSize = size;
        boxHeight = height;
        leaf = leafType[0].getDynamicLeavesState().getBlock();
        worldGenProportion = proportionForWorldgen;
    }

    public FeatureGenFruitLeaves setFruitingRadius(int fruitingRadius) {
        this.fruitingRadius = fruitingRadius;
        return this;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if (ModConfigs.fruityLeaves && (TreeHelper.getRadius(world, rootPos.up()) >= fruitingRadius) && natural)
            changeRandLeaf(world, rootPos, 10, species);
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

    private boolean isLeavesValid (World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == leaf && state.getValue(BlockDynamicLeaves.HYDRO) == 1;
    }

    private void attemptLeafChange(World world, BlockPos pos, boolean worldGen, Species species, SafeChunkBounds safeBounds){
        if (worldGen){
            if (safeBounds.inBounds(pos, true) && isLeavesValid(world, pos) && world.rand.nextFloat() <= species.seasonalFruitProductionFactor(world, pos)){
                int age = world.rand.nextInt(4);
                if (species.testFlowerSeasonHold(world, pos, getSeasonValue(world, pos))) age = Math.max(age, 2);
                world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE,  age));
            }
        }
        else if (isLeavesValid(world, pos)){
            int growthStage = world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE);
            if (growthStage == 3){
                world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
                return;
            }
            if (world.rand.nextFloat() <= species.seasonalFruitProductionFactor(world, pos)) {
                switch (growthStage) {
                    case 0:
                        world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
                        break;
                    case 1:
                        world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
                        break;
                    case 2:
                        if (!species.testFlowerSeasonHold(world, pos, getSeasonValue(world, pos)))
                            world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 3));
                        break;
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
