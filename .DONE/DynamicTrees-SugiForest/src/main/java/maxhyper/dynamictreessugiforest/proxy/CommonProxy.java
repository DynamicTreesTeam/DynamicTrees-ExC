package maxhyper.dynamictreessugiforest.proxy;


import maxhyper.dynamictreessugiforest.growth.CustomCellKits;
import sugiforest.core.Config;

public class CommonProxy {
	
	public void preInit() {
		CustomCellKits.preInit();
	}
	
	public void init() {
		Config.sugiOnHills = 0;
	}
	
	public void postInit() {
	}
	
}
