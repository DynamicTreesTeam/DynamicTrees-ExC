package maxhyper.dynamictreestheaether2;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreestheaether2.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesTheAether2.MODID, name= DynamicTreesTheAether2.NAME, dependencies = DynamicTreesTheAether2.DEPENDENCIES)
public class DynamicTreesTheAether2 {
	
	public static final String MODID = "dynamictreestheaether2";
	public static final String NAME = "Dynamic Trees for The Aether II";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:aether";
	
	@Mod.Instance
	public static DynamicTreesTheAether2 instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestheaether2.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestheaether2.proxy.CommonProxy") //com.
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
