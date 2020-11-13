package maxhyper.dynamictreesic2;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreesic2.compat.IC2Proxy;
import maxhyper.dynamictreesic2.proxy.CommonProxy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid= DynamicTreesIC2.MODID, name= DynamicTreesIC2.NAME, dependencies = DynamicTreesIC2.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-IndustrialCraft2/version_info.json?raw=true")
public class DynamicTreesIC2 {
	
	public static final String MODID = "dynamictreesic2";
	public static final String NAME = "Dynamic Trees for Industrial Craft 2";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:ic2";
	
	@Mod.Instance
	public static DynamicTreesIC2 instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesic2.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesic2.proxy.CommonProxy") //com.
	public static CommonProxy proxy;

	public static Logger logger;

	public static IC2Proxy proxyIC2;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		String modName = "";
		for (ModContainer mod : Loader.instance().getModList()){
			if (mod.getModId().equals("ic2")){
				modName = mod.getName();
			}
		}
		try {
			proxyIC2 =
					modName.equals("Industrial Craft Classic")?
							Class.forName("maxhyper.dynamictreesic2.compat.IC2MethodsClassic").asSubclass(IC2Proxy.class).newInstance() :
							Class.forName("maxhyper.dynamictreesic2.compat.IC2Methods").asSubclass(IC2Proxy.class).newInstance() ;
		} catch (Exception e){
			e.printStackTrace();
		}
		logger = event.getModLog();
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
