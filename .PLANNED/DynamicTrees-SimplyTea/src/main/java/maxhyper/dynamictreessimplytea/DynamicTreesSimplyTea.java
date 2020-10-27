package maxhyper.dynamictreessimplytea;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreessimplytea.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesSimplyTea.MODID, name= DynamicTreesSimplyTea.NAME, dependencies = DynamicTreesSimplyTea.DEPENDENCIES)
public class DynamicTreesSimplyTea {
	
	public static final String MODID = "dynamictreessimplytea";
	public static final String NAME = "Dynamic Trees for Simply Tea";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:simplytea";
	
	@Mod.Instance
	public static DynamicTreesSimplyTea instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreessimplytea.proxy.ClientProxy", serverSide = "maxhyper.dynamictreessimplytea.proxy.CommonProxy") //com.
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
