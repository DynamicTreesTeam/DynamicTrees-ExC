package maxhyper.dynamictreesic2.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;
import maxhyper.dynamictreesic2.DynamicTreesIC2;

import ic2.core.init.MainConfig;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public void populate(BiomeDataBase dbase) {
        Species rubber = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, "rubberIC"));

        Biome.REGISTRY.forEach(biome -> {
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(25).add(rubber, 4);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {
                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(50).add(rubber, 1);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(100).add(rubber, 1);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS)) {
                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(70).add(rubber, 1);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            } else return;

            DynamicTreesIC2.logger.info("Added Rubber species to biome database for " + biome.getBiomeName() + ".");
        });
    }

}

