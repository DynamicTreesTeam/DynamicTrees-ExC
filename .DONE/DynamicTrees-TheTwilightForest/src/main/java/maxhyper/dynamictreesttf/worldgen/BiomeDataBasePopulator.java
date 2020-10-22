package maxhyper.dynamictreesttf.worldgen;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;

import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import twilightforest.TFConfig;
import twilightforest.biomes.TFBiomeSpookyForest;
import twilightforest.biomes.TFBiomes;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species canopy, spookyCanopy, spookyOak, darkwood, mangrove, rainbowOak, smallRobustOak, sicklyOak, oak, birch, spruce, spruceBig, spruceHuge,
    mushroomred, mushroombrown;
    public static Biome[] TwilightBiomes = {
            TFBiomes.clearing,              //no trees
            TFBiomes.darkForest,            //done
            TFBiomes.darkForestCenter,      //done
            TFBiomes.deepMushrooms,         //done
            TFBiomes.denseTwilightForest,   //done
            TFBiomes.enchantedForest,       //done
            TFBiomes.fireflyForest,         //done
            TFBiomes.fireSwamp,             //done
            TFBiomes.glacier,               //no trees
            TFBiomes.highlands,             //done
            TFBiomes.highlandsCenter,       //no trees
            TFBiomes.mushrooms,             //done
            TFBiomes.oakSavanna,            //done
            TFBiomes.snowy_forest,          //done
            TFBiomes.spookyForest,          //done
            TFBiomes.stream,                //no trees
            TFBiomes.tfLake,                //no trees
            TFBiomes.tfSwamp,               //done
            TFBiomes.thornlands,            //no trees
            TFBiomes.twilightForest         //done
    };

    public void populate(BiomeDataBase dbase) {
        oak = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));
        birch = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch"));
        spruce = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce"));
        spruceBig = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "megaspruce"));
        mushroomred = TreeRegistry.findSpeciesSloppy("mushroomred");
        mushroombrown = TreeRegistry.findSpeciesSloppy("mushroombrn");
        canopy = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "canopy"));
        rainbowOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "rainbowOak"));
        smallRobustOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "robusttwilightoak"));
        darkwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "darkwood"));
        spruceHuge = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "hugemegaspruce"));
        spookyCanopy = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "canopyspooky"));
        spookyOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "oakspooky"));

        ModConfigs.dimensionBlacklist.remove(TFConfig.dimension.dimensionID);
        for (Biome biome : TwilightBiomes){
            dbase.setCancelVanillaTreeGen(biome, true);
        }

        dbase.setSpeciesSelector(TFBiomes.darkForest, new BiomePropertySelectors.RandomSpeciesSelector().
                add(darkwood, 4).
                add(oak, 1).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.darkForest, (rand, noiseDensity) -> noiseDensity * 2, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.darkForest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.darkForestCenter, new BiomePropertySelectors.RandomSpeciesSelector().
                add(darkwood, 4).
                add(oak, 1).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.darkForestCenter, (rand, noiseDensity) -> noiseDensity * 2, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.darkForestCenter, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);


        dbase.setSpeciesSelector(TFBiomes.twilightForest, new BiomePropertySelectors.RandomSpeciesSelector().
                add(canopy, 6).
                add(oak, 3).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.twilightForest, (rand, noiseDensity) -> noiseDensity * 1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.twilightForest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.fireflyForest, new BiomePropertySelectors.RandomSpeciesSelector().
                add(canopy, 6).
                add(oak, 3).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.fireflyForest, (rand, noiseDensity) -> noiseDensity * 1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.fireflyForest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.denseTwilightForest, new BiomePropertySelectors.RandomSpeciesSelector().
                add(canopy, 6).
                add(oak, 3).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.denseTwilightForest, (rand, noiseDensity) -> noiseDensity * 2, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.denseTwilightForest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.enchantedForest, new BiomePropertySelectors.RandomSpeciesSelector().
                add(rainbowOak, 1).
                add(canopy, 2).
                add(oak, 2).
                add(birch, 2), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.enchantedForest, (rand, noiseDensity) -> noiseDensity * 1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.enchantedForest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.spookyForest, new BiomePropertySelectors.RandomSpeciesSelector().
                add(spookyCanopy, 4).
                add(spookyOak, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.spookyForest, (rand, noiseDensity) -> noiseDensity * 1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.spookyForest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.mushrooms, new BiomePropertySelectors.RandomSpeciesSelector().
                add(mushroomred, 10).
                add(mushroombrown, 10).
                add(canopy, 2).
                add(oak, 1).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.mushrooms, (rand, noiseDensity) -> noiseDensity * 2, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.mushrooms, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.deepMushrooms, new BiomePropertySelectors.RandomSpeciesSelector().
                add(mushroomred, 6).
                add(mushroombrown, 6).
                add(canopy, 1).
                add(oak, 3).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.deepMushrooms, (rand, noiseDensity) -> noiseDensity * 0.1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.deepMushrooms, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.tfSwamp, new BiomePropertySelectors.RandomSpeciesSelector().
                add(oak, 1), BiomeDataBase.Operation.REPLACE); //Mangroves are generated separately
        dbase.setDensitySelector(TFBiomes.tfSwamp, (rand, noiseDensity) -> noiseDensity * 0.5, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.tfSwamp, (rand, species, radius) -> rand.nextInt(2)==0?BiomePropertySelectors.EnumChance.OK:BiomePropertySelectors.EnumChance.CANCEL, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.fireSwamp, new BiomePropertySelectors.RandomSpeciesSelector().
                add(oak, 1), BiomeDataBase.Operation.REPLACE); //Mangroves are generated separately
        dbase.setDensitySelector(TFBiomes.fireSwamp, (rand, noiseDensity) -> noiseDensity * 0.5, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.fireSwamp, (rand, species, radius) -> rand.nextInt(2)==0?BiomePropertySelectors.EnumChance.OK:BiomePropertySelectors.EnumChance.CANCEL, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.oakSavanna, new BiomePropertySelectors.RandomSpeciesSelector().
                add(smallRobustOak, 1).
                add(oak, 2), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.oakSavanna, (rand, noiseDensity) -> noiseDensity * 0.01, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.oakSavanna, (rand, species, radius) -> rand.nextInt(2)==0?BiomePropertySelectors.EnumChance.OK:BiomePropertySelectors.EnumChance.CANCEL, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.highlands, new BiomePropertySelectors.RandomSpeciesSelector().
                add(spruceBig, 2).
                add(spruce, 3).
                add(birch, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.highlands, (rand, noiseDensity) -> noiseDensity * 1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.highlands, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(TFBiomes.snowy_forest, new BiomePropertySelectors.RandomSpeciesSelector().
                add(spruceHuge, 1).
                add(spruce, 4), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(TFBiomes.snowy_forest, (rand, noiseDensity) -> noiseDensity * 0.8, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(TFBiomes.snowy_forest, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

    }
}
