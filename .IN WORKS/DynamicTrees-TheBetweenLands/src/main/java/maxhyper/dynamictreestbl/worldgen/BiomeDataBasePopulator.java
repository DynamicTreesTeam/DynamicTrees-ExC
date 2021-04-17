package maxhyper.dynamictreestbl.worldgen;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import net.minecraft.util.ResourceLocation;
import thebetweenlands.common.config.BetweenlandsConfig;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator(){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesTBL.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        ModConfigs.dimensionBlacklist.remove(BetweenlandsConfig.WORLD_AND_DIMENSION.dimensionId);
        jsonPopulator.populate(dbase);
    }
}
