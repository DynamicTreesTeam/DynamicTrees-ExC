package maxhyper.dynamictreesic2;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static boolean classicLookingRubberTree;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Trees
        classicLookingRubberTree = config.getBoolean("classicLookingRubberTree", "trees", false, "Makes rubber trees shorter and pointier, similar to the non-dynamic version.");

        config.save();
    }
}
