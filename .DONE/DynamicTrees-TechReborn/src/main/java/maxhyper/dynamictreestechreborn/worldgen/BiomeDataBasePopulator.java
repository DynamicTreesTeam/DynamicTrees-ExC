package maxhyper.dynamictreestechreborn.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import maxhyper.dynamictreestechreborn.DynamicTreesTechReborn;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import techreborn.Core;

import java.util.Arrays;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species rubber;

    public void populate(BiomeDataBase dbase) {
        rubber = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTechReborn.MODID, "rubber"));

        int chance = Core.worldGen.config.rubberTreeConfig.chance;

        Biome.REGISTRY.forEach(biome -> {
            if (biome.getRegistryName() != null && Arrays.asList(Core.worldGen.config.rubberTreeConfig.rubberTreeBiomeBlacklist).contains(biome.getRegistryName().toString())) {
                return;
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(chance - 20).add(rubber, 1);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(chance - 8).add(rubber, 1);
                dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
            }
        });
    }
}

