package maxhyper.dynamictreestbl;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static float sapDropMultiplier;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Trees
        sapDropMultiplier = config.getFloat("sapDropMultiplier", "trees", 1.5f, 0.0f, 128.0f, "The multiplier for sap dropped when chopping a Sap tree down.");

        config.save();
    }
}
