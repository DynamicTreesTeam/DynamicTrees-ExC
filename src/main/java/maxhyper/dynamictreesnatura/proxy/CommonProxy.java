package maxhyper.dynamictreesnatura.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreesnatura.growth.BloodwoodGrowthLogic;
import maxhyper.dynamictreesnatura.growth.CustomCellKits;
import maxhyper.dynamictreesnatura.growth.HopseedGrowthLogic;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preInit() {
		new CustomCellKits(); //Initialize CellKits
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "hopseed"), new HopseedGrowthLogic());
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "bloodwood"), new BloodwoodGrowthLogic());
	}
	
	public void init() {
	}
	
	public void postInit(){
	}
	
}
