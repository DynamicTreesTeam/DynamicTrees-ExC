package maxhyper.dynamictreesjurassicraft;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesjurassicraft.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesJurassiCraft.MODID, name= DynamicTreesJurassiCraft.NAME, dependencies = DynamicTreesJurassiCraft.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-JurassiCraft/version_info.json?raw=true")
public class DynamicTreesJurassiCraft {
	
	public static final String MODID = "dynamictreesjurassicraft";
	public static final String NAME = "Dynamic Trees for JurassiCraft";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:jurassicraft@[2.1.23,)";
	
	@Mod.Instance
	public static DynamicTreesJurassiCraft instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesjurassicraft.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesjurassicraft.proxy.CommonProxy") //com.
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
