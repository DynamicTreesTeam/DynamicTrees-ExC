package maxhyper.dynamictreesintegrateddynamics;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreesintegrateddynamics.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesIntegratedDynamics.MODID, name= DynamicTreesIntegratedDynamics.NAME, dependencies = DynamicTreesIntegratedDynamics.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-IntegratedDynamics/version_info.json?raw=true")
public class DynamicTreesIntegratedDynamics {
	
	public static final String MODID = "dynamictreesintegrateddynamics";
	public static final String NAME = "Dynamic Trees for Integrated Dynamics";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:integrateddynamics";
	
	@Mod.Instance
	public static DynamicTreesIntegratedDynamics instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesintegrateddynamics.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesintegrateddynamics.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
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
