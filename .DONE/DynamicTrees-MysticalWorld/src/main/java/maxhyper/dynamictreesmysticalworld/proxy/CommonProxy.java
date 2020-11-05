package maxhyper.dynamictreesmysticalworld.proxy;


import com.ferreusveritas.dynamictrees.ModConfigs;
import epicsquid.mysticalworld.config.ConfigManager;

public class CommonProxy {
	
	public void preInit() {
	}
	
	public void init() {
		if (ModConfigs.worldGen){
			ConfigManager.burntTrees.attempts = 0;
		}
	}
	
	public void postInit() {
	}
	
}
