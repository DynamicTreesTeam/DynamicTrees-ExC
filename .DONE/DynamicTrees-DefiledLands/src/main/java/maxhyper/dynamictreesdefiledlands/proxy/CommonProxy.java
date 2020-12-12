package maxhyper.dynamictreesdefiledlands.proxy;


import maxhyper.dynamictreesdefiledlands.cells.CellKits;
import maxhyper.dynamictreesdefiledlands.event.TreeGenCancelDefiledEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
	public void preInit() {
		CellKits.init();
		MinecraftForge.TERRAIN_GEN_BUS.register(new TreeGenCancelDefiledEventHandler());
	}
	
	public void init() {
	}
	
	public void postInit() {
	}
	
}
