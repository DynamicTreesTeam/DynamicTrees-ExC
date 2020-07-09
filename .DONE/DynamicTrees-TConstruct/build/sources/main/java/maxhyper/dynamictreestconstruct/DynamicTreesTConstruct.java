package maxhyper.dynamictreestconstruct;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreestconstruct.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesTConstruct.MODID, name= DynamicTreesTConstruct.NAME, dependencies = DynamicTreesTConstruct.DEPENDENCIES)
public class DynamicTreesTConstruct {
	
	public static final String MODID = "dynamictreestconstruct";
	public static final String NAME = "Dynamic Trees Extra Compat";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";after:tconstruct";
	
	@Mod.Instance
	public static DynamicTreesTConstruct instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestconstruct.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestconstruct.proxy.CommonProxy") //com.
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
