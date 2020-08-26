package maxhyper.dynamictreestheaether.genfeatures;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class FeatureGenRandomLeaves implements IPostGenFeature, IPostGrowFeature {
    public int boxSize, boxHeight;
    public static IBlockState leaf1, leaf2;
    public float worldGenProportion;

    public FeatureGenRandomLeaves(int size, int height,  IBlockState leafType1, IBlockState leafType2, float proportionForWorldgen){
        boxSize = size;
        boxHeight = height;
        leaf1 = leafType1;
        leaf2 = leafType2;
        worldGenProportion = proportionForWorldgen;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        //changeRandLeaf(world, rootPos, 1);
        return false;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        changeLeaves(world, rootPos, worldGenProportion);
        return true;
    }

    private void attemptLeafChange(World world, BlockPos pos){
        if (world.getBlockState(pos).getBlock() == leaf1.getBlock() && world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE).equals(leaf1.getValue(BlockDynamicLeaves.TREE))){
            world.setBlockState(pos, leaf2);
        } else if (world.getBlockState(pos).getBlock() == leaf2.getBlock() && world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE).equals(leaf2.getValue(BlockDynamicLeaves.TREE))){
            world.setBlockState(pos, leaf1);
        }
    }

    private void changeRandLeaf(World world, BlockPos rootPos, int attempts){
        Random rand = new Random();
        do {
            int randX = rootPos.getX() + rand.nextInt((2*boxSize)+1) - boxSize;
            int randZ = rootPos.getZ() + rand.nextInt((2*boxSize)+1) - boxSize;
            int randY = rootPos.getY() + rand.nextInt(boxHeight + 1);
            attemptLeafChange(world, new BlockPos(randX,randY,randZ));
        } while ((attempts--)>0);
    }

    private void changeLeaves(World world, BlockPos rootPos, float proportion){
        if (proportion <= 0 || proportion > 1) return;
        Random rand = new Random();
        for(int x=rootPos.getX()-boxSize; x<rootPos.getX()+2*boxSize; x++){
            for(int z=rootPos.getZ()-boxSize; z<rootPos.getZ()+2*boxSize; z++){
                for(int y=rootPos.getY(); y<rootPos.getY()+boxHeight; y++){
                    if (rand.nextFloat()*(1/proportion) <= 1){
                        attemptLeafChange(world, new BlockPos(x,y,z));
                    }
                }
            }
        }
    }

}
