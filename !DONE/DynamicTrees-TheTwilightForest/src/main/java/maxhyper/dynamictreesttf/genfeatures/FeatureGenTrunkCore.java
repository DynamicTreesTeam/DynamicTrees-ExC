package maxhyper.dynamictreesttf.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils.Surround;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesttf.blocks.BlockBranchMagicCore;
import maxhyper.dynamictreesttf.blocks.BlockBranchMagicCoreThick;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class FeatureGenTrunkCore implements IPostGenFeature, IPostGrowFeature {

    public BlockBranch coreBranch;
    public int minRadiusForCore;
    public int coreTrunkHeight;
    public boolean thick;
    public static Surround thickCoreDir;

    public FeatureGenTrunkCore (BlockBranch core, int minCoreRadius, int coreHeight, Surround spawnDir){
        coreBranch = core;
        minRadiusForCore = minCoreRadius;
        coreTrunkHeight = coreHeight;
        thickCoreDir = spawnDir;
        thick = true;
    }

    public FeatureGenTrunkCore (BlockBranch core, int minCoreRadius, int coreHeight){
        coreBranch = core;
        minRadiusForCore = minCoreRadius;
        coreTrunkHeight = coreHeight;
        thick = false;
    }

    @Override
    public boolean postGeneration(World world, BlockPos blockPos, Species species, Biome biome, int i, List<BlockPos> list, SafeChunkBounds safeChunkBounds, IBlockState iBlockState) {
        if (thick){
            world.setBlockState(blockPos.up(coreTrunkHeight).add(thickCoreDir.getOpposite().getOffsetPos()), coreBranch.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUSNYBBLE, 7));
        } else {
            world.setBlockState(blockPos.up(coreTrunkHeight), coreBranch.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, world.getBlockState(blockPos.up(coreTrunkHeight)).getValue(BlockDynamicTwilightRoots.RADIUS)));
        }
        return false;
    }

    @Override
    public boolean postGrow(World world, BlockPos blockPos, BlockPos blockPos1, Species species, int i, boolean b) {
        IBlockState trunkState = world.getBlockState(blockPos.up(coreTrunkHeight));
        if (trunkState.getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS) || trunkState.getProperties().containsKey(BlockDynamicTwilightRoots.RADIUSNYBBLE)){
            if (thick){
                if (((BlockBranchThick)trunkState.getBlock()).getRadius(trunkState) >= minRadiusForCore){
                    if (minRadiusForCore > 15){
                        if (trunkState.getBlock() != ((BlockBranchThick)coreBranch).otherBlock && trunkState.getBlock() != ((BlockBranchMagicCoreThick)coreBranch).switchedBlock.otherBlock){
                            world.setBlockState(blockPos.up(coreTrunkHeight), ((BlockBranchThick)coreBranch).otherBlock.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUSNYBBLE, trunkState.getValue(BlockDynamicTwilightRoots.RADIUSNYBBLE)));
                        }
                    } else {
                        if (trunkState.getBlock() != coreBranch && trunkState.getBlock() != ((BlockBranchMagicCoreThick)coreBranch).switchedBlock){
                            world.setBlockState(blockPos.up(coreTrunkHeight), coreBranch.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUSNYBBLE, trunkState.getValue(BlockDynamicTwilightRoots.RADIUSNYBBLE)));
                        }
                    }
                }
            } else if (trunkState.getValue(BlockDynamicTwilightRoots.RADIUS) >= minRadiusForCore) {
                if (trunkState.getBlock() != coreBranch && trunkState.getBlock() != ((BlockBranchMagicCore)coreBranch).switchedBlock){
                    world.setBlockState(blockPos.up(coreTrunkHeight), coreBranch.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, world.getBlockState(blockPos.up(coreTrunkHeight)).getValue(BlockDynamicTwilightRoots.RADIUS)));
                }
            }
        }
        return true;
    }
}
