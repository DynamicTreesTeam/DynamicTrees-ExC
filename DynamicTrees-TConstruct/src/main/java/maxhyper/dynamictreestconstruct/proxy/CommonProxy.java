package maxhyper.dynamictreestconstruct.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import maxhyper.dynamictreestconstruct.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreestconstruct.growth.SlimeGrowthLogic;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.shared.TinkerCommons;

public class CommonProxy {
	
	public void preInit() {

			TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "slime"), new SlimeGrowthLogic());
			slimeknights.tconstruct.common.config.Config.genSlimeIslands = false;

	}
	
	public void init() {

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeBlue")).
				addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallBlue).setRarity(0.05f));

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimePurple")).
				addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallPurple).setRarity(0.05f));

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeMagma")).
				addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallMagma).setRarity(0.05f));

	}
	
	public void postInit() {
	}
	
}
