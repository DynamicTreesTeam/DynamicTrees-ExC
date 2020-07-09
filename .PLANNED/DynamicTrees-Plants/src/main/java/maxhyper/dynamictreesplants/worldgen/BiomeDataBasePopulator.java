package maxhyper.dynamictreesplants.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import shadows.plants2.init.ModRegistry;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public void populate(BiomeDataBase dbase) {

        dbase.setCancelVanillaTreeGen(ModRegistry.CRYSTAL_FOREST, true);

    }
}

