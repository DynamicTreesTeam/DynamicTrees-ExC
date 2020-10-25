package maxhyper.dynamictreestheaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockSnowBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class FeatureGenSnowArea implements IPostGenFeature {

    public int snowRadius;
    public IBlockState present;
    public int presentOdds;

    private static boolean gap = true;

    public FeatureGenSnowArea(int snowAreaRadius, IBlockState specialBlock, int specialBlockOdds){
        snowRadius = snowAreaRadius;
        present = specialBlock;
        presentOdds = specialBlockOdds;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        generateSnow(world, rootPos, safeBounds);
        return true;
    }

    public boolean addSnowLayer(World world, BlockPos pos, Random rand, SafeChunkBounds safeBounds){
        BlockPos blockpos = new BlockPos(pos.getX(), world.getHeight(pos.getX(),pos.getZ()),pos.getZ());
        if (!safeBounds.inBounds(blockpos, gap) || blockpos.getY() <= 1) return false;
        if (rand.nextInt(presentOdds) == 0){
            safeBounds.setBlockState(world, blockpos, present, gap);
            return true;
        }
        if (world.getBlockState(blockpos.down()).getBlock() == Blocks.SNOW_LAYER && world.getBlockState(blockpos.down()).getValue(BlockSnow.LAYERS) < 8 ){
            safeBounds.setBlockState(world, blockpos.down(), Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, world.getBlockState(blockpos.down()).getValue(BlockSnow.LAYERS)+1), gap);
            return true;
        } else if (Blocks.SNOW_LAYER.canPlaceBlockAt(world, blockpos) && blockpos.getY()>0){
            safeBounds.setBlockState(world, blockpos,Blocks.SNOW_LAYER.getDefaultState(), gap);
            return true;
        }
        return false;
    }

    public void generateSnow(World world, BlockPos rootPos, SafeChunkBounds safeBounds){
        Random rand = new Random();
        for(int x = rootPos.getX() - snowRadius; x<rootPos.getX() + snowRadius;x++){
            for(int z = rootPos.getZ() - snowRadius; z<rootPos.getZ() + snowRadius;z++){
                double distFromRoot = Math.sqrt(Math.pow(rootPos.getX()-x, 2) + Math.pow(rootPos.getZ()-z,2));
                if(distFromRoot <= snowRadius){
                    if (rand.nextFloat()*(distFromRoot/snowRadius) <= 0.5){
                        addSnowLayer(world,new BlockPos(x,0, z), rand, safeBounds); //y doesnt matter as its reset in addSnowLayer
                    }
                }
            }
        }
    }
}
