package maxhyper.dynamictreesextrautils2;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesextrautils2.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesExtraUtils2.MODID, name= DynamicTreesExtraUtils2.NAME, dependencies = DynamicTreesExtraUtils2.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-ExtraUtils2/version_info.json?raw=true")
public class DynamicTreesExtraUtils2 {
	
	public static final String MODID = "dynamictreesextrautils2";
	public static final String NAME = "Dynamic Trees for Extra Utilities 2";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:extrautils2";
	
	@Mod.Instance
	public static DynamicTreesExtraUtils2 instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesextrautils2.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesextrautils2.proxy.CommonProxy") //com.
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
