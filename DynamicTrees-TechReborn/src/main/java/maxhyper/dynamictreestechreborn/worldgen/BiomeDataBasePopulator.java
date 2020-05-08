package maxhyper.dynamictreestechreborn.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import net.minecraft.util.ResourceLocation;
import techreborn.Core;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species rubber;

    public void populate(BiomeDataBase dbase) {

        Core.worldGen.config.rubberTreeConfig.shouldSpawn = false;
    }
}

