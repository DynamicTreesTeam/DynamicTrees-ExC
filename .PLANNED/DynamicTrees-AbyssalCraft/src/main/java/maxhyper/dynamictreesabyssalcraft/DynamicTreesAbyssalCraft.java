package maxhyper.dynamictreesabyssalcraft;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesabyssalcraft.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesAbyssalCraft.MODID, name= DynamicTreesAbyssalCraft.NAME, dependencies = DynamicTreesAbyssalCraft.DEPENDENCIES)
public class DynamicTreesAbyssalCraft {
	
	public static final String MODID = "dynamictreesabyssalcraft";
	public static final String NAME = "Dynamic Trees for Abyssal Craft";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:abyssalcraft";
	
	@Mod.Instance
	public static DynamicTreesAbyssalCraft instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesabyssalcraft.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesabyssalcraft.proxy.CommonProxy") //com.
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
