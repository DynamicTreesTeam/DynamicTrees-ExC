package maxhyper.dynamictreestf;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestf.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.BlockTFMagicLog;
import twilightforest.enums.MagicWoodVariant;

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = DynamicTreesTF.MODID)
@ObjectHolder(DynamicTreesTF.MODID)
public class ModContent {

	public static ILeavesProperties sicklyTwilightOakLeavesProperties, canopyLeavesProperties, mangroveLeavesProperties,
			darkwoodLeavesProperties, robustTwilightOakLeavesProperties, rainbowOakLeavesProperties,
	timeLeavesProperties,transformationLeavesProperties, minersLeavesProperties, sortingLeavesProperties;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		sicklyTwilightOakLeavesProperties = setUpLeaves(TreeSicklyTwilightOak.leavesBlock, TreeSicklyTwilightOak.leavesState, "deciduous");
		canopyLeavesProperties = setUpLeaves(TreeCanopy.leavesBlock, TreeCanopy.leavesState, "deciduous");
		mangroveLeavesProperties = setUpLeaves(TreeMangrove.leavesBlock, TreeMangrove.leavesState, "deciduous");
		darkwoodLeavesProperties = setUpLeaves(TreeDarkwood.leavesBlock, TreeDarkwood.leavesState, "deciduous");
		robustTwilightOakLeavesProperties = setUpLeaves(TreeRobustTwilightOak.leavesBlock, TreeRobustTwilightOak.leavesState, "deciduous");
		rainbowOakLeavesProperties = setUpLeaves(TreeRainbowOak.leavesBlock, TreeRainbowOak.leavesState, "deciduous");

		timeLeavesProperties = setUpLeaves(TreeMagicTime.leavesBlock, TreeMagicTime.leavesState, 0, "deciduous");
		transformationLeavesProperties = setUpLeaves(TreeMagicTransformation.leavesBlock, TreeMagicTransformation.leavesState, 1, "deciduous");
		minersLeavesProperties = setUpLeaves(TreeMagicMiners.leavesBlock, TreeMagicMiners.leavesState, 2,"deciduous");
		sortingLeavesProperties = setUpLeaves(TreeMagicSorting.leavesBlock, TreeMagicSorting.leavesState, 3, "deciduous");

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 0, sicklyTwilightOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 1, robustTwilightOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 2, rainbowOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 4, canopyLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 5, mangroveLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 6, darkwoodLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 8, timeLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 9, transformationLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 10, minersLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTF.MODID, 11, sortingLeavesProperties);


		TreeFamily sicklytwilightOakTree = new TreeSicklyTwilightOak();
		TreeFamily canopyTree = new TreeCanopy();
		TreeFamily mangroveTree = new TreeMangrove();
		TreeFamily darkwoodTree = new TreeDarkwood();
		TreeFamily robustTwilightOakTree = new TreeRobustTwilightOak();
		TreeFamily timeTree = new TreeMagicTime();
		TreeFamily transformationTree = new TreeMagicTransformation();
		TreeFamily minersTree = new TreeMagicMiners();
		TreeFamily sortingTree = new TreeMagicSorting();
		TreeFamily rainbowOakTree = new TreeRainbowOak();

		Collections.addAll(trees, sicklytwilightOakTree, canopyTree, mangroveTree, darkwoodTree, robustTwilightOakTree, timeTree, transformationTree, minersTree, sortingTree, rainbowOakTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTF.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	private static ILeavesProperties setUpLeaves (Block leavesBlock, IBlockState leavesState, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesState,
				new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState)),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState));
			}
		};
		return leavesProperties;
	}
	private static ILeavesProperties setUpLeaves (Block leavesBlock, IBlockState leavesState, int leavesMeta, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesState,
				new ItemStack(leavesBlock, 1, leavesMeta),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesMeta);
			}
		};
		return leavesProperties;
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		setUpSeedRecipes("sicklyTwilightOak", new ItemStack(TreeSicklyTwilightOak.saplingBlock, 1, TreeSicklyTwilightOak.saplingMeta));
		setUpSeedRecipes("canopy", new ItemStack(TreeCanopy.saplingBlock, 1, TreeCanopy.saplingMeta));
		setUpSeedRecipes("mangrove", new ItemStack(TreeMangrove.saplingBlock, 1, TreeMangrove.saplingMeta));
		setUpSeedRecipes("darkwood", new ItemStack(TreeDarkwood.saplingBlock, 1, TreeDarkwood.saplingMeta));
		setUpSeedRecipes("robustTwilightOak", new ItemStack(TreeRobustTwilightOak.saplingBlock, 1, TreeRobustTwilightOak.saplingMeta));
		setUpSeedRecipes("treeOfTime", new ItemStack(TreeMagicTime.saplingBlock, 1, TreeMagicTime.saplingMeta));
		setUpSeedRecipes("treeOfTransformation", new ItemStack(TreeMagicTransformation.saplingBlock, 1, TreeMagicTransformation.saplingMeta));
		setUpSeedRecipes("minersTree", new ItemStack(TreeMagicMiners.saplingBlock, 1, TreeMagicMiners.saplingMeta));
		setUpSeedRecipes("sortingTree", new ItemStack(TreeMagicSorting.saplingBlock, 1, TreeMagicSorting.saplingMeta));
		setUpSeedRecipes("rainbowOak", new ItemStack(TreeRainbowOak.saplingBlock, 1, TreeRainbowOak.saplingMeta));

	}
	private static void setUpSeedRecipes(String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTF.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesTF.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

	}
}
