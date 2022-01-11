package maxhyper.dtneapolitan.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import maxhyper.dtneapolitan.DynamicTreesNeapolitan;
import net.minecraft.util.ResourceLocation;

public class DTNeapolitanGenFeatures {

    public static GenFeature PALM_FRUIT_FEATURE = new BananaFruitGenFeature(new ResourceLocation(DynamicTreesNeapolitan.MOD_ID,"banana_fruit"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(PALM_FRUIT_FEATURE);
    }

}
