package maxhyper.dynamictreesmistyworld;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesmistyworld.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesMistyWorld.MODID, name= DynamicTreesMistyWorld.NAME, dependencies = DynamicTreesMistyWorld.DEPENDENCIES)
public class DynamicTreesMistyWorld {
	
	public static final String MODID = "dynamictreesmistyworld";
	public static final String NAME = "Dynamic Trees for Misty World";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:mist";
	
	@Mod.Instance
	public static DynamicTreesMistyWorld instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesmistyworld.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesmistyworld.proxy.CommonProxy") //com.
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
