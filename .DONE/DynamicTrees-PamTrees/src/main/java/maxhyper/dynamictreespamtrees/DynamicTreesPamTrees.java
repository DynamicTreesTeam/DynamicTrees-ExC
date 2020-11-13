package maxhyper.dynamictreespamtrees;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreespamtrees.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesPamTrees.MODID, name= DynamicTreesPamTrees.NAME, dependencies = DynamicTreesPamTrees.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-PamTrees/version_info.json?raw=true")
public class DynamicTreesPamTrees {

	public static final String REDBUDTREE_MOD = "redbudtree";
	public static final String SPOOKYTREE_MOD = "spookytree";

	public static final String MODID = "dynamictreespamtrees";
	public static final String NAME = "Dynamic Trees for Pam's Trees";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";after:"+REDBUDTREE_MOD+"@[1.12.2b,)"
			+ ";after:"+SPOOKYTREE_MOD;
	
	@Mod.Instance
	public static DynamicTreesPamTrees instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreespamtrees.proxy.ClientProxy", serverSide = "maxhyper.dynamictreespamtrees.proxy.CommonProxy") //com.
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
