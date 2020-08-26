package maxhyper.dynamictreesplants.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import shadows.plants2.Plants2;
import shadows.plants2.data.PlantConfig;
import shadows.plants2.init.ModRegistry;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species ashen, blackKauri, blazing, brazilianPine, crystal, darkCrystal, incenseCedar, murrayPine;

    private static void createStaticAliases() {
        ashen = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "ashen"));
        blackKauri = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "blackKauri"));
        blazing = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "blazing"));
        brazilianPine = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "brazilianPine"));
        crystal = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "crystal"));
        darkCrystal = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "darkCrystal"));
        incenseCedar = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "incenseCedar"));
        murrayPine = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "murrayPine"));
    }

    public void populate(BiomeDataBase dbase) {
        createStaticAliases();
        dbase.setCancelVanillaTreeGen(ModRegistry.CRYSTAL_FOREST, true);
        PlantConfig.treeGen = false;
        PlantConfig.netherTreeGen = false;

        Biome.REGISTRY.forEach(biome -> {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {

                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().
                        add(ashen, 10).
                        add(blazing, 10);
                dbase.setSpeciesSelector(biome, selector, BiomeDataBase.Operation.SPLICE_BEFORE);

            }
        });

        Biome.REGISTRY.forEach(biome -> {
            if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {

                if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)){
                    BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(100).
                            add(incenseCedar, 1).
                            add(murrayPine, 2);
                    dbase.setSpeciesSelector(biome, selector, BiomeDataBase.Operation.SPLICE_BEFORE);
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
                    BiomePropertySelectors.RandomSpeciesSelector selector2 = new BiomePropertySelectors.RandomSpeciesSelector().add(50).
                            add(brazilianPine, 1);
                    dbase.setSpeciesSelector(biome, selector2, BiomeDataBase.Operation.SPLICE_BEFORE);
                }
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) || biome == Biomes.ROOFED_FOREST || biome == Biomes.MUTATED_ROOFED_FOREST) {
                    BiomePropertySelectors.RandomSpeciesSelector selector3 = new BiomePropertySelectors.RandomSpeciesSelector().add(75).
                            add(blackKauri, 1);
                    dbase.setSpeciesSelector(biome, selector3, BiomeDataBase.Operation.SPLICE_BEFORE);
                }
            }
        });

        dbase.setSpeciesSelector(ModRegistry.CRYSTAL_FOREST, new BiomePropertySelectors.RandomSpeciesSelector().
                add(crystal, 3).
                add(darkCrystal, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(ModRegistry.CRYSTAL_FOREST, (rand, noiseDensity) -> noiseDensity * 1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(ModRegistry.CRYSTAL_FOREST, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);


    }
}

