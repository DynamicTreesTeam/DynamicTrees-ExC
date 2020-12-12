package maxhyper.dynamictreesdefiledlands.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import lykrast.defiledlands.common.init.ModBiomes;
import maxhyper.dynamictreesdefiledlands.DynamicTreesDefiledLands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator (){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesDefiledLands.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        jsonPopulator.populate(dbase);
    }
}

