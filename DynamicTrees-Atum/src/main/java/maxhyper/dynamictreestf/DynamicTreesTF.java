package maxhyper.dynamictreestf;

import com.ferreusveritas.dynamictrees.ModConstants;
import maxhyper.dynamictreestf.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesTF.MODID, name= DynamicTreesTF.NAME, dependencies = DynamicTreesTF.DEPENDENCIES)
public class DynamicTreesTF {
	
	public static final String MODID = "dynamictreestf";
	public static final String NAME = "Dynamic Trees for Twilight Forest";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST + ";required-after:twilightforest";
	
	@Mod.Instance
	public static DynamicTreesTF instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreestf.proxy.ClientProxy", serverSide = "maxhyper.dynamictreestf.proxy.CommonProxy") //com.
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
