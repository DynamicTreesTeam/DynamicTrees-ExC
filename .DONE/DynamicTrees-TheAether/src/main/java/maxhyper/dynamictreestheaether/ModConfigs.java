package maxhyper.dynamictreestheaether;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static float holidayTreeChance;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Trees
        holidayTreeChance = config.getFloat("holidayTreeChance", "trees", 0.02f, 0f, 1f, "Chance for a random Skyroot tree to be a Christmas tree when holiday mode is enabled in The Aether.");

        config.save();
    }
}
