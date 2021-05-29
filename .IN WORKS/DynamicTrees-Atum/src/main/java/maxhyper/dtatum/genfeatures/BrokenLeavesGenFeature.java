package maxhyper.dtatum.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.GenFeatureProperty;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class BrokenLeavesGenFeature extends GenFeature implements IPostGenFeature {

    public BrokenLeavesGenFeature(ResourceLocation registryName, GenFeatureProperty<?>... properties) {
        super(registryName, properties);
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        if (!endPoints.isEmpty()){
            BlockPos tip = endPoints.get(0).above(2);
            if (!safeBounds.inBounds(tip, true)) return false;
            if (world.getBlockState(tip).getBlock() instanceof DynamicLeavesBlock){
                for (CoordUtils.Surround surr : CoordUtils.Surround.values()){
                    BlockPos leafPos = tip.offset(surr.getOffset());
                    if (world.getBlockState(leafPos).getBlock() instanceof DynamicLeavesBlock)
                        if (world.getRandom().nextFloat() > 0.5f)
                            world.setBlock(leafPos, Blocks.AIR.defaultBlockState(), 2);
                }
                return true;
            }
        }
        return false;
    }
}
