package maxhyper.dynamictreesic2;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static boolean classicLookingRubberTree;

    public static int nothingToEmptyChance;
    public static int emptyToFilledChance;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Trees
        classicLookingRubberTree = config.getBoolean("classicLookingRubberTree", "trees", false, "Makes rubber trees shorter and pointier, similar to the non-dynamic version.");

        nothingToEmptyChance = config.getInt("nothingToEmptyChance", "trees", 200, 0, Integer.MAX_VALUE, "The chance on each game tick that a rubber branch without a tapping spot generates one.");

        emptyToFilledChance = config.getInt("emptyToFilledChance", "trees", 50, 0, Integer.MAX_VALUE, "The chance on each game tick that a rubber branch with a tapping spot fills it.");

        config.save();
    }
}
