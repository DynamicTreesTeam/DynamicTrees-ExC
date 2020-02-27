package maxhyper.dynamictreesbl;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreesbl.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesBL.MODID, name= DynamicTreesBL.NAME, dependencies = DynamicTreesBL.DEPENDENCIES)
public class DynamicTreesBL {
	
	public static final String MODID = "dynamictreesbl";
	public static final String NAME = "Dynamic Trees for The Between Lands";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST + ";required-after:thebetweenlands";
	
	@Mod.Instance
	public static maxhyper.dynamictreesbl.DynamicTreesBL instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesbl.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesbl.proxy.CommonProxy")
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
