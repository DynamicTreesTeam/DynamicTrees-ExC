package maxhyper.dynamictreesttf.worldgen;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import twilightforest.TFConfig;
import twilightforest.biomes.TFBiomes;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    public static Biome[] TwilightBiomes = {
            TFBiomes.clearing,              //no trees
            TFBiomes.darkForest,            //done
            TFBiomes.darkForestCenter,      //done
            TFBiomes.deepMushrooms,         //done
            TFBiomes.denseTwilightForest,   //done
            TFBiomes.enchantedForest,       //done
            TFBiomes.fireflyForest,         //done
            TFBiomes.fireSwamp,             //done
            TFBiomes.glacier,               //no trees
            TFBiomes.highlands,             //done
            TFBiomes.highlandsCenter,       //no trees
            TFBiomes.mushrooms,             //done
            TFBiomes.oakSavanna,            //done
            TFBiomes.snowy_forest,          //done
            TFBiomes.spookyForest,          //done
            TFBiomes.stream,                //no trees
            TFBiomes.tfLake,                //no trees
            TFBiomes.tfSwamp,               //done
            TFBiomes.thornlands,            //no trees
            TFBiomes.twilightForest         //done
    };

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator (){
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(DynamicTreesTTF.MODID, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        ModConfigs.dimensionBlacklist.remove(TFConfig.dimension.dimensionID);
        jsonPopulator.populate(dbase);
    }
}
