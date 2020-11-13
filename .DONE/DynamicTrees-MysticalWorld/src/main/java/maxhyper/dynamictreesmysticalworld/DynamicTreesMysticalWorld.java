package maxhyper.dynamictreesmysticalworld;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesmysticalworld.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesMysticalWorld.MODID, name= DynamicTreesMysticalWorld.NAME, dependencies = DynamicTreesMysticalWorld.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-MysticalWorld/version_info.json?raw=true")
public class DynamicTreesMysticalWorld {
	
	public static final String MODID = "dynamictreesmysticalworld";
	public static final String NAME = "Dynamic Trees for Mystical World";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:mysticalworld";
	
	@Mod.Instance
	public static DynamicTreesMysticalWorld instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesmysticalworld.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesmysticalworld.proxy.CommonProxy") //com.
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
