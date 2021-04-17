package maxhyper.dynamictreestbl.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class FeatureGenHangerVine implements IPostGenFeature {

    protected int qty = 8;
    protected int maxLength = 8;
    protected float verSpread = 30;
    protected float rayDistance = 5;
    protected IBlockState vineState;

    public FeatureGenHangerVine (IBlockState vine){
        vineState = vine;
    }

    public FeatureGenHangerVine setQuantity(int qty) {
        this.qty = qty;
        return this;
    }

    public FeatureGenHangerVine setMaxLength(int length) {
        this.maxLength = length;
        return this;
    }

    public FeatureGenHangerVine setVerSpread(float verSpread) {
        this.verSpread = verSpread;
        return this;
    }

    public FeatureGenHangerVine setRayDistance(float rayDistance) {
        this.rayDistance = rayDistance;
        return this;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        if(safeBounds != SafeChunkBounds.ANY) {//worldgen
            if(!endPoints.isEmpty()) {
                for(int i = 0; i < qty; i++) {
                    BlockPos endPoint = endPoints.get(world.rand.nextInt(endPoints.size()));
                    addVine(world, species, rootPos, endPoint, safeBounds);
                }
                return true;
            }
        }
        return false;
    }

    protected void addVine(World world, Species species, BlockPos rootPos, BlockPos branchPos, SafeChunkBounds safeBounds) {

        BlockPos vinePos = CoordUtils.getRayTraceFruitPos(world, species, rootPos, branchPos, safeBounds);

        if(vinePos != BlockPos.ORIGIN && safeBounds.inBounds(vinePos, true)) {
            int len = MathHelper.clamp(world.rand.nextInt(maxLength) + 3, 3, maxLength);
            MutableBlockPos mPos = new MutableBlockPos(vinePos);
            for(int i = 0; i < len; i++) {
                if(world.isAirBlock(mPos)) {
                    world.setBlockState(mPos, vineState);
                    mPos.setY(mPos.getY() - 1);
                } else {
                    break;
                }

            }
        }
    }
}
