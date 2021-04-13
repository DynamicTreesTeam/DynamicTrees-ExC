package maxhyper.dynamictreesic2.genfeatures;

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

import java.util.LinkedList;
import java.util.List;

public class FeatureGenSapLog implements IPostGenFeature, IPostGrowFeature {

    private int height;
    private BlockBranch branchEmpty, branchHole, branchFull;
    private float holeToFilledChance = 0.02f;
    private float emptyToHoleChance = 0.02f;
    protected int fruitingRadius = 8;
    private int worldgenAttempts = 5;

    public FeatureGenSapLog(int height, BlockBranch branch1, BlockBranch branch2, BlockBranch branch3){
        this.height = height;
        this.branchEmpty = branch1;
        this.branchHole = branch2;
        this.branchFull = branch3;
    }

    public FeatureGenSapLog setFruitingRadius(int fruitingRadius) {
        this.fruitingRadius = fruitingRadius;
        return this;
    }

    public FeatureGenSapLog setEmptyToHoleChance(float emptyToHoleChance) {
        this.emptyToHoleChance = emptyToHoleChance;
        return this;
    }

    public FeatureGenSapLog setHoleToFilledChance(float holeToFilledChance) {
        this.holeToFilledChance = holeToFilledChance;
        return this;
    }

    public FeatureGenSapLog setWorldgenAttempts(int worldgenAttempts) {
        this.worldgenAttempts = worldgenAttempts;
        return this;
    }

    @Override
    public boolean postGeneration(World world, BlockPos blockPos, Species species, Biome biome, int i, List<BlockPos> list, SafeChunkBounds safeChunkBounds, IBlockState iBlockState) {
        return attemptSapGeneration(world, blockPos,true);
    }

    @Override
    public boolean postGrow(World world, BlockPos blockPos, BlockPos blockPos1, Species species, int i, boolean natural) {
        if (natural) return attemptSapGeneration(world, blockPos, false);
        return false;
    }

    private int getTreeHeight (World world, BlockPos rootPos){
        for (int y = 1; y <= height; y++){
            if (!TreeHelper.isBranch(world.getBlockState(rootPos.up(y)))) return y - 1;
        }
        return height;
    }
    private List<BlockPos> getHolePositions (World world, BlockPos rootPos, int treeHeight){
        List<BlockPos> list = new LinkedList<>();
        for (int y = 1; y <= treeHeight; y++){
            BlockPos testPos = rootPos.up(y);
            if (world.getBlockState(testPos).getBlock() == branchHole)
                list.add(testPos);
        }
        return list;
    }

    private boolean attemptSapGeneration (World world, BlockPos rootPos, boolean worldgen){
        int treeHeight = getTreeHeight(world, rootPos);
        if (worldgen){
            boolean generated = false;
            for (int i = 0; i < worldgenAttempts; i++)
                if (generateSap(world, rootPos, true, treeHeight))
                    generated = true;
            return generated;
        } else {
            if (world.rand.nextFloat() < holeToFilledChance){
                fillSapHole(world, getHolePositions(world, rootPos, treeHeight));
            }
            if (world.rand.nextFloat() < emptyToHoleChance)
                return generateSap(world, rootPos, false, treeHeight);
        }
        return false;
    }

    private boolean generateSap (World world, BlockPos rootPos, boolean filled, int treeHeight){
        BlockPos sapPos = rootPos.up(1 + world.rand.nextInt(treeHeight));

        IBlockState downState = world.getBlockState(sapPos.down());
        boolean downIsSap = downState.getBlock() == branchHole || downState.getBlock() == branchFull;

        int radius = TreeHelper.getRadius(world, sapPos);
        if (!downIsSap && world.getBlockState(sapPos).getBlock() == branchEmpty && radius >= fruitingRadius){
            world.setBlockState(sapPos, filled ? branchFull.getStateForRadius(radius) : branchHole.getStateForRadius(radius));
            return true;
        }
        return false;
    }

    private void fillSapHole (World world, List<BlockPos> holePositions){
        if (holePositions.size() <= 0) return;
        BlockPos chosenPos = holePositions.get(world.rand.nextInt(holePositions.size()));
        if (world.getBlockState(chosenPos).getBlock() == branchHole){
            int radius = TreeHelper.getRadius(world, chosenPos);
            world.setBlockState(chosenPos, branchFull.getStateForRadius(radius));
        }
    }

}

