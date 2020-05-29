package maxhyper.dynamictreesic2.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;
import maxhyper.dynamictreesic2.DynamicTreesIC2;

import ic2.core.init.MainConfig;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species oak;
    private static Species rubber;

    public void populate(BiomeDataBase dbase) {

        MainConfig.get().set("worldgen/rubberTree", false);

    }
}

