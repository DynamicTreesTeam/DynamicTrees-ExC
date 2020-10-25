package maxhyper.dynamictreessugiforest.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;

import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import maxhyper.dynamictreessugiforest.DynamicTreesSugiForest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import sugiforest.world.SugiBiomes;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator (){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesSugiForest.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        jsonPopulator.populate(dbase);
    }
}

