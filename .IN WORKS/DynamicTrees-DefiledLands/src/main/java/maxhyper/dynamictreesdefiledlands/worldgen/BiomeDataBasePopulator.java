package maxhyper.dynamictreesdefiledlands.worldgen;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import lykrast.defiledlands.common.init.ModBiomes;
import lykrast.defiledlands.core.DefiledLands;
import maxhyper.dynamictreesdefiledlands.DynamicTreesDefiledLands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    Biome[] defiledBiomes = {
            ModBiomes.desertDefiled,
            ModBiomes.forestTenebra,
            ModBiomes.forestVilespine,
            ModBiomes.hillsDefiled,
            ModBiomes.icePlainsDefiled,
            ModBiomes.plainsDefiled,
            ModBiomes.swampDefiled
    };

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator (){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesDefiledLands.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        for (Biome biome : defiledBiomes){
            biome.decorator.treesPerChunk = 0;
        }
        jsonPopulator.populate(dbase);
    }
}

