package maxhyper.dynamictreestheaether.proxy;


import com.legacy.aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.ModContent;
import maxhyper.dynamictreestheaether.growth.CustomCellKits;
import maxhyper.dynamictreestheaether.worldgen.WorldGen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void preInit() {
		CustomCellKits.preInit();
		GameRegistry.registerWorldGenerator(new WorldGen(), 0);
	}
	
	public void init() {
		ModContent.blockWhiteApple.setDroppedItem(new ItemStack(ItemsAether.white_apple));
	}
	
	public void postInit() {
	}
	
}
