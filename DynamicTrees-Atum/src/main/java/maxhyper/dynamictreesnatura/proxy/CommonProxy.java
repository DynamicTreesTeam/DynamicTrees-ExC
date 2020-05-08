package maxhyper.dynamictreesnatura.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.progwml6.natura.common.config.Config;
import maxhyper.dynamictreesnatura.growth.BloodwoodGrowthLogic;
import maxhyper.dynamictreesnatura.growth.CustomCellKits;
import maxhyper.dynamictreesnatura.growth.HopseedGrowthLogic;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preInit() {
		new CustomCellKits(); //Initialize CellKits
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "hopseed"), new HopseedGrowthLogic());
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "bloodwood"), new BloodwoodGrowthLogic());
		Config.generateMaple = false;
		Config.generateSilverbell = false;
		Config.generateAmaranth = false;
		Config.generateTiger = false;
		Config.generateWillow = false;
		Config.generateEucalyptus = false;
		Config.generateHopseed = false;
		Config.generateSakura = false;
		//Config.generateBloodwood = false;
		Config.generateDarkwood = false;
		Config.generateFusewood = false;
		Config.generateGhostwood = false;
	}
	
	public void init() {
	}
	
	public void postInit(){
	}
	
}
