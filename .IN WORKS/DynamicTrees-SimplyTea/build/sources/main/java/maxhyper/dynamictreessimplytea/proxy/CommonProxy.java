package maxhyper.dynamictreessimplytea.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import elucent.simplytea.core.Config;

public class CommonProxy {
	
	public void preInit() {
		Config.tree.enable_generation = false;
	}
	
	public void init() {
	}
	
	public void postInit() {
	}
	
}
