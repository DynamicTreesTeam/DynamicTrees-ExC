package maxhyper.dynamictreespamtrees.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import maxhyper.dynamictreespamtrees.DynamicTreesPamTrees;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.Loader;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public void populate(BiomeDataBase dbase) {

        if (Loader.isModLoaded(DynamicTreesPamTrees.SPOOKYTREE_MOD)){
            Species spooky = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPamTrees.MODID, "spooky"));
            Species spookybig = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPamTrees.MODID, "megaspooky"));
            Biome.REGISTRY.forEach(biome -> {
                if (BiomeDictionary.hasType(biome, Type.FOREST) && !BiomeDictionary.hasType(biome, Type.CONIFEROUS) && !BiomeDictionary.hasType(biome, Type.SPOOKY)){
                    RandomSpeciesSelector selector = new RandomSpeciesSelector().add(800).add(spooky, 1);
                    dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
                }
                if (BiomeDictionary.hasType(biome, Type.FOREST) && BiomeDictionary.hasType(biome, Type.SPOOKY)){
                    RandomSpeciesSelector selector = new RandomSpeciesSelector().add(300).add(spookybig, 1);
                    dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
                }
            });
        }

        if (Loader.isModLoaded(DynamicTreesPamTrees.REDBUDTREE_MOD)){
            Species redbud = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPamTrees.MODID, "redbud"));
            Biome.REGISTRY.forEach(biome -> {
                if (BiomeDictionary.hasType(biome, Type.FOREST) && !BiomeDictionary.hasType(biome, Type.CONIFEROUS) && !BiomeDictionary.hasType(biome, Type.SPOOKY)){
                    RandomSpeciesSelector selector = new RandomSpeciesSelector().add(300).add(redbud, 1);
                    dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
                }
            });
        }

    }
}

