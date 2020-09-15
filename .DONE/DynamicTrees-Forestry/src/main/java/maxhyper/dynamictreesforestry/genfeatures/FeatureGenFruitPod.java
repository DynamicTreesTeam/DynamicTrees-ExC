package maxhyper.dynamictreesforestry.genfeatures;

import java.util.List;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFruitCocoa;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;


public class FeatureGenFruitPod implements IPostGenFeature, IPostGrowFeature {

    BlockCocoa fruitPod;
    int allowedSize;

    public FeatureGenFruitPod (BlockCocoa fruitPod, int size){
        this.fruitPod = fruitPod;
        allowedSize = Math.max(size, 1);
    }

    private int getFruitChance (World world, BlockPos pos, boolean worldgen){
        if (!worldgen){
            float factor = SeasonHelper.globalSeasonalFruitProductionFactor(world, pos);
            if (factor <= 0) return -1;
            return (int)(16 / factor);
        } else return 4;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if(world.rand.nextInt() % getFruitChance(world, rootPos, false) == 0) {
            addCocoa(world, rootPos, getLeavesHeight(rootPos, world).down(world.rand.nextInt(allowedSize)), false);
        }
        return false;
    }

    private BlockPos getLeavesHeight (BlockPos rootPos, World world){
        for (int y= 1; y < 20; y++){
            BlockPos testPos = rootPos.up(y);
            if ((world.getBlockState(testPos).getBlock() instanceof BlockLeaves)){
                return testPos.down();
            }
        }
        return rootPos;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        boolean placed = false;
        for (int i=0;i<8;i++){
            if(world.rand.nextInt() % getFruitChance(world, rootPos,true) == 0) {
                addCocoa(world, rootPos, getLeavesHeight(rootPos, world).down(world.rand.nextInt(allowedSize)),true);
                placed = true;
            }
        }
        return placed;
    }

    private void addCocoa(World world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()){
            return;
        }
        EnumFacing placeDir = EnumFacing.HORIZONTALS[world.rand.nextInt(4)];
        if (world.isAirBlock(leavesPos.offset(placeDir))){
            world.setBlockState(leavesPos.offset(placeDir), fruitPod.getDefaultState().withProperty(BlockCocoa.FACING, placeDir.getOpposite()).withProperty(BlockCocoa.AGE, worldGen?(world.rand.nextInt(3)):0));
        }
    }

}