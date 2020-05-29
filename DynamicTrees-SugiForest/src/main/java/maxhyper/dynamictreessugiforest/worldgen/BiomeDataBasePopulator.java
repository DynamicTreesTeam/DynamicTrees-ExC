package maxhyper.dynamictreessugiforest.worldgen;

import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;

import sugiforest.world.SugiBiomes;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species oak;
    private static Species sugi;

    public void populate(BiomeDataBase dbase) {

        dbase.setCancelVanillaTreeGen(SugiBiomes.SUGI_FOREST, true);
    }
}

