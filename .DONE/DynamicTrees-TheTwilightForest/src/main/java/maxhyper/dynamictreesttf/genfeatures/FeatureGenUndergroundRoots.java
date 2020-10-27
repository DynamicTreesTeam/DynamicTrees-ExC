package maxhyper.dynamictreesttf.genfeatures;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;

public class FeatureGenUndergroundRoots implements IPostGenFeature, IPostGrowFeature {

    public BlockDynamicTwilightRoots rootsDirt, rootsExposed;
    public int rootBranchChance, maxRadius, rootGrowChance;

    public int worldgenAttempts = 20;

    public int maxDepth = 20;


    public FeatureGenUndergroundRoots(BlockDynamicTwilightRoots rootDirt, BlockDynamicTwilightRoots root, int maxRootRadius, int branchChance, int growChance){
        rootsDirt = rootDirt;
        rootsExposed = root;
        rootBranchChance = branchChance;
        maxRadius = Math.min(maxRootRadius, 8);
        rootGrowChance = growChance;
    }

    EnumFacing[] dirsExceptUp = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
    EnumFacing[] dirsAll = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.UP};

    private boolean isGroundBlock(Block block){
        return DirtHelper.isSoilAcceptable(block, DirtHelper.getSoilFlags(DirtHelper.DIRTLIKE));
    }
    private boolean isGrassBlock(Block block){
        return block == Blocks.GRASS;
    }
    private boolean isRootBlock(Block block){ return block == rootsDirt || block == rootsExposed; }

    private boolean checkAvailableAround(World world, BlockPos blockPos, EnumFacing cameFrom){
        for (int i=0;i<6;i++){
            if (dirsAll[i] == cameFrom){
                continue;
            }
            if(world.isBlockLoaded(blockPos.offset(dirsAll[i]))) {
                IBlockState offsetState = world.getBlockState(blockPos.offset(dirsAll[i]));
                if (offsetState.getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS)){
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private EnumFacing findRandomFreeDir(World world, BlockPos blockPos, Random rand, EnumFacing cameFrom, BlockPos rootPos){
        if (rand.nextInt(3) == 0){
            return null;
        }
        EnumFacing[] possibleDirections = new EnumFacing[5];
        int dirCount = 0;
        for (int i=0;i<5;i++){
            if (dirsExceptUp[i] == cameFrom){
                continue;
            }
            IBlockState offsetState = world.getBlockState(blockPos.offset(dirsExceptUp[i]));
            Block offsetBlock = world.getBlockState(blockPos.offset(dirsExceptUp[i])).getBlock();
            if ((world.isAirBlock(blockPos.offset(dirsExceptUp[i])) || offsetBlock == Blocks.WATER || offsetBlock == Blocks.FLOWING_WATER || isGroundBlock(offsetState.getBlock())) && checkAvailableAround(world,blockPos.offset(dirsExceptUp[i]), dirsExceptUp[i].getOpposite())){
                if (dirsExceptUp[i]==EnumFacing.DOWN && rand.nextInt(8) != 0){
                    return EnumFacing.DOWN;
                }
                possibleDirections[dirCount] = dirsExceptUp[i];
                dirCount++;
            }
        }
        if (dirCount == 0) return null;
        return possibleDirections[rand.nextInt(dirCount)];
    }

    private EnumFacing findRandomRootDir(World world, BlockPos blockPos, Random rand, EnumFacing cameFrom, BlockPos rootPos){
        EnumFacing[] possibleDirections = new EnumFacing[5];
        int dirCount = 0;
        for (int i=0;i<5;i++){
            if (dirsExceptUp[i] == cameFrom){
                continue;
            }
            IBlockState offsetState = world.getBlockState(blockPos.offset(dirsExceptUp[i]));
            if ( isRootBlock(offsetState.getBlock())  ){
                if (dirsExceptUp[i]==EnumFacing.DOWN && rand.nextInt(4) != 0){
                    return EnumFacing.DOWN;
                }
                possibleDirections[dirCount] = dirsExceptUp[i];
                dirCount++;
            }
        }
        if (dirCount == 0) return findRandomFreeDir(world,blockPos,rand, cameFrom, rootPos);
        return possibleDirections[rand.nextInt(dirCount)];
    }

    private boolean isOverRootBlock(BlockPos pos, BlockPos rootPos){
        return offsetSpawn(pos, true) != rootPos;
    }

    public BlockPos offsetSpawn(BlockPos root){
        return offsetSpawn(root, false);
    }
    public BlockPos offsetSpawn(BlockPos root, boolean invert){
        if (invert){
            return root.up();
        } else {
            return root.down();
        }
    }

    private boolean cancelGrowChance(Random rand){
        return rand.nextInt(2) == 0;
    }

    private boolean iterateRootGrow(World world, BlockPos blockPos, Random rand, int radius, EnumFacing cameFrom, BlockPos rootPos, int currentStep){
        if (world.getBlockState(rootPos).getBlock() != ModBlocks.blockRootyDirt) return false;
        if (currentStep > maxDepth) return false;
        IBlockState state = world.getBlockState(blockPos);
        if (isRootBlock(state.getBlock())){
            int currentRadius = state.getValue(BlockDynamicTwilightRoots.RADIUS);
            boolean grow = isOverRootBlock(blockPos, rootPos) && currentRadius<radius && currentRadius<8;
            radius = currentRadius;
            world.setBlockState(blockPos, state.withProperty(BlockDynamicTwilightRoots.RADIUS, currentRadius + (grow?1:0) ));
        } else if (world.isAirBlock(blockPos)){
            if (!cancelGrowChance(rand)) {
                world.setBlockState(blockPos, rootsExposed.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, radius > 4 ? radius / 2 : 2));
            } else {
                return false;
            }
        } else if (isGroundBlock(state.getBlock())){
            world.setBlockState(blockPos, rootsDirt.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, radius>4?radius/2:2).withProperty(BlockDynamicTwilightRoots.GRASSY, isGrassBlock(state.getBlock())));
        } else {
            return false;
        }
        if (rand.nextFloat() <= 0.2)
            radius--;
        if (radius > 0){
            EnumFacing chosenDir = findRandomRootDir(world,blockPos,rand, cameFrom, rootPos);
            if (chosenDir == null) return true;
            iterateRootGrow(world,blockPos.offset(chosenDir), rand, radius, chosenDir.getOpposite(), rootPos, currentStep + 1);
            if ( (rootPos.getY() == blockPos.getY()) || rand.nextInt(rootBranchChance) == 0){ //isOverRootBlock(blockPos, rootPos) ||
                chosenDir = findRandomFreeDir(world,blockPos, rand, cameFrom, rootPos);
                if (chosenDir == null) return true;
                iterateRootGrow(world,blockPos.offset(chosenDir), rand, radius, chosenDir.getOpposite(), rootPos, currentStep + 1);
            }
        }
        return true;
    }

    @Override
    public boolean postGeneration(World world, BlockPos blockPos, Species species, Biome biome, int i, List<BlockPos> list, SafeChunkBounds safeChunkBounds, IBlockState iBlockState) {
        Random rand = new Random();
        if (!world.getBlockState(offsetSpawn(blockPos)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS)){
            boolean placed = iterateRootGrow(world, offsetSpawn(blockPos), rand, 2, EnumFacing.UP, blockPos, 0);
            if (!placed) return false;
        }
        for (int a=worldgenAttempts; a>0; a--){
            int radius = world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS);
            int bound = (radius/2)*rootGrowChance;
            boolean grow = bound > 0 && rand.nextInt(bound) == 0;
            iterateRootGrow(world, offsetSpawn(blockPos), rand, world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS)+(grow?1:0), EnumFacing.UP, blockPos, 0);
        }
        return true;
    }
    @Override
    public boolean postGrow(World world, BlockPos blockPos, BlockPos blockPos1, Species species, int i, boolean b) {
        Random rand = new Random();
        if (!world.getBlockState(offsetSpawn(blockPos)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS)){
            return iterateRootGrow(world, offsetSpawn(blockPos), rand, 2, EnumFacing.UP, blockPos, 0);
        } else if (world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS) < maxRadius){
            int radius = world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS);
            int bound = (radius/2)*rootGrowChance;
            boolean grow = bound > 0 && rand.nextInt(bound) == 0;
            iterateRootGrow(world, offsetSpawn(blockPos), rand, world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS)+(grow?1:0), EnumFacing.UP, blockPos, 0);
        }
        return true;
    }
}
