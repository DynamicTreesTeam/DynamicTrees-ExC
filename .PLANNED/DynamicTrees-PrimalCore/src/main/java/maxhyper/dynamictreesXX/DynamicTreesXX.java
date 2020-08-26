package maxhyper.dynamictreesXX;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesXX.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesXX.MODID, name= DynamicTreesXX.NAME, dependencies = DynamicTreesXX.DEPENDENCIES)
public class DynamicTreesXX {
	
	public static final String MODID = "dynamictreesXX";
	public static final String NAME = "Dynamic Trees for XX";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:sugiforest";
	
	@Mod.Instance
	public static DynamicTreesXX instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesXX.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesXX.proxy.CommonProxy") //com.
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
