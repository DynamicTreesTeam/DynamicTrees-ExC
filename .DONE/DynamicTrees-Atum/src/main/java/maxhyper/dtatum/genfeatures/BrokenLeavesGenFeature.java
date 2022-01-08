package maxhyper.dtatum.genfeatures;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BrokenLeavesGenFeature extends GenFeature {

    public static final ConfigurationProperty<Float> PERCENTAGE =
            ConfigurationProperty.property("percentage", Float.class);

    public BrokenLeavesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(PERCENTAGE);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(PERCENTAGE, 0.3f);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        if (!context.endPoints().isEmpty()) {
            BlockPos tip = context.endPoints().get(0).above(2);
            if (!context.bounds().inBounds(tip, true)) {
                return false;
            }
            IWorld world = context.world();
            if (world.getBlockState(tip).getBlock() instanceof DynamicLeavesBlock) {
                for (CoordUtils.Surround surr : CoordUtils.Surround.values()) {
                    BlockPos leafPos = tip.offset(surr.getOffset());
                    if (world.getBlockState(leafPos).getBlock() instanceof DynamicLeavesBlock &&
                            world.getRandom().nextFloat() > configuration.get(PERCENTAGE)) {
                        world.setBlock(leafPos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
                return true;
            }
        }
        return false;
    }

}
