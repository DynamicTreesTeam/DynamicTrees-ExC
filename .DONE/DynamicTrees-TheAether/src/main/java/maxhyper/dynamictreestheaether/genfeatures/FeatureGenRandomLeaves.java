package maxhyper.dynamictreestheaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreestheaether.ModConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class FeatureGenRandomLeaves implements IPostGenFeature, IPostGrowFeature {
    public int boxSize, boxHeight;
    public Block leaf;
    public float worldGenProportion;
    protected int fruitingRadius = 8;
    protected int treeEmpty, treeFruit;
    protected boolean enableGrowth;

    public FeatureGenRandomLeaves(int size, int height, Block leafBlock, int treeEmpty, int treeFruit, float proportionForWorldgen, boolean enableGrowth){
        boxSize = size;
        boxHeight = height;
        leaf = leafBlock;
        this.treeEmpty = treeEmpty;
        this.treeFruit = treeFruit;
        worldGenProportion = proportionForWorldgen;
        this.enableGrowth = enableGrowth;
    }

    public FeatureGenRandomLeaves setFruitingRadius(int fruitingRadius) {
        this.fruitingRadius = fruitingRadius;
        return this;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if (enableGrowth && ModConfigs.fruityLeaves && (TreeHelper.getRadius(world, rootPos.up()) >= fruitingRadius) && natural)
            changeRandLeaf(world, rootPos, 2, species);
        return false;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
            changeLeaves(world, rootPos, worldGenProportion, species, safeBounds);
        return true;
    }

    private boolean isLeavesValid (World world, BlockPos pos){
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == leaf && state.getValue(BlockDynamicLeaves.HYDRO) == 1;
    }

    private void attemptLeafChange(World world, BlockPos pos, boolean worldGen, Species species, SafeChunkBounds safeBounds){
        if (worldGen){
            if (safeBounds.inBounds(pos, true) && isLeavesValid(world, pos) && world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE) == treeEmpty){
                world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE,  treeFruit));
            }
        }
        else if (isLeavesValid(world, pos)){
            int treeValue = world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE);
            if (treeValue == treeFruit){
                world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE, treeEmpty));
                return;
            }
            if (world.rand.nextFloat() <= species.seasonalFruitProductionFactor(world, pos)) {
                if (treeValue == treeEmpty)
                    world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE, treeFruit));
                else if (treeValue == treeFruit)
                    world.setBlockState(pos, leaf.getDefaultState().withProperty(BlockDynamicLeaves.TREE, treeEmpty));
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
