package maxhyper.dynamictreesforestry;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreesforestry.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesForestry.MODID, name= DynamicTreesForestry.NAME, dependencies = DynamicTreesForestry.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-Forestry/version_info.json?raw=true")
public class DynamicTreesForestry {
	
	public static final String MODID = "dynamictreesforestry";
	public static final String NAME = "Dynamic Trees for Forestry";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST + ";required-after:forestry";
	
	@Mod.Instance
	public static DynamicTreesForestry instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesforestry.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesforestry.proxy.CommonProxy")
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
