package maxhyper.dynamictreestconstruct.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import maxhyper.dynamictreestconstruct.ModContent;
import maxhyper.dynamictreestconstruct.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreestconstruct.growth.SlimeGrowthLogic;
import maxhyper.dynamictreestconstruct.worldgen.DynamicMagmaSlimeIslandGenerator;
import maxhyper.dynamictreestconstruct.worldgen.DynamicSlimeIslandGenerator;
import maxhyper.dynamictreestconstruct.worldgen.WorldEvents;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import slimeknights.tconstruct.shared.TinkerCommons;

public class CommonProxy {

	public void preInit() {
		TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "slime"), new SlimeGrowthLogic());
	}

	public void init() {

		ModContent.genSlimeIslands = slimeknights.tconstruct.common.config.Config.genSlimeIslands;
		slimeknights.tconstruct.common.config.Config.genSlimeIslands = false;
		GameRegistry.registerWorldGenerator(new DynamicSlimeIslandGenerator(), 21);
		GameRegistry.registerWorldGenerator(new DynamicMagmaSlimeIslandGenerator(), 21);

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
		MinecraftForge.EVENT_BUS.register(new WorldEvents());
	}
	
}
