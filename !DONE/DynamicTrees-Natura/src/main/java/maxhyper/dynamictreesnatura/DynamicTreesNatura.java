package maxhyper.dynamictreesnatura;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreesnatura.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesNatura.MODID, name=DynamicTreesNatura.NAME, dependencies = DynamicTreesNatura.DEPENDENCIES)
public class DynamicTreesNatura {
	
	public static final String MODID = "dynamictreesnatura";
	public static final String NAME = "Dynamic Trees for Natura";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST + ";required-after:natura";
	
	@Mod.Instance
	public static DynamicTreesNatura instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesnatura.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesnatura.proxy.CommonProxy")
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
