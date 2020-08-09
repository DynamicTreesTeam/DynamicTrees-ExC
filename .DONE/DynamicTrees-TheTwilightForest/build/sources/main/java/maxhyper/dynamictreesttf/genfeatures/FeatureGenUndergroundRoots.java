package maxhyper.dynamictreesttf.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
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

    public BlockDynamicTwilightRoots rootsDirt, rootsExposed, rootsDirt2;
    public BlockBranch rootsExposed2;
    public int rootBranchChance, maxRadius, rootGrowChance, rootTypeChangeHeight;
    public boolean isMangrove;

    public int worldgenAttempts = 20;

    public FeatureGenUndergroundRoots(BlockDynamicTwilightRoots rootDirt, BlockDynamicTwilightRoots rootDirt2, BlockDynamicTwilightRoots root, BlockBranch root2, int heightToChangeRoot, int maxRootRadius, int branchChance, int growChance){
        rootsDirt = rootDirt;
        rootsExposed = root;
        rootBranchChance = branchChance;
        maxRadius = Math.min(maxRootRadius, 8);
        rootGrowChance = growChance;
        isMangrove = true;
        rootsExposed2 = root2;
        rootsDirt2 = rootDirt2;
        rootTypeChangeHeight = heightToChangeRoot;
    }

    public FeatureGenUndergroundRoots(BlockDynamicTwilightRoots rootDirt, BlockDynamicTwilightRoots root, int maxRootRadius, int branchChance, int growChance){
        rootsDirt = rootDirt;
        rootsExposed = root;
        rootBranchChance = branchChance;
        maxRadius = Math.min(maxRootRadius, 8);
        rootGrowChance = growChance;
        isMangrove = false;
    }

    EnumFacing[] dirsExceptUp = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
    EnumFacing[] dirsAll = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.UP};


    private boolean isGroundBlock(Block block){
        return block == Blocks.DIRT || block == Blocks.GRASS_PATH || block == Blocks.SAND || block == Blocks.GRAVEL || isGrassBlock(block);
    }
    private boolean isGrassBlock(Block block){
        return block == Blocks.GRASS;
    }
    private boolean isRootBlock(Block block){ return block == rootsDirt || block == rootsExposed || block == rootsDirt2 || block == rootsExposed2; }

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
            if (dirsExceptUp[i] == cameFrom || (isMangrove && dirsExceptUp[i]==EnumFacing.DOWN && blockPos.up().getX() == rootPos.getX() &&  blockPos.up().getZ() == rootPos.getZ() )){
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
        return offsetSpawn(pos, !isMangrove) != rootPos;
    }

    private boolean cancelGrowChance(Random rand){
        if (isMangrove){
            return rand.nextInt(4) == 0;
        } else {
            return rand.nextInt(2) == 0;
        }
    }

    private boolean iterateRootGrow(World world, BlockPos blockPos, Random rand, int radius, EnumFacing cameFrom, BlockPos rootPos){
        IBlockState state = world.getBlockState(blockPos);
        if (isRootBlock(state.getBlock())){
            int currentRadius = state.getValue(BlockDynamicTwilightRoots.RADIUS);
            boolean grow = isOverRootBlock(blockPos, rootPos) && currentRadius<radius && currentRadius<8;
            radius = currentRadius;
            world.setBlockState(blockPos, state.withProperty(BlockDynamicTwilightRoots.RADIUS, currentRadius + (grow?1:0) ));
        } else if (world.isAirBlock(blockPos) || state.getBlock() == Blocks.WATER  || state.getBlock() == Blocks.FLOWING_WATER){
            if (!cancelGrowChance(rand)) {
                if (blockPos.getY() > rootPos.getY() - rootTypeChangeHeight) {
                    world.setBlockState(blockPos, rootsExposed2.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, radius > 4 ? radius / 2 : 2));
                } else {
                    world.setBlockState(blockPos, rootsExposed.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, radius > 4 ? radius / 2 : 2));
                }
            } else {
                return false;
            }
        } else if (isGroundBlock(state.getBlock())){
            if (blockPos.getY() > rootPos.getY() - rootTypeChangeHeight){
                world.setBlockState(blockPos, rootsDirt2.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, radius>4?radius/2:2).withProperty(BlockDynamicTwilightRoots.GRASSY, isGrassBlock(state.getBlock())));
            } else {
                if (!isMangrove || rand.nextInt(2) != 0) {
                    world.setBlockState(blockPos, rootsDirt.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, radius>4?radius/2:2).withProperty(BlockDynamicTwilightRoots.GRASSY, isGrassBlock(state.getBlock())));
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (rand.nextFloat() <= 0.2)
            radius--;
        if (radius > 2){
            EnumFacing chosenDir = findRandomRootDir(world,blockPos,rand, cameFrom, rootPos);
            if (chosenDir == null) return true;
            iterateRootGrow(world,blockPos.offset(chosenDir), rand, radius, chosenDir.getOpposite(), rootPos);
            if (isOverRootBlock(blockPos, rootPos) || rand.nextInt(rootBranchChance) == 0){
                chosenDir = findRandomFreeDir(world,blockPos, rand, cameFrom, rootPos);
                if (chosenDir == null) return true;
                iterateRootGrow(world,blockPos.offset(chosenDir), rand, radius, chosenDir.getOpposite(), rootPos);
            }
        }
        return true;
    }

    public BlockPos offsetSpawn(BlockPos root){
        return offsetSpawn(root, isMangrove);
    }
    public BlockPos offsetSpawn(BlockPos root, boolean invert){
        if (invert){
            return root.up();
        } else {
            return root.down();
        }
    }

    @Override
    public boolean postGeneration(World world, BlockPos blockPos, Species species, Biome biome, int i, List<BlockPos> list, SafeChunkBounds safeChunkBounds, IBlockState iBlockState) {
        Random rand = new Random();
        if (!world.getBlockState(offsetSpawn(blockPos)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS)){
            boolean placed = iterateRootGrow(world, offsetSpawn(blockPos), rand, 2, EnumFacing.UP, blockPos);
            if (!placed) return false;
        }
        for (int a=worldgenAttempts; a>0; a--){
            int radius = world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS);
            int bound = (radius/2)*rootGrowChance;
            boolean grow = !isMangrove && bound > 0 && rand.nextInt(bound) == 0;
            iterateRootGrow(world, offsetSpawn(blockPos), rand, world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS)+(grow?1:0), EnumFacing.UP, blockPos);
            if (isMangrove && world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS) == maxRadius){
                world.setBlockState(blockPos, rootsExposed2.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, 8));
            }
        }
        return true;
    }
    @Override
    public boolean postGrow(World world, BlockPos blockPos, BlockPos blockPos1, Species species, int i, boolean b) {
        Random rand = new Random();
        if (!world.getBlockState(offsetSpawn(blockPos)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS)){
            boolean placed = iterateRootGrow(world, offsetSpawn(blockPos), rand, 2, EnumFacing.UP, blockPos);
            if (!placed) return false;
        } else if (world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS) < maxRadius){
            int radius = world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS);
            int bound = (radius/2)*rootGrowChance;
            boolean grow = !isMangrove && bound > 0 && rand.nextInt(bound) == 0;
            iterateRootGrow(world, offsetSpawn(blockPos), rand, world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS)+(grow?1:0), EnumFacing.UP, blockPos);
        } //else if (isMangrove && world.getBlockState(offsetSpawn(blockPos)).getValue(BlockDynamicTwilightRoots.RADIUS) == maxRadius){
          //  world.setBlockState(blockPos, rootsDirt2.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, 8));
          //}
        return true;
    }
}
