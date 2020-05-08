package maxhyper.dynamictreesquark.proxy;

import vazkii.quark.world.feature.TreeVariants;

public class CommonProxy {
	
	public void preInit() {
			TreeVariants.enableSakura = false; //fix
			TreeVariants.enableSwamp = false;
	}
	
	public void init() {
	}
	
	public void postInit() {
	}
	
}
