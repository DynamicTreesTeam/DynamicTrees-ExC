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
            ipe, kapok, larch, lemon, mahoe, maple, mahogany, padauk, palm, papaya, plum, poplar, silverLime, greenheart,
            teak, walnut, wenge, willow, zebrawood;

    private float globalRarity;

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
        mahogany = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.MAHOGANY));
        padauk = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PADAUK));
        palm = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.DATEPALM));
        papaya = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PAPAYA));
        plum = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PLUM));
        silverLime = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.SILVERLIME));
        greenheart = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.GREENHEART));
        teak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.TEAK));
        wenge = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.WENGE));
        willow = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.WILLOW));
        zebrawood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.ZEBRAWOOD));
    }

    public void populate(BiomeDataBase dbase) {
        createStaticAliases();
        TreeConfig.blacklistTreeDim(null, 0); //Disables forestry trees from spawning in the overworld
        globalRarity = TreeConfig.getSpawnRarity(null);
        if (globalRarity <= 0.0F) {
            return;
        }
        Biome.REGISTRY.forEach(biome -> {
            if (biome == Biomes.RIVER){
                //There has to be an extra check since PALM trees are the only ones that spawn in rivers
                if (TreeConfig.isValidBiome(ModContent.getTreeUIDfromID(ModConstants.DATEPALM), biome)){
                    RandomSpeciesSelector selector = new RandomSpeciesSelector().
                            add(palm, getSpawnWeight(ModConstants.DATEPALM, biome));
                    dbase.setSpeciesSelector(biome, selector, Operation.REPLACE);
                    dbase.setChanceSelector(biome, (rand, species, radius) -> rand.nextFloat() < 0.1f ? BiomePropertySelectors.EnumChance.OK : BiomePropertySelectors.EnumChance.CANCEL, Operation.REPLACE);
                    dbase.setDensitySelector(biome, (rand, noiseDensity) -> noiseDensity * 0.01, Operation.REPLACE);
                }
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(willow, getSpawnWeight(ModConstants.WILLOW, biome));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(plum, getSpawnWeight(ModConstants.PLUM, biome)).
                        add(palm, getSpawnWeight(ModConstants.DATEPALM, biome)).
                        add(teak, getSpawnWeight(ModConstants.TEAK, biome)).
                        add(balsa, getSpawnWeight(ModConstants.BALSA, biome)).
                        add(mahogany, getSpawnWeight(ModConstants.MAHOGANY, biome)).
                        add(papaya, getSpawnWeight(ModConstants.PAPAYA, biome)).
                        add(zebrawood, getSpawnWeight(ModConstants.ZEBRAWOOD, biome)).
                        add(greenheart, getSpawnWeight(ModConstants.GREENHEART, biome)).
                        add(ebony, getSpawnWeight(ModConstants.EBONY, biome)).
                        add(cocobolo, getSpawnWeight(ModConstants.COCOBOLO, biome)).
                        add(wenge, getSpawnWeight(ModConstants.WENGE, biome)).
                        add(padauk, getSpawnWeight(ModConstants.PADAUK, biome));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(bullPine, getSpawnWeight(ModConstants.BULLPINE, biome)).
                        add(larch, getSpawnWeight(ModConstants.LARCH, biome));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(cherry, getSpawnWeight(ModConstants.CHERRY, biome)).
                        add(maple, getSpawnWeight(ModConstants.MAPLE, biome)).
                        add(silverLime, getSpawnWeight(ModConstants.SILVERLIME, biome));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(100).
                        add(desertAcacia, getSpawnWeight(ModConstants.DESERTACACIA, biome)).
                        add(padauk, getSpawnWeight(ModConstants.PADAUK, biome));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD)){
                RandomSpeciesSelector selector = new RandomSpeciesSelector().add(500). //We use 500 instead of 100 to make baobab extra rare
                        add(baobab, getSpawnWeight(ModConstants.BAOBAB, biome)).
                        add(ebony, getSpawnWeight(ModConstants.EBONY, biome));
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }

        });

    }

    protected int getSpawnWeight (String tree, Biome biome){
        if (TreeConfig.isValidBiome(ModContent.getTreeUIDfromID(tree), biome)){
            return (int)Math.ceil(10 * TreeConfig.getSpawnRarity(ModContent.getTreeUIDfromID(tree)) * globalRarity);
        }
        return 0;
    }

}

