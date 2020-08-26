package maxhyper.dynamictreesexc;

import com.ferreusveritas.dynamictrees.ModConstants;

import maxhyper.dynamictreesexc.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= DynamicTreesExC.MODID, name= DynamicTreesExC.NAME, dependencies = DynamicTreesExC.DEPENDENCIES)
public class DynamicTreesExC {
	
	public static final String MODID = "dynamictreesexc";
	public static final String NAME = "Dynamic Trees Extra Compat";
	public static final String DEPENDENCIES = "required-after:" + ModConstants.DYNAMICTREES_LATEST
			//+ ";after:quark"
			+ ";after:integrateddynamics"
			+ ";after:tconstruct"
			+ ";after:techreborn"
			+ ";after:thaumcraft"
			+ ";after:thaumicbases"
			+ ";after:extrautils2"
			+ ";after:atum"
			+ ";after:forbidden_arcanus"
			+ ";after:simplytea"
			+ ";after:sugiforest"
			+ ";after:betterwithmods"
			+ ";after:betterwithaddons"
			+ ";after:fossil";
	
	@Mod.Instance
	public static DynamicTreesExC instance;
	
	@SidedProxy(clientSide = "maxhyper.dynamictreesexc.proxy.ClientProxy", serverSide = "maxhyper.dynamictreesexc.proxy.CommonProxy") //com.
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
