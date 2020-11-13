package maxhyper.dynamictreesthaumicbases;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesthaumicbases.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesThaumcraftAddons.MODID, name= DynamicTreesThaumcraftAddons.NAME, dependencies = DynamicTreesThaumcraftAddons.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-ThaumcraftAddons/version_info.json?raw=true")
public class DynamicTreesThaumcraftAddons {
	
	public static final String MODID = "dynamictreesthaumicbases";
	public static final String NAME = "Dynamic Trees for Thaumcraft Addons";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";after:thaumcraft"
			+ ";required-after:thaumicbases@[3.3.500.6r,)";
	
	@Mod.Instance
	public static DynamicTreesThaumcraftAddons instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesthaumicbases.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesthaumicbases.proxy.CommonProxy") //com.
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
