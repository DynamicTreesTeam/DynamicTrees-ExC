package maxhyper.dynamictreesintegrateddynamics;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static boolean menrilBerriesOnMenrilTrees;
    public static float menrilResinMultiplier;
    public static float menrilBerriesRarity;
    public static float sapChance;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Trees
        menrilBerriesOnMenrilTrees = config.getBoolean("menrilBerriesOnMenrilTrees", "trees", true, "Enable green menril berries growing like fruit under menril trees.");
        menrilResinMultiplier = config.getFloat("menrilResinMultiplier", "trees", 1.8f, 0.0f, 128.0f, "The multiplier for Menril Chunks dropped when chopping a Menril tree down.");
        menrilBerriesRarity = config.getFloat("menrilBerriesRarity", "trees", 0.15f, 0.0f, 1.0f, "The chance for Menril leaves to drop Menril Berries.");

        sapChance = config.getFloat("sapChance", "trees", 0.02f, 0, 1, "The chance on each tree growth signal that a menril tree attempts to generate a filled tapping spot.");

        config.save();
    }
}
