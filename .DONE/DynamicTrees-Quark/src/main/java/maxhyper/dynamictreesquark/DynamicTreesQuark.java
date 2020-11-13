package maxhyper.dynamictreesquark;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesquark.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesQuark.MODID, name= DynamicTreesQuark.NAME, dependencies = DynamicTreesQuark.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-Quark/version_info.json?raw=true")
public class DynamicTreesQuark {
	
	public static final String MODID = "dynamictreesquark";
	public static final String NAME = "Dynamic Trees for Quark";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:quark";
	
	@Mod.Instance
	public static DynamicTreesQuark instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesquark.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesquark.proxy.CommonProxy") //com.
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
