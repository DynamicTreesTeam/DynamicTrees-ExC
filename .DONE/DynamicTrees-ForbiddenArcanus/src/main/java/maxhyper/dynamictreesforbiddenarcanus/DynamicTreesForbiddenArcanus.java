package maxhyper.dynamictreesforbiddenarcanus;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesforbiddenarcanus.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesForbiddenArcanus.MODID, name= DynamicTreesForbiddenArcanus.NAME, dependencies = DynamicTreesForbiddenArcanus.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-ForbiddenArcanus/version_info.json?raw=true")
public class DynamicTreesForbiddenArcanus {
	
	public static final String MODID = "dynamictreesforbiddenarcanus";
	public static final String NAME = "Dynamic Trees for Forbidden & Arcanus";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:forbidden_arcanus";
	
	@Mod.Instance
	public static DynamicTreesForbiddenArcanus instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesforbiddenarcanus.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesforbiddenarcanus.proxy.CommonProxy") //com.
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
