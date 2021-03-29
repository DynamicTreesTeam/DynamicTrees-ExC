package maxhyper.dynamictreescuisine.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import maxhyper.dynamictreescuisine.DynamicTreesCuisine;
import net.minecraft.util.ResourceLocation;
import snownee.cuisine.CuisineConfig;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator (){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesCuisine.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        CuisineConfig.WORLD_GEN.fruitTreesGenRate = 0;
        jsonPopulator.populate(dbase);
    }
}


