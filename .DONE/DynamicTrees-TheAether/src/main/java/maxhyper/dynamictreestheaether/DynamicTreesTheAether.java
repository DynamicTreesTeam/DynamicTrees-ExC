package maxhyper.dynamictreestheaether;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreestheaether.proxy.CommonProxy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, name= maxhyper.dynamictreestheaether.DynamicTreesTheAether.NAME, dependencies = maxhyper.dynamictreestheaether.DynamicTreesTheAether.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-TheAether/version_info.json?raw=true")
public class DynamicTreesTheAether {
	
	public static final String MODID = "dynamictreestheaether";
	public static final String NAME = "Dynamic Trees for The Aether";
	public static final String LOSTAETHER = "lost_aether";
	public static final String DEPENDENCIES = "required-after:" + "dynamictrees@[1.12.2-0.9.23,)"
			+ ";required-after:aether_legacy@[1.5.0,)" +";after:"+LOSTAETHER;
	
	@Mod.Instance
	public static maxhyper.dynamictreestheaether.DynamicTreesTheAether instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestheaether.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestheaether.proxy.CommonProxy") //com.
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
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
