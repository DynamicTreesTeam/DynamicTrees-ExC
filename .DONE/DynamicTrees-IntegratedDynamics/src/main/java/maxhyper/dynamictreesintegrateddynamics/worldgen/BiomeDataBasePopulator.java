package maxhyper.dynamictreesintegrateddynamics.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import maxhyper.dynamictreesintegrateddynamics.ModConfigs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import maxhyper.dynamictreesintegrateddynamics.DynamicTreesIntegratedDynamics;

import org.cyclops.integrateddynamics.world.biome.BiomeMeneglin;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator (){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        Species menril = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, "menril"));

        if (ModConfigs.menrilTreeOccurance > 0){
            Biome.REGISTRY.forEach(biome -> {
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {

                    RandomSpeciesSelector selector = new RandomSpeciesSelector().add((int)(1/ModConfigs.menrilTreeOccurance)).
                            add(menril, 1);
                    dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

                }
            });
        }

        jsonPopulator.populate(dbase);
    }
}

