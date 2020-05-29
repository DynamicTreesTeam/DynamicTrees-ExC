package maxhyper.dynamictreesnatura.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.progwml6.natura.common.config.Config;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.growth.BloodwoodGrowthLogic;
import maxhyper.dynamictreesnatura.growth.CustomCellKits;
import maxhyper.dynamictreesnatura.growth.HopseedGrowthLogic;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preInit() {
		new CustomCellKits(); //Initialize CellKits
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "hopseed"), new HopseedGrowthLogic());
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "bloodwood"), new BloodwoodGrowthLogic());

		ModContent.generateMaple = Config.generateMaple;
		Config.generateMaple = false;
		ModContent.generateSilverbell = Config.generateSilverbell;
		Config.generateSilverbell = false;
		ModContent.generateAmaranth = Config.generateAmaranth;
		Config.generateAmaranth = false;
		ModContent.generateTiger = Config.generateTiger;
		Config.generateTiger = false;
		ModContent.generateWillow = Config.generateWillow;
		Config.generateWillow = false;
		ModContent.generateEucalyptus = Config.generateEucalyptus;
		Config.generateEucalyptus = false;
		ModContent.generateHopseed = Config.generateHopseed;
		Config.generateHopseed = false;
		ModContent.generateSakura = Config.generateSakura;
		Config.generateSakura = false;
		ModContent.generateBloodwood = Config.generateBloodwood;
		//Config.generateBloodwood = false;
		ModContent.generateDarkwood = Config.generateDarkwood;
		Config.generateDarkwood = false;
		ModContent.generateFusewood = Config.generateFusewood;
		Config.generateFusewood = false;
		ModContent.generateGhostwood = Config.generateGhostwood;
		Config.generateGhostwood = false;
		ModContent.generateSaguaro = Config.generateSaguaro;
		Config.generateSaguaro = false;

	}
	
	public void init() {
	}
	
	public void postInit(){
	}
	
}
