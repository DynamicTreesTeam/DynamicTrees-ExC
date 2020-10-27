package maxhyper.dynamictreesic2.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class FeatureGenSapLog implements IPostGenFeature {

    protected static final PropertyInteger RADIUS = PropertyInteger.create("radius", 1, 8);
    private int height;
    private BlockBranch branchEmpty,branchFull;
    private float sapChance = 0.6f;
    private float filledSapChance = 0.4f;

    public FeatureGenSapLog (int height, BlockBranch branch1, BlockBranch branch2){
        this.height = height;
        branchEmpty = branch1;
        branchFull = branch2;
    }
    public FeatureGenSapLog (int height, BlockBranch branch1, BlockBranch branch2, float sapChance, float filledSapChance){
        this(height, branch1, branch2);
        this.sapChance = sapChance;
        this.filledSapChance = filledSapChance;
    }

    @Override
    public boolean postGeneration(World world, BlockPos blockPos, Species species, Biome biome, int i, List<BlockPos> list, SafeChunkBounds safeChunkBounds, IBlockState iBlockState) {
        boolean previousWasSap = false;
        for (int y = 1; y <= height;y++){
            BlockPos testPos = blockPos.up(y);
            IBlockState testState =  world.getBlockState(testPos);
            if (!previousWasSap && TreeHelper.isBranch(testState) && world.rand.nextFloat() < sapChance){
                //only continue if the branch underneath has no sap
                 int testRadius = TreeHelper.getRadius(world, testPos);
                    if (testRadius > 6){
                        if (world.rand.nextFloat() < filledSapChance){
                            world.setBlockState(testPos, branchFull.getDefaultState().withProperty(RADIUS, testRadius));
                        } else {
                            world.setBlockState(testPos, branchEmpty.getDefaultState().withProperty(RADIUS, testRadius));
                        }
                        previousWasSap = true;
                    } else {
                        previousWasSap = false;
                    }
            } else {
                previousWasSap = false;
            }
        }
        return false;
    }
}
