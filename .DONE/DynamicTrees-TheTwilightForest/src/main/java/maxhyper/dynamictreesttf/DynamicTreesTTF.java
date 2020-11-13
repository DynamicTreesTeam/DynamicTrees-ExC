package maxhyper.dynamictreesttf;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreesttf.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesTTF.MODID, name= DynamicTreesTTF.NAME, dependencies = DynamicTreesTTF.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-TheTwilightForest/version_info.json?raw=true")
public class DynamicTreesTTF {
	
	public static final String MODID = "dynamictreesttf";
	public static final String NAME = "Dynamic Trees for The Twilight Forest";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST + ";required-after:twilightforest@[3.10.1013, 4.0)";
	
	@Mod.Instance
	public static DynamicTreesTTF instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesttf.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesttf.proxy.CommonProxy") //com.
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
