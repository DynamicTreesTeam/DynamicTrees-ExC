package maxhyper.dynamictreesatum;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.teammetallurgy.atum.init.AtumItems;
import maxhyper.dynamictreesatum.blocks.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesatum.trees.A2TreeDeadPalm;
import maxhyper.dynamictreesatum.trees.A2TreeDeadTree;
import maxhyper.dynamictreesatum.trees.A2TreePalm;
import maxhyper.dynamictreesatum.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = DynamicTreesAtum.MODID)
@ObjectHolder(DynamicTreesAtum.MODID)
public class ModContent {

	public static BlockDynamicLeaves palmFrondLeaves;
	public static ILeavesProperties palmLeavesProperties, deadPalmLeavesProperties, nullLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		palmFrondLeaves = new BlockDynamicLeavesPalm("leaves_palm");
		registry.register(palmFrondLeaves);

		palmLeavesProperties = new LeavesProperties(
				A2TreePalm.leavesBlock.getDefaultState(),
				new ItemStack(A2TreePalm.leavesBlock),
				TreeRegistry.findCellKit("palm") ) {
			@Override public boolean appearanceChangesWithHydro() { return true; }
		};
		deadPalmLeavesProperties = new LeavesProperties(
				A2TreeDeadPalm.leavesBlock.getDefaultState(),
				new ItemStack(A2TreeDeadPalm.leavesBlock),
				TreeRegistry.findCellKit("palm") ) {
			@Override public boolean appearanceChangesWithHydro() { return true; }
		};
		nullLeavesProperties = new LeavesProperties(null, ItemStack.EMPTY, TreeRegistry.findCellKit("bare") ) {};

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesAtum.MODID, 0, nullLeavesProperties);

		palmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		palmFrondLeaves.setProperties(0, palmLeavesProperties);
		deadPalmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		palmFrondLeaves.setProperties(1, deadPalmLeavesProperties);

		TreeFamily palmTree = new A2TreePalm();
		TreeFamily palmDeadTree = new A2TreeDeadPalm();
		TreeFamily deadTree = new A2TreeDeadTree();
		Collections.addAll(trees, palmTree, palmDeadTree, deadTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesAtum.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));

		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("atum", "fertile_soil")), DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("atum", "limestone_gravel")), DirtHelper.GRAVELLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("atum", "sand")), DirtHelper.SANDLIKE);
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		setUpSeedRecipes("palm", new ItemStack(A2TreePalm.saplingBlock));

		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesAtum.MODID, "palm"));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesAtum.MODID, "dateSeed"), null, treeSeed, Ingredient.fromItem(AtumItems.DATE));


	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesAtum.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (TreeFamily tree : trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		LeavesPaging.getLeavesMapForModId(DynamicTreesAtum.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(palmFrondLeaves, new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(nullLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockLeaves.DECAYABLE).build());
	}
}
