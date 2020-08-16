package maxhyper.dynamictreesnatura.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;

import com.progwml6.natura.common.config.Config;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species maple, silverbell, amaranth, tigerwood, willow, eucalyptus, hopseed, sakura,
        ghostwood, bloodwood, fusewood, darkwood, saguaro;

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
        saguaro = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "saguaro"));
    }

    public void populate(BiomeDataBase dbase) {
        createStaticAliases();
        Biome.REGISTRY.forEach(biome -> {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(1000).
                        add(sakura,     ModContent.generateSakura     ? 1 + (100/Config.sakuraSpawnRarity) / 5 : 0).
                        add(eucalyptus, ModContent.generateEucalyptus ? 100/Config.eucalyptusSpawnRarity       : 0).
                        add(maple,      ModContent.generateMaple      ? 100/Config.mapleRarity                 : 0).
                        add(silverbell, ModContent.generateSilverbell ? 100/Config.silverbellRarity            : 0).
                        add(tigerwood,  ModContent.generateTiger      ? 100/Config.tigerRarity                 : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(eucalyptus, ModContent.generateEucalyptus ? 1 + (int)((100/Config.eucalyptusSpawnRarity) / 1.5) : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.HILLS)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500).
                        add(hopseed,    ModContent.generateHopseed    ? 100/Config.hopseedSpawnRarity           : 0).
                        add(eucalyptus, ModContent.generateEucalyptus ? 10 * (100/Config.eucalyptusSpawnRarity) : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500).
                        add(sakura, ModContent.generateSakura ? 100/Config.hopseedSpawnRarity    : 0).
                        add(willow, ModContent.generateWillow ? 100/Config.eucalyptusSpawnRarity : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(amaranth, ModContent.generateAmaranth ? 100/Config.amaranthRarity : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500).
                        add(willow, ModContent.generateWillow ? 100/Config.willowRarity : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(ghostwood, ModContent.generateGhostwood ? 100/Config.ghostwoodSpawnRarity : 0).
                        add(fusewood,  ModContent.generateFusewood  ? 100/Config.fusewoodSpawnRarity  : 0).
                        add(darkwood,  ModContent.generateDarkwood  ? 100/Config.darkwoodSpawnRarity  : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
                dbase.setDensitySelector(biome, (rand, noiseDensity) -> ((noiseDensity * 0.25) + 0.75) * 0.5, Operation.REPLACE);
            }
            else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)) {

                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(20).
                        add(saguaro, ModContent.generateSaguaro ? (10/Config.saguaroSpawnRarity) : 0);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

            }

        });
    }

}

