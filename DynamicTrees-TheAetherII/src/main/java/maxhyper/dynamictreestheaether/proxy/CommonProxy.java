package maxhyper.dynamictreestheaether.proxy;


import maxhyper.dynamictreestheaether.growth.CustomCellKits;
import maxhyper.dynamictreestheaether.worldgen.WorldGen;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void preInit() {
		CustomCellKits.preInit();
		//MinecraftForge.EVENT_BUS.register(new DecorateBiomeEventHandler());
	}
	
	public void init() {
		GameRegistry.registerWorldGenerator(new WorldGen(), 0);
	}
	
	public void postInit() {
	}
	
}
