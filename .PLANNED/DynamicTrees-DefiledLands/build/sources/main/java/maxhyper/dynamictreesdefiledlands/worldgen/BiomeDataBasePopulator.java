package maxhyper.dynamictreesdefiledlands.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import lykrast.defiledlands.common.init.ModBiomes;
import lykrast.defiledlands.core.DefiledLands;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public void populate(BiomeDataBase dbase) {

        dbase.setCancelVanillaTreeGen(ModBiomes.plainsDefiled, true);

    }
}

