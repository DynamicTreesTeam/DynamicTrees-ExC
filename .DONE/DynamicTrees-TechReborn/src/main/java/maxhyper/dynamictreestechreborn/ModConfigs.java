package maxhyper.dynamictreestechreborn;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static boolean classicLookingRubberTree;
    public static float rubberDropMultiplier;
    public static float sapChance;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Trees
        classicLookingRubberTree = config.getBoolean("classicLookingRubberTree", "trees", false, "Makes rubber trees shorter and pointier, similar to the non-dynamic version.");
        rubberDropMultiplier = config.getFloat("rubberDropMultiplier", "trees", 0.8f, 0.0f, 128.0f, "The multiplier for rubber dropped when chopping a Rubber tree down.");
        sapChance = config.getFloat("sapChance", "trees", 0.02f, 0, 1, "The chance on each tree growth signal that a rubber tree attempts to generate a filled tapping spot.");

        config.save();
    }
}
