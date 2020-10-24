package maxhyper.dynamictreesplants;

import java.util.ArrayList;
import java.util.Collections;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import maxhyper.dynamictreesplants.blocks.BlockDynamicLeavesSpecial;
import maxhyper.dynamictreesplants.blocks.BlockRootyCrystal;
import maxhyper.dynamictreesplants.blocks.BlockRootyNether;
import maxhyper.dynamictreesplants.trees.*;
import maxhyper.dynamictreesplants.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.CryptManager;
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
import shadows.plants2.init.ModRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesPlants.MODID)
@ObjectHolder(DynamicTreesPlants.MODID)
public class ModContent {

	public static BlockRooty rootyNetherDirt, rootyCrystalDirt;
	public static BlockDynamicLeaves specialLeaves;
	public static ILeavesProperties murrayPineLeavesProperties, incenseCedarLeavesProperties, brazilianPineLeavesProperties,
			blackKauriLeavesProperties, crystalLeavesProperties, darkCrystalLeavesProperties, ashenLeavesProperties, blazingLeavesProperties;

	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	public static String CRYSTALLIKE = "crystallike";

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		DirtHelper.createNewAdjective(CRYSTALLIKE);

		IForgeRegistry<Block> registry = event.getRegistry();

		rootyNetherDirt = new BlockRootyNether(false);
		registry.register(rootyNetherDirt);
		rootyCrystalDirt = new BlockRootyCrystal(false);
		registry.register(rootyCrystalDirt);

		blackKauriLeavesProperties = setUpLeaves(TreeBlackKauri.leavesBlock, "dynamictreesplants:crystal", TreeBlackKauri.meta);
		brazilianPineLeavesProperties = setUpLeaves(TreeBrazilianPine.leavesBlock, "conifer", TreeBrazilianPine.meta);
		incenseCedarLeavesProperties = setUpLeaves(TreeIncenseCedar.leavesBlock, "conifer", TreeIncenseCedar.meta);
		murrayPineLeavesProperties = setUpLeaves(TreeMurrayPine.leavesBlock, "dynamictreesplants:crystal", TreeMurrayPine.meta, 8);

		specialLeaves = new BlockDynamicLeavesSpecial();
		registry.register(specialLeaves);

		ashenLeavesProperties = setUpLeaves(TreeAshen.leavesBlock, "dynamictreesplants:nether", TreeAshen.meta, 0);
		blazingLeavesProperties = setUpLeaves(TreeBlazing.leavesBlock, "dynamictreesplants:nether", TreeBlazing.meta, 0);
		crystalLeavesProperties = setUpLeaves(TreeCrystal.leavesBlock, "dynamictreesplants:crystal", TreeCrystal.meta);
		darkCrystalLeavesProperties = setUpLeaves(TreeDarkCrystal.leavesBlock, "dynamictreesplants:crystal", TreeCrystal.meta);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesPlants.MODID, 0, blackKauriLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesPlants.MODID, 1, brazilianPineLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesPlants.MODID, 2, incenseCedarLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesPlants.MODID, 3, murrayPineLeavesProperties);

		ashenLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		specialLeaves.setProperties(0, ashenLeavesProperties);
		blazingLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		specialLeaves.setProperties(1, blazingLeavesProperties);
		crystalLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		specialLeaves.setProperties(2, crystalLeavesProperties);
		darkCrystalLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 3));
		specialLeaves.setProperties(3, darkCrystalLeavesProperties);

		TreeFamily blackKauri = new TreeBlackKauri();
		TreeFamily brazilianPine = new TreeBrazilianPine();
		TreeFamily incenseCedar = new TreeIncenseCedar();
		TreeFamily murrayPine = new TreeMurrayPine();
		TreeFamily ashenTree = new TreeAshen();
		TreeFamily blazingTree = new TreeBlazing();
		TreeFamily crystalTree = new TreeCrystal();
		TreeFamily darkCrystalTree = new TreeDarkCrystal();

		Collections.addAll(trees, blackKauri, brazilianPine, incenseCedar, murrayPine, ashenTree, blazingTree, crystalTree, darkCrystalTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesPlants.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));

		DirtHelper.registerSoil(rootyNetherDirt, DirtHelper.NETHERLIKE);
		DirtHelper.registerSoil(rootyCrystalDirt, CRYSTALLIKE);
		DirtHelper.registerSoil(ModRegistry.GROUNDCOVER, CRYSTALLIKE);
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit, int meta, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getDefaultState(),
				new ItemStack(leavesBlock, 1, meta),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, meta);
			}

			@Override
			public int getLightRequirement() {
				return light;
			}
		};
		return leavesProperties;
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit, int meta){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getDefaultState(),
				new ItemStack(leavesBlock, 1, meta),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, meta);
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

		setUpSeedRecipes("blackkauri", new ItemStack(TreeBlackKauri.saplingBlock, 1, TreeBlackKauri.meta));
		setUpSeedRecipes("brazilianpine", new ItemStack(TreeBrazilianPine.saplingBlock, 1, TreeBrazilianPine.meta));
		setUpSeedRecipes("incensecedar", new ItemStack(TreeIncenseCedar.saplingBlock, 1, TreeIncenseCedar.meta));
		setUpSeedRecipes("murraypine", new ItemStack(TreeMurrayPine.saplingBlock, 1, TreeMurrayPine.meta));
		setUpSeedRecipes("ashen", new ItemStack(TreeAshen.saplingBlock, 1, TreeAshen.meta));
		setUpSeedRecipes("blazing", new ItemStack(TreeBlazing.saplingBlock, 1, TreeBlazing.meta));
		setUpSeedRecipes("crystal", new ItemStack(TreeCrystal.saplingBlock, 1, TreeCrystal.meta));

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesPlants.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(specialLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(rootyNetherDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
		ModelLoader.setCustomStateMapper(rootyCrystalDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
	}
}
