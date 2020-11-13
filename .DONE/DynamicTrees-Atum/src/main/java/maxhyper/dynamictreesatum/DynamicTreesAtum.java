package maxhyper.dynamictreesatum;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreesatum.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesAtum.MODID, name= DynamicTreesAtum.NAME, dependencies = DynamicTreesAtum.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-Atum/version_info.json?raw=true")
public class DynamicTreesAtum {
	
	public static final String MODID = "dynamictreesatum";
	public static final String NAME = "Dynamic Trees Extra Compat";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:atum@[2.0.18,)";
	
	@Mod.Instance
	public static DynamicTreesAtum instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesatum.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesatum.proxy.CommonProxy") //com.
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
