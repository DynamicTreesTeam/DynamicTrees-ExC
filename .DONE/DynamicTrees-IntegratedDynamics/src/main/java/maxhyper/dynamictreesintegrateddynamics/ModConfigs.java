package maxhyper.dynamictreesintegrateddynamics;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static float menrilTreeOccurance;
    public static float menrilResinMultiplier;
    public static float menrilBerriesRarity;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //World
        menrilTreeOccurance = config.getFloat("menrilTreeOccurance", "world", 0.003f, 0.0f, 1.0f, "The chance of a tree outside of the Meneglin Biome to be a Menril tree.");

        //Trees
        menrilResinMultiplier = config.getFloat("menrilResinMultiplier", "trees", 1.8f, 0.0f, 128.0f, "The multiplier for Menril Chunks dropped when chopping a Menril tree down.");
        menrilBerriesRarity = config.getFloat("menrilBerriesRarity", "trees", 0.15f, 0.0f, 1.0f, "The chance for Menril leaves to drop Menril Berries.");


        config.save();
    }
}
