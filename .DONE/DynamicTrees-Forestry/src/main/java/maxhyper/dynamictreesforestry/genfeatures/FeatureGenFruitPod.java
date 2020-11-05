package maxhyper.dynamictreesforestry.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesforestry.blocks.BlockFruitDate;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;


public class FeatureGenFruitPod implements IPostGenFeature, IPostGrowFeature {

    BlockFruitDate fruitPod;
    int allowedSize;
    protected int fruitingRadius = 6;
    protected int frondHeight = 20;

    public FeatureGenFruitPod (BlockFruitDate fruitPod, int size){
        this.fruitPod = fruitPod;
        allowedSize = Math.max(size, 1);
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if((TreeHelper.getRadius(world, rootPos.up()) >= fruitingRadius) && natural && world.rand.nextInt() % 16 == 0) {
            if(species.seasonalFruitProductionFactor(world, rootPos) > world.rand.nextFloat()) {
                addFruit(world, rootPos, getLeavesHeight(rootPos, world).down(world.rand.nextInt(allowedSize)), false);
            }
        }
        return false;
    }

    private BlockPos getLeavesHeight (BlockPos rootPos, World world){
        for (int y= 1; y < frondHeight; y++){
            BlockPos testPos = rootPos.up(y);
            if ((world.getBlockState(testPos).getBlock() instanceof BlockLeaves)){
                return testPos;
            }
        }
        return rootPos;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        boolean placed = false;
        int qty = 8;
        qty *= species.seasonalFruitProductionFactor(world, rootPos);
        for (int i=0;i<qty;i++){
            if(world.rand.nextInt(4) == 0) {
                addFruit(world, rootPos, getLeavesHeight(rootPos, world).down(world.rand.nextInt(allowedSize)),true);
                placed = true;
            }
        }
        return placed;
    }

    private void addFruit(World world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()){
            return;
        }
        EnumFacing placeDir = EnumFacing.HORIZONTALS[world.rand.nextInt(4)];
        leavesPos = leavesPos.down(); //we move the pos down so the fruit can stick to the trunk
        if (world.isAirBlock(leavesPos.offset(placeDir))){
            world.setBlockState(leavesPos.offset(placeDir), fruitPod.getDefaultState().withProperty(BlockFruitDate.FACING, placeDir.getOpposite()).withProperty(BlockFruitDate.AGE, worldGen?(1+world.rand.nextInt(2)):0));
        }
    }

}