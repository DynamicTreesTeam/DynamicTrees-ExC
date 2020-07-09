package maxhyper.dynamictreesfossil;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesfossil.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesFossil.MODID, name= DynamicTreesFossil.NAME, dependencies = DynamicTreesFossil.DEPENDENCIES)
public class DynamicTreesFossil {
	
	public static final String MODID = "dynamictreesfossil";
	public static final String NAME = "Dynamic Trees for Fossils and Archeology";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:fossil";
	
	@Mod.Instance
	public static DynamicTreesFossil instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesfossil.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesfossil.proxy.CommonProxy") //com.
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
