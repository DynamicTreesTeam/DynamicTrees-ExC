package maxhyper.dynamictreesquark.worldgen;

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
import maxhyper.dynamictreesquark.DynamicTreesQuark;
import vazkii.quark.world.feature.TreeVariants;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species swampOak, blossoming;

    public void populate(BiomeDataBase dbase) {

            blossoming = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesQuark.MODID, "blossoming"));
            swampOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesQuark.MODID, "swampOak"));

            Biome.REGISTRY.forEach(biome -> {
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                    RandomSpeciesSelector selector = new RandomSpeciesSelector().add(10).
                            add(blossoming, 2);
                    dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

                } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                    RandomSpeciesSelector selector = new RandomSpeciesSelector().add(10).
                            add(swampOak, 5);

                    dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

                }
            });
    }
}

