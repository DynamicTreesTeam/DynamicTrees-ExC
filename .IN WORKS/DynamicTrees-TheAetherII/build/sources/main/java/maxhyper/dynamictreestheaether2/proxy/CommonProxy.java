package maxhyper.dynamictreestheaether2.proxy;

import maxhyper.dynamictreestheaether2.ModContent;
import maxhyper.dynamictreestheaether2.growth.CustomCellKits;
import maxhyper.dynamictreestheaether2.worldgen.WorldGen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void preInit() {
		CustomCellKits.preInit();
		GameRegistry.registerWorldGenerator(new WorldGen(), 0);
	}
	
	public void init() {
	}
	
	public void postInit() {
	}
	
}
