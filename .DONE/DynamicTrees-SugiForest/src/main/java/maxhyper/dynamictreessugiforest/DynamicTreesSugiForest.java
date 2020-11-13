package maxhyper.dynamictreessugiforest;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreessugiforest.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesSugiForest.MODID, name= DynamicTreesSugiForest.NAME, dependencies = DynamicTreesSugiForest.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-SugiForest/version_info.json?raw=true")
public class DynamicTreesSugiForest {
	
	public static final String MODID = "dynamictreessugiforest";
	public static final String NAME = "Dynamic Trees for Sugi Forest";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:sugiforest";
	
	@Mod.Instance
	public static DynamicTreesSugiForest instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreessugiforest.proxy.ClientProxy", serverSide = "maxhyper.dynamictreessugiforest.proxy.CommonProxy") //com.
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
