package maxhyper.dynamictreesnatura.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.progwml6.natura.common.config.Config;
import com.progwml6.natura.overworld.block.saplings.BlockOverworldSapling;
import com.progwml6.natura.shared.NaturaCommons;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.dropcreators.DropCreatorOtherSeed;
import maxhyper.dynamictreesnatura.growth.BloodwoodGrowthLogic;
import maxhyper.dynamictreesnatura.growth.CustomCellKits;
import maxhyper.dynamictreesnatura.growth.HopseedGrowthLogic;
import maxhyper.dynamictreesnatura.worldgen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
	
	public void preInit() {
		new CustomCellKits(); //Initialize CellKits
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "hopseed"), new HopseedGrowthLogic());
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "bloodwood"), new BloodwoodGrowthLogic());

		if(WorldGenRegistry.isWorldGenEnabled()) {
			GameRegistry.registerWorldGenerator(new WorldGen(), 10);

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
			Config.generateBloodwood = false;
			ModContent.generateDarkwood = Config.generateDarkwood;
			Config.generateDarkwood = false;
			ModContent.generateFusewood = Config.generateFusewood;
			Config.generateFusewood = false;
			ModContent.generateGhostwood = Config.generateGhostwood;
			Config.generateGhostwood = false;
			ModContent.generateSaguaro = Config.generateSaguaro;
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
