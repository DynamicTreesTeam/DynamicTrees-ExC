package maxhyper.dynamictreestheaether;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreestheaether.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, name= maxhyper.dynamictreestheaether.DynamicTreesTheAether.NAME, dependencies = maxhyper.dynamictreestheaether.DynamicTreesTheAether.DEPENDENCIES)
public class DynamicTreesTheAether {
	
	public static final String MODID = "dynamictreestheaether";
	public static final String NAME = "Dynamic Trees for The Aether Legacy";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:aether_legacy";
	
	@Mod.Instance
	public static maxhyper.dynamictreestheaether.DynamicTreesTheAether instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestheaether.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestheaether.proxy.CommonProxy") //com.
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
