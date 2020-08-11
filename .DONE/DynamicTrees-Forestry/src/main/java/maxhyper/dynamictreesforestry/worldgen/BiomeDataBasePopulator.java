package maxhyper.dynamictreesforestry.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import forestry.arboriculture.TreeConfig;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;


public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species balsa, baobab, bullPine, cherry, chestnut, coastSequoia, cocobolo, desertAcacia, ebony,
            ipe, kapok, larch, lemon, mahoe, maple, meranti, padauk, palm, papaya, plum, poplar, silverLime, sipiri,
            teak, walnut, wenge, willow, zebrawood;

    private static void createStaticAliases() {
        balsa = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.BALSA));
        baobab = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.BAOBAB));
        bullPine = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.BULLPINE));
        cherry = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.CHERRY));
        cocobolo = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.COCOBOLO));
        desertAcacia = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.DESERTACACIA));
        ebony = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.EBONY));
        larch = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.LARCH));
        maple = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.MAPLE));
        meranti = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.MAHOGANY));
        padauk = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PADAUK));
        palm = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.DATEPALM));
        papaya = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PAPAYA));
        plum = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PLUM));
        silverLime = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.SILVERLIME));
        sipiri = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.GREENHEART));
        teak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.TEAK));
        wenge = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.WENGE));
        willow = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.WILLOW));
        zebrawood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.ZEBRAWOOD));
    }

    public void populate(BiomeDataBase dbase) {
        createStaticAliases();
        TreeConfig.blacklistTreeDim(null, 0); //Disables forestry trees from spawning in the overworld
        float globalRarity = TreeConfig.getSpawnRarity(null);
        if (globalRarity <= 0.0F) {
            return;
        }
        Biome.REGISTRY.forEach(biome -> {
            if (biome == Biomes.RIVER){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().
                        add(palm, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.DATEPALM)) * globalRarity));
                dbase.setSpeciesSelector(biome, selector, Operation.REPLACE);
                dbase.setChanceSelector(biome, (rand, species, radius) -> rand.nextFloat() < 0.2f ? BiomePropertySelectors.EnumChance.OK : BiomePropertySelectors.EnumChance.CANCEL, Operation.REPLACE);
                dbase.setDensitySelector(biome, (rand, noiseDensity) -> noiseDensity * 0.01, Operation.REPLACE);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(willow, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.WILLOW)) * globalRarity));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(plum, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.PLUM)) * globalRarity)).
                        add(palm, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.DATEPALM)) * globalRarity)).
                        add(teak, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.TEAK)) * globalRarity)).
                        add(balsa, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.BALSA)) * globalRarity)).
                        add(meranti, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.MAHOGANY)) * globalRarity)).
                        add(papaya, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.PAPAYA)) * globalRarity)).
                        add(zebrawood, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.ZEBRAWOOD)) * globalRarity)).
                        add(sipiri, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.GREENHEART)) * globalRarity)).
                        add(ebony, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.EBONY)) * globalRarity)).
                        add(cocobolo, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.COCOBOLO)) * globalRarity)).
                        add(wenge, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.WENGE)) * globalRarity)).
                        add(padauk, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.PADAUK)) * globalRarity));//.
                        //add(mahoe, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.MAHOE)) * globalRarity));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(bullPine, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.BULLPINE)) * globalRarity)).
                        add(larch, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.LARCH)) * globalRarity));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(cherry, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.CHERRY)) * globalRarity)).
                        add(maple, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.MAPLE)) * globalRarity)).
                        add(silverLime, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.SILVERLIME)) * globalRarity));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(desertAcacia, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.DESERTACACIA)) * globalRarity)).
                        add(padauk, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.PADAUK)) * globalRarity));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500). //We use 500 instead of 100 to make baobab extra rare
                        add(baobab, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.BAOBAB)) * globalRarity)).
                        add(ebony, (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(ModConstants.EBONY)) * globalRarity));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }

        });

    }

}

