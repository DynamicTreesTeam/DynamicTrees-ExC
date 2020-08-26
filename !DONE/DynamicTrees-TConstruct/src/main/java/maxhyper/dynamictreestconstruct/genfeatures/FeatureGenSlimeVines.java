package maxhyper.dynamictreestconstruct.genfeatures;

import java.util.List;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class FeatureGenSlimeVines implements IPostGenFeature {

    protected final Block vineState;

    public FeatureGenSlimeVines(Block vine) {
        this.vineState = vine;
    }

    public int getQuantity(boolean worldGen) {
        return worldGen ? 20 : 1;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {

        if(!endPoints.isEmpty()) {
            int qty = getQuantity(true);
            for(int i = 0; i < qty; i++) {
                BlockPos endPoint = endPoints.get(world.rand.nextInt(endPoints.size()));
                BlockPos fruitPos = CoordUtils.getRayTraceFruitPos(world, species, rootPos.up(), endPoint, safeBounds);
                world.setBlockState(fruitPos, vineState.getStateFromMeta(1 + world.rand.nextInt(14)));
            }
            return true;
        }
        return false;
    }

}