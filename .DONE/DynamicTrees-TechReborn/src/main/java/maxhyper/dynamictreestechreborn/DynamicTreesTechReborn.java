package maxhyper.dynamictreestechreborn;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreestechreborn.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesTechReborn.MODID, name= DynamicTreesTechReborn.NAME, dependencies = DynamicTreesTechReborn.DEPENDENCIES)
public class DynamicTreesTechReborn {
	
	public static final String MODID = "dynamictreestechreborn";
	public static final String NAME = "Dynamic Trees Extra Compat";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			//+ ";after:quark"
			+ ";required-after:techreborn";
	
	@Mod.Instance
	public static DynamicTreesTechReborn instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestechreborn.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestechreborn.proxy.CommonProxy") //com.
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
