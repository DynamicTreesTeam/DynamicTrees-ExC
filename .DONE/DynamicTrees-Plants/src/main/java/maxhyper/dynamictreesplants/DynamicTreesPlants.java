package maxhyper.dynamictreesplants;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesplants.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesPlants.MODID, name= DynamicTreesPlants.NAME, dependencies = DynamicTreesPlants.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-Plants/version_info.json?raw=true")
public class DynamicTreesPlants {
	
	public static final String MODID = "dynamictreesplants";
	public static final String NAME = "Dynamic Trees for Plants";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:plants2";
	
	@Mod.Instance
	public static DynamicTreesPlants instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesplants.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesplants.proxy.CommonProxy") //com.
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
