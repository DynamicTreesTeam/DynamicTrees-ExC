package maxhyper.dynamictreesttf.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import twilightforest.block.BlockTFCritter;

import java.util.List;
import java.util.Random;

public class FeatureGenLogCritter implements IPostGenFeature, IPostGrowFeature {

    public int heightLimit;
    public BlockTFCritter critterBlock;
    public int chanceRand;
    public int separationHeight;
    public int worldgenAttempts = 100;

    public FeatureGenLogCritter(int heightLim, BlockTFCritter critter, int chance, int separation){
        heightLimit = heightLim;
        critterBlock = critter;
        chanceRand = chance;
        separationHeight = separation;
    }

    EnumFacing[] horizontalsDir = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};

    private boolean checkForOtherCrittersHorizontally(World world, BlockPos blockPos, int heightToCheck){
        for(int i = blockPos.getY()-heightToCheck; i<blockPos.getY()+heightToCheck; i++){
            for(int j=0; j<4;j++){
                BlockPos pos = new BlockPos(blockPos.getX(), i, blockPos.getZ());
                if (world.getBlockState(pos.offset(horizontalsDir[j])).getBlock() == critterBlock){
                    return false;
                }
            }
        }
        return true;
    }

    private void attemptCritterPlacing (World world, BlockPos blockPos, Random rand){
        EnumFacing chosenDir = horizontalsDir[rand.nextInt(4)];
        try{
            BlockBranchBasic branch = (BlockBranchBasic)(world.getBlockState(blockPos).getBlock());
            if (checkForOtherCrittersHorizontally(world, blockPos, separationHeight) && world.isAirBlock(blockPos.offset(chosenDir)) && branch.getRadius(world.getBlockState(blockPos)) >= BlockBranchTwilight.minCritterRadius) {
                world.setBlockState(blockPos.offset(chosenDir), critterBlock.getDefaultState().withProperty(BlockDirectional.FACING, chosenDir));
            }
        }catch (Exception e){}
    }

    @Override
    public boolean postGeneration(World world, BlockPos blockPos, Species species, Biome biome, int i, List<BlockPos> list, SafeChunkBounds safeChunkBounds, IBlockState iBlockState) {
        for (int j=0;j<worldgenAttempts;j++){
            Random rand = new Random();
            if (rand.nextInt(chanceRand) == 0){
                int chosenHeight = 3 + rand.nextInt(heightLimit-2);
                attemptCritterPlacing(world, new BlockPos(blockPos.getX(), blockPos.getY() + chosenHeight, blockPos.getZ()), rand);
            }
        }
        return true;
    }

    @Override
    public boolean postGrow(World world, BlockPos blockPos, BlockPos blockPos1, Species species, int soilLife, boolean natural) {
        if (!natural) return false;
        Random rand = new Random();
        if (rand.nextInt(chanceRand) == 0){
            int chosenHeight = 3 + rand.nextInt(heightLimit-2);
            attemptCritterPlacing(world, new BlockPos(blockPos.getX(), blockPos.getY() + chosenHeight, blockPos.getZ()), rand);
        }
        return true;
    }
}
