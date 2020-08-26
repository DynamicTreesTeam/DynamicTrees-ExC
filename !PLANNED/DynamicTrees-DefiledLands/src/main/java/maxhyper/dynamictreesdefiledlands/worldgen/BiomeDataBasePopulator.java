package maxhyper.dynamictreesdefiledlands.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import lykrast.defiledlands.common.init.ModBiomes;
import lykrast.defiledlands.core.DefiledLands;
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

    public void populate(BiomeDataBase dbase) {

        for(Biome biome : defiledBiomes){
            dbase.setCancelVanillaTreeGen(biome, true);
        }

    }
}

