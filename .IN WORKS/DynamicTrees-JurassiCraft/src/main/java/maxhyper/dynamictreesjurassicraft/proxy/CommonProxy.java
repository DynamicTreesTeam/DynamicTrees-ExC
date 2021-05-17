package maxhyper.dynamictreesjurassicraft.proxy;


import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreesjurassicraft.DynamicTreesJurassiCraft;
import maxhyper.dynamictreesjurassicraft.growth.AraucariaLogic;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preInit() {
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(DynamicTreesJurassiCraft.MODID, "araucaria"), new AraucariaLogic());
	}
	
	public void init() {
	}
	
	public void postInit() {
	}
	
}
