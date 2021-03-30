package maxhyper.dynamictreestheaether;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Config(modid = DynamicTreesTheAether.MODID)
public class ModConfigs {

    @Config.Comment("Chance for a random Skyroot tree to be a Christmas tree when holiday mode is enabled in The Aether.")
    @Config.Name("Holiday Tree Chance")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    @Config.RequiresMcRestart()
    public static float holidayTreeChance = 0.02f;

    @Config.Comment("Allows crystal leaves to grow fruit on their own, like the non-dynamic versions (in addition to the Dynamic Trees fruit blocks).")
    @Config.Name("Fruity Leaves")
    @Config.RequiresMcRestart()
    public static boolean fruityLeaves = true;

    @Config.Comment("Allows crystal leaves with fruit to be right clicked instead of needing to break the block. requires \"Fruity Leaves\" to be set to true.")
    @Config.Name("Fruity Leaves")
    public static boolean pickFruitFromLeaves = true;

    @Config.Comment("Allows crystal fruit to grow over time. If the Lost Aether addon is installed, this is overriden to true.")
    @Config.Name("Crystal Trees Grow")
    public static boolean crystalTreesGrow = false;

}