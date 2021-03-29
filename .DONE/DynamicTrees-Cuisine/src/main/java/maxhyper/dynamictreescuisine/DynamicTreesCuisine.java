package maxhyper.dynamictreescuisine;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreescuisine.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesCuisine.MODID, name= DynamicTreesCuisine.NAME, dependencies = DynamicTreesCuisine.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-Forestry/version_info.json?raw=true")
public class DynamicTreesCuisine {
	
	public static final String MODID = "dynamictreescuisine";
	public static final String NAME = "Dynamic Trees for Cuisine";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:cuisine";
	
	@Mod.Instance
	public static DynamicTreesCuisine instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreescuisine.proxy.ClientProxy", serverSide = "maxhyper.dynamictreescuisine.proxy.CommonProxy") //com.
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		proxy.postInit();
	}
	
}
