package maxhyper.dynamictreesnatura.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.EnumChance;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;

import com.progwml6.natura.common.config.Config;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species maple, silverbell, amaranth, tigerwood, willow, eucalyptus, hopseed, sakura,
        ghostwood, bloodwood, fusewood, darkwood;

    private static void createStaticAliases() {
        maple = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "maple"));
        silverbell = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "silverbell"));
        amaranth = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "amaranth"));
        tigerwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "tigerwood"));
        hopseed = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "hopseed"));
        willow = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "willow"));
        sakura = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "sakura"));
        eucalyptus = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "eucalyptus"));
        ghostwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "ghostwood"));
        bloodwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "bloodwood"));
        fusewood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "fusewood"));
        darkwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "darkwood"));
    }

    public void populate(BiomeDataBase dbase) {
        createStaticAliases();
        Biome.REGISTRY.forEach(biome -> {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(2000).
                        add(sakura, 1 + Config.sakuraSpawnRarity / 5).
                        add(eucalyptus, Config.eucalyptusSpawnRarity).
                        add(maple, Config.mapleRarity).
                        add(silverbell, Config.silverbellRarity).
                        add(tigerwood, Config.tigerRarity);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(eucalyptus, 1 + (int)(Config.eucalyptusSpawnRarity / 1.5));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.HILLS)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(hopseed, Config.hopseedSpawnRarity).
                        add(eucalyptus, 10 * Config.eucalyptusSpawnRarity);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500).
                        add(sakura, Config.hopseedSpawnRarity).
                        add(willow, Config.eucalyptusSpawnRarity);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500).
                        add(amaranth, Config.amaranthRarity);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500).
                        add(willow, Config.willowRarity);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(ghostwood, Config.ghostwoodSpawnRarity).
                        add(fusewood, Config.fusewoodSpawnRarity).
                        add(darkwood, Config.darkwoodSpawnRarity);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
                dbase.setDensitySelector(biome, (rand, noiseDensity) -> ((noiseDensity * 0.25) + 0.75) * 0.5, Operation.REPLACE);
            }
//            else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)) {
//
//                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(640).
//                        add(saguaro, (10/Config.saguaroSpawnRarity));
//                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
//
//            }

        });
    }

}

