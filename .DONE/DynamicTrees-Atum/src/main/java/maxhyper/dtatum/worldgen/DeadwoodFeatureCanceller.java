package maxhyper.dtatum.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.teammetallurgy.atum.world.gen.feature.DeadwoodFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class DeadwoodFeatureCanceller<T extends IFeatureConfig> extends FeatureCanceller {

    public DeadwoodFeatureCanceller(ResourceLocation registryName) {
        super(registryName);
    }

    //builder.addFeature(Decoration.VEGETAL_DECORATION,
    // AtumFeatures.DEADWOOD_FEATURE
    // .configured(NoFeatureConfig.INSTANCE)
    // .decorated(Placements.HEIGHTMAP_SQUARE)
    // .decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(count, extraChance, extraCount))));

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.FeatureCancellations featureCancellations) {
        IFeatureConfig featureConfig = configuredFeature.config;

        if (!(featureConfig instanceof DecoratedFeatureConfig))
            return false;
        featureConfig = ((DecoratedFeatureConfig) featureConfig).feature.get().config;

        return featureConfig instanceof DecoratedFeatureConfig
                && ((DecoratedFeatureConfig) featureConfig).feature.get().feature instanceof DeadwoodFeature;
    }
}
