package maxhyper.dynamictreesabyssalcraft.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.shinoow.abyssalcraft.api.biome.ACBiomes;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public void populate(BiomeDataBase dbase) {

        dbase.setCancelVanillaTreeGen(ACBiomes.darklands_forest, true);
        dbase.setCancelVanillaTreeGen(ACBiomes.dreadlands_forest, true);

    }
}

