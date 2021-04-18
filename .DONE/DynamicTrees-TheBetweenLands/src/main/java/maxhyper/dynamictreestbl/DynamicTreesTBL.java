package maxhyper.dynamictreestbl;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreestbl.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesTBL.MODID, name= DynamicTreesTBL.NAME, dependencies = DynamicTreesTBL.DEPENDENCIES, updateJSON = "https://github.com/supermassimo/DynamicTrees-ExC/blob/1.12.2/.DONE/DynamicTrees-TheBetweenLands/version_info.json?raw=true")
public class DynamicTreesTBL {
	
	public static final String MODID = "dynamictreestbl";
	public static final String NAME = "Dynamic Trees for The Between Lands";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST + ";required-after:thebetweenlands";
	
	@Mod.Instance
	public static DynamicTreesTBL instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestbl.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestbl.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
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
