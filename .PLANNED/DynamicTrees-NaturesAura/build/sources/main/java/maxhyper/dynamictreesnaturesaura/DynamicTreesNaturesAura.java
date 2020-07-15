package maxhyper.dynamictreesnaturesaura;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesnaturesaura.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesNaturesAura.MODID, name= DynamicTreesNaturesAura.NAME, dependencies = DynamicTreesNaturesAura.DEPENDENCIES)
public class DynamicTreesNaturesAura {
	
	public static final String MODID = "dynamictreesnaturesaura";
	public static final String NAME = "Dynamic Trees for Natures Aura";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			+ ";required-after:naturesaura";
	
	@Mod.Instance
	public static DynamicTreesNaturesAura instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesnaturesaura.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesnaturesaura.proxy.CommonProxy") //com.
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
