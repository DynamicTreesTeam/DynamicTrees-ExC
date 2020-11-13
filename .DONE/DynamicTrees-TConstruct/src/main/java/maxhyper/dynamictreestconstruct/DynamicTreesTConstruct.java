package maxhyper.dynamictreestconstruct;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreestconstruct.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import slimeknights.tconstruct.TConstruct;

@Mod(modid= DynamicTreesTConstruct.MODID, name= DynamicTreesTConstruct.NAME, dependencies = DynamicTreesTConstruct.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-TConstruct/version_info.json?raw=true")
public class DynamicTreesTConstruct {
	
	public static final String MODID = "dynamictreestconstruct";
	public static final String NAME = "Dynamic Trees for Tinkers' Construct";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";after:tconstruct";
	
	@Mod.Instance
	public static DynamicTreesTConstruct instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestconstruct.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestconstruct.proxy.CommonProxy") //com.
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld"))
			return;
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld"))
			return;
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld"))
			return;
		proxy.postInit();
	}
	
}
