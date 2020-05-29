package maxhyper.dynamictreestconstruct.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import maxhyper.dynamictreestconstruct.ModContent;
import maxhyper.dynamictreestconstruct.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreestconstruct.growth.SlimeGrowthLogic;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.shared.TinkerCommons;

public class CommonProxy {

	public void preInit() {
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "slime"), new SlimeGrowthLogic());
		//slimeknights.tconstruct.common.config.Config.genSlimeIslands = false;
	}

	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeBlue"))
				.addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallBlue, new ItemStack(Items.SLIME_BALL)).setRarity(0.005f));

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimePurple")).
				addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallPurple).setRarity(0.005f));

		//TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeMagma")).
		//		addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallMagma, new ItemStack(Items.SLIME_BALL)).setRarity(0.05f));

		ModContent.blockGreenSlime.setDroppedItem(new ItemStack(Items.SLIME_BALL));
		ModContent.blockBlueSlime.setDroppedItem(TinkerCommons.matSlimeBallBlue);
		ModContent.blockPurpleSlime.setDroppedItem(TinkerCommons.matSlimeBallPurple);
		ModContent.blockMagmaSlime.setDroppedItem(TinkerCommons.matSlimeBallMagma);
	}
	
	public void postInit() {
	}
	
}
