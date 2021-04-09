package maxhyper.dynamictreesnatura;

import net.minecraftforge.common.config.Config;

@Config(modid = DynamicTreesNatura.MODID)
public class ModConfigs {

    @Config.Comment("Allows darkwood leaves to grow fruit on their own, like the non-dynamic versions (in addition to the Dynamic Trees fruit blocks).")
    @Config.Name("Fruity Leaves")
    @Config.RequiresMcRestart()
    public static boolean fruityLeaves = true;

    @Config.Comment("Allows darkwood leaves with fruit to be right clicked instead of needing to break the block. requires \"Fruity Leaves\" to be set to true.")
    @Config.Name("Fruity Leaves")
    public static boolean pickFruitFromLeaves = true;

}