package maxhyper.dynamictreestotemic;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreestotemic.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesTotemic.MODID, name= DynamicTreesTotemic.NAME, dependencies = DynamicTreesTotemic.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/tree/1.12.2/.DONE/DynamicTrees-Totemic/version_info.json?raw=true")
public class DynamicTreesTotemic {
	
	public static final String MODID = "dynamictreestotemic";
	public static final String NAME = "Dynamic Trees for Totemic";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:totemic";
	
	@Mod.Instance
	public static DynamicTreesTotemic instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestotemic.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestotemic.proxy.CommonProxy") //com.
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
