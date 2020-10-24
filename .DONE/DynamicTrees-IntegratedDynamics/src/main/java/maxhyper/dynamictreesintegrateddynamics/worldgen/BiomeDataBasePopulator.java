package maxhyper.dynamictreesintegrateddynamics.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import maxhyper.dynamictreesintegrateddynamics.DynamicTreesIntegratedDynamics;
import net.minecraft.util.ResourceLocation;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator (){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        jsonPopulator.populate(dbase);
    }
}

