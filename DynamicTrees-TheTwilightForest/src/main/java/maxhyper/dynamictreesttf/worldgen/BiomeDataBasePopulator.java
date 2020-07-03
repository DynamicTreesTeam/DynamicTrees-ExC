package maxhyper.dynamictreesttf.worldgen;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;

import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import twilightforest.TFConfig;
import twilightforest.biomes.TFBiomeDecorator;
import twilightforest.biomes.TFBiomes;

import java.util.ArrayList;
import java.util.List;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species canopy, deadCanopy, darkwood, mangrove, rainbowOak, smallRobustOak, sicklyOak, oak, birch, spruce, spruceBig, spruceHuge;
    public static Biome[] TwilightBiomes = {TFBiomes.clearing, TFBiomes.darkForest, TFBiomes.darkForestCenter, TFBiomes.deepMushrooms, TFBiomes.denseTwilightForest,
            TFBiomes.enchantedForest, TFBiomes.fireflyForest, TFBiomes.fireSwamp, TFBiomes.glacier, TFBiomes.highlands, TFBiomes.highlandsCenter,
            TFBiomes.mushrooms, TFBiomes.oakSavanna, TFBiomes.snowy_forest, TFBiomes.spookyForest, TFBiomes.stream, TFBiomes.tfLake, TFBiomes.tfSwamp,
            TFBiomes.thornlands, TFBiomes.twilightForest};

    public void populate(BiomeDataBase dbase) {
        oak = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));
        birch = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch"));
        canopy = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "canopy"));
        smallRobustOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "robusttwilightoak"));

        ModConfigs.dimensionBlacklist.remove(TFConfig.dimension.dimensionID);

        dbase.setSpeciesSelector(TFBiomes.twilightForest, new BiomePropertySelectors.RandomSpeciesSelector()
                .add(canopy, 8).
                        add(oak, 3).
                        add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.twilightForest, (rand, noiseDensity) -> noiseDensity * 1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.twilightForest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.oakSavanna, new BiomePropertySelectors.RandomSpeciesSelector()
                .add(smallRobustOak, 1).
                        add(oak, 5), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.oakSavanna, (rand, noiseDensity) -> noiseDensity * 0.1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.oakSavanna, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

    }
}
