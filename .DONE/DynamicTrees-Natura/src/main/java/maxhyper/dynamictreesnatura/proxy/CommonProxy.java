package maxhyper.dynamictreesnatura.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.progwml6.natura.common.config.Config;
import com.progwml6.natura.shared.NaturaCommons;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.dropcreators.DropCreatorOtherSeed;
import maxhyper.dynamictreesnatura.growth.BloodwoodGrowthLogic;
import maxhyper.dynamictreesnatura.growth.CustomCellKits;
import maxhyper.dynamictreesnatura.growth.HopseedGrowthLogic;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preInit() {
		new CustomCellKits(); //Initialize CellKits
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "hopseed"), new HopseedGrowthLogic());
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "bloodwood"), new BloodwoodGrowthLogic());

		if(WorldGenRegistry.isWorldGenEnabled()) {
			//GameRegistry.registerWorldGenerator(new WorldGen(), 10);
			//ModContent.generateBloodwood = Config.generateBloodwood;

			Config.generateMaple = false;
			Config.generateSilverbell = false;
			Config.generateAmaranth = false;
			Config.generateTiger = false;
			Config.generateWillow = false;
			Config.generateEucalyptus = false;
			Config.generateHopseed = false;
			Config.generateSakura = false;
			Config.generateBloodwood = false;
			Config.generateDarkwood = false;
			Config.generateFusewood = false;
			Config.generateGhostwood = false;
			Config.generateSaguaro = false;
		}

	}
	
	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "darkwood"))
				.addDropCreator(new DropCreatorOtherSeed(NaturaCommons.potashApple));

		// Register sapling replacements.
		// registerSaplingReplacement();
	}

	private static void registerSaplingReplacement(final String blockName, final String speciesName) {
		// TreeRegistry.registerSaplingReplacer(Block.getBlockFromName("natura:" + blockName).getDefaultState().withProperty(BlockOverworldSapling.FOLIAGE, "d"), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, speciesName)));
	}

	public void postInit(){
	}
	
}
