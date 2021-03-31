package maxhyper.dynamictreesforestry;

import net.minecraftforge.common.config.Config;

@Config(modid = DynamicTreesForestry.MODID)
public class ModConfigs {

    @Config.Comment("Allows fruity leaves to grow fruit on their own, like the non-dynamic versions (in addition to the Dynamic Trees fruit blocks).")
    @Config.Name("Fruity Leaves")
    @Config.RequiresMcRestart()
    public static boolean fruityLeaves = true;

    @Config.Comment("Allows bone meal to be used on fruity tree leaves, similar to non-dynamic Forestry's leaves. requires Fruity Leaves to be true.")
    @Config.Name("Bone Meal fruity leaves")
    public static boolean boneMealLeaves = true;

}