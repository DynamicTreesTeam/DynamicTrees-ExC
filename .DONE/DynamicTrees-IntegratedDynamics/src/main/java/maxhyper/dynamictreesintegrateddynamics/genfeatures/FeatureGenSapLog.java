package maxhyper.dynamictreesintegrateddynamics.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class FeatureGenSapLog implements IPostGenFeature, IPostGrowFeature {

    private int height;
    private BlockBranch branchEmpty, branchFull;
    private float sapChance = 0.02f;
    protected int fruitingRadius = 8;
    private int worldgenAttempts = 5;

    public FeatureGenSapLog(int height, BlockBranch branch1, BlockBranch branch2){
        this.height = height;
        this.branchEmpty = branch1;
        this.branchFull = branch2;
    }

    public FeatureGenSapLog setFruitingRadius(int fruitingRadius) {
        this.fruitingRadius = fruitingRadius;
        return this;
    }

    public FeatureGenSapLog setSapChance(float sapChance) {
        this.sapChance = sapChance;
        return this;
    }

    public FeatureGenSapLog setWorldgenAttempts(int worldgenAttempts) {
        this.worldgenAttempts = worldgenAttempts;
        return this;
    }

    @Override
    public boolean postGeneration(World world, BlockPos blockPos, Species species, Biome biome, int i, List<BlockPos> list, SafeChunkBounds safeChunkBounds, IBlockState iBlockState) {
        return attemptSapGeneration(world, blockPos, worldgenAttempts, true);
    }

    @Override
    public boolean postGrow(World world, BlockPos blockPos, BlockPos blockPos1, Species species, int i, boolean natural) {
        if (natural) return attemptSapGeneration(world, blockPos, 1, false);
        return false;
    }

    private int getTreeHeight (World world, BlockPos rootPos){
        for (int y = 1; y <= height; y++){
            if (!TreeHelper.isBranch(world.getBlockState(rootPos.up(y)))) return y - 1;
        }
        return height;
    }

    private boolean attemptSapGeneration (World world, BlockPos rootPos, int attempts, boolean worldgen){
        int counter = 0;
        do {
            if (worldgen || world.rand.nextFloat() < sapChance){
                BlockPos sapPos = rootPos.up(1 + world.rand.nextInt(getTreeHeight(world, rootPos)));

                IBlockState downState = world.getBlockState(sapPos.down());
                boolean downIsSap = downState.getBlock() == branchFull;

                int radius = TreeHelper.getRadius(world, sapPos);
                if (!downIsSap && world.getBlockState(sapPos).getBlock() == branchEmpty && radius >= fruitingRadius)
                    world.setBlockState(sapPos, branchFull.getStateForRadius(radius));

            }
            counter++;
        } while (counter < attempts);
        return false;
    }

}
