package maxhyper.dynamictreessugiforest.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;

import maxhyper.dynamictreessugiforest.DynamicTreesSugiForest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import sugiforest.world.SugiBiomes;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species sugi;

    public void populate(BiomeDataBase dbase) {
        sugi = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesSugiForest.MODID, "sugi"));

        Biome.REGISTRY.forEach(biome -> {
            if (biome.isHighHumidity() || biome.getDefaultTemperature() >= 1) {

                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(350).
                        add(sugi, 1);
                dbase.setSpeciesSelector(biome, selector, BiomeDataBase.Operation.SPLICE_BEFORE);

            }
        });
        dbase.setForestness(SugiBiomes.SUGI_FOREST, 1);
        dbase.setMultipass(SugiBiomes.SUGI_FOREST, pass -> {
            switch(pass) {
                case 0: return 0;//Zero means to run as normal
                case 1: return 5;//Return only radius 5 on pass 1
                case 2: return 3;//Return only radius 3 on pass 2
                default: return -1;//A negative number means to terminate
            }
        });
        dbase.setCancelVanillaTreeGen(SugiBiomes.SUGI_FOREST, true);
        dbase.setSpeciesSelector(SugiBiomes.SUGI_FOREST, new BiomePropertySelectors.RandomSpeciesSelector().add(sugi, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(SugiBiomes.SUGI_FOREST, (rand, noiseDensity) -> noiseDensity*2f, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(SugiBiomes.SUGI_FOREST, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

    }
}

