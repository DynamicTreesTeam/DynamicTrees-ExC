package maxhyper.dynamictreestconstruct;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ModConfigs {

    public static File configDirectory;

    public static boolean greenSlimeBallsInBlueTrees;
    public static boolean blueSlimeBallsInBlueTrees;
    public static boolean purpleSlimeBallsInPurpleTrees;
    public static boolean orangeSlimeBallsInMagmaTrees;

    public static void preInit(FMLPreInitializationEvent event) {

        configDirectory = event.getModConfigurationDirectory();

        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        //Trees
        greenSlimeBallsInBlueTrees = config.getBoolean("greenSlimeBallsInBlueTrees", "trees", true, "Enable green slime balls growing like fruit under blue slime trees.");
        blueSlimeBallsInBlueTrees = config.getBoolean("blueSlimeBallsInBlueTrees", "trees", true, "Enable blue slime balls growing like fruit under blue slime trees.");
        purpleSlimeBallsInPurpleTrees = config.getBoolean("purpleSlimeBallsInPurpleTrees", "trees", true, "Enable purple slime balls growing like fruit under purple slime trees.");
        orangeSlimeBallsInMagmaTrees = config.getBoolean("orangeSlimeBallsInMagmaTrees", "trees", true, "Enable orange slime balls growing like fruit under magma slime trees.");

        config.save();
    }
}
