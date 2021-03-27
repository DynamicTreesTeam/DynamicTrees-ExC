package maxhyper.dynamictreescuisine;

import net.minecraftforge.common.config.Config;

@Config(modid = DynamicTreesCuisine.MODID)
public class ModConfigs {

	@Config.Comment("Allows leaves to grow fruit on their own, like the non-dynamic versions (in addition to the Dynamic Trees fruit blocks).")
	@Config.Name("Fruity Leaves")
	@Config.RequiresMcRestart()
	public static boolean fruityLeaves = true;

}
