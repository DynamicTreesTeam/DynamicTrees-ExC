package maxhyper.dynamictreestheaether2;

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

import maxhyper.dynamictreestheaether2.blocks.BlockDynamicLeavesAether;
import maxhyper.dynamictreestheaether2.trees.*;
import maxhyper.dynamictreestheaether2.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesTheAether2.MODID)
@ObjectHolder(DynamicTreesTheAether2.MODID)
public class ModContent {

	public static BlockDynamicLeaves specialLeaves, skyrootLeaves, wisprootLeaves, greatrootLeaves;
	public static ILeavesProperties greenSkyrootLeavesProperties, blueSkyrootLeavesProperties, darkblueSkyrootLeavesProperties,
			greenWisprootLeavesProperties, blueWisprootLeavesProperties, darkblueWisprootLeavesProperties,
			greenGreatrootLeavesProperties, blueGreatrootLeavesProperties, darkblueGreatrootLeavesProperties,
			amberootLeavesProperties, holidayLeavesProperties, holidayDecorLeavesProperties, therawoodLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	public static String AETHERLIKE = "aetherlike";
	public static String THERALIKE = "theralike";

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		DirtHelper.createNewAdjective(AETHERLIKE);
		DirtHelper.createNewAdjective(THERALIKE);

		IForgeRegistry<Block> registry = event.getRegistry();

		specialLeaves = new BlockDynamicLeavesAether("leaves_special"){
			@Override
			public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
				if (state.getValue(TREE) == 2){
					return 15;
				}
				return 0;
			}
		};
		registry.register(specialLeaves);
		skyrootLeaves = new BlockDynamicLeavesAether("leaves_skyroot");
		registry.register(skyrootLeaves);
		wisprootLeaves = new BlockDynamicLeavesAether("leaves_wisproot");
		registry.register(wisprootLeaves);
		greatrootLeaves = new BlockDynamicLeavesAether("leaves_greatroot");
		registry.register(greatrootLeaves);

		greenSkyrootLeavesProperties = setUpLeaves(TreeSkyroot.leavesBlockGreen.getDefaultState(), "conifer", 4, 13);
		greenWisprootLeavesProperties = setUpLeaves(TreeWisproot.leavesBlockGreen.getDefaultState(), "conifer", 4, 13);
		greenGreatrootLeavesProperties = setUpLeaves(TreeGreatroot.leavesBlockGreen.getDefaultState(), "conifer", 4, 13);

		blueSkyrootLeavesProperties = setUpLeaves(TreeSkyroot.leavesBlockBlue.getDefaultState(), "conifer", 4, 13);
		blueWisprootLeavesProperties = setUpLeaves(TreeWisproot.leavesBlockBlue.getDefaultState(), "conifer", 4, 13);
		blueGreatrootLeavesProperties = setUpLeaves(TreeGreatroot.leavesBlockBlue.getDefaultState(), "conifer", 4, 13);

		darkblueSkyrootLeavesProperties = setUpLeaves(TreeSkyroot.leavesBlockDarkBlue.getDefaultState(), "conifer", 4, 13);
		darkblueWisprootLeavesProperties = setUpLeaves(TreeWisproot.leavesBlockDarkBlue.getDefaultState(), "conifer", 4, 13);
		darkblueGreatrootLeavesProperties = setUpLeaves(TreeGreatroot.leavesBlockDarkBlue.getDefaultState(), "conifer", 4, 13);

		amberootLeavesProperties = setUpLeaves(TreeAmberoot.leavesBlock.getDefaultState(), "conifer", 2, 10);
		holidayLeavesProperties = setUpLeaves(TreeHoliday.leavesBlock.getDefaultState(), "conifer", 2, 8);
		holidayDecorLeavesProperties = setUpLeaves(TreeHoliday.leavesBlock2.getDefaultState(), "conifer", 2, 8);
		therawoodLeavesProperties = setUpLeaves(TreeTherawood.leavesBlock.getDefaultState(), "conifer", 4, 13);

		amberootLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		holidayLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		holidayDecorLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		therawoodLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 3));
		specialLeaves.setProperties(0, amberootLeavesProperties);
		specialLeaves.setProperties(1, holidayLeavesProperties);
		specialLeaves.setProperties(2, holidayDecorLeavesProperties);
		specialLeaves.setProperties(3, therawoodLeavesProperties);

		greenSkyrootLeavesProperties.setDynamicLeavesState(skyrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		skyrootLeaves.setProperties(0, greenSkyrootLeavesProperties);
		blueSkyrootLeavesProperties.setDynamicLeavesState(skyrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		skyrootLeaves.setProperties(1, blueSkyrootLeavesProperties);
		darkblueSkyrootLeavesProperties.setDynamicLeavesState(skyrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		skyrootLeaves.setProperties(2, darkblueSkyrootLeavesProperties);

		greenWisprootLeavesProperties.setDynamicLeavesState(wisprootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		wisprootLeaves.setProperties(0, greenWisprootLeavesProperties);
		blueWisprootLeavesProperties.setDynamicLeavesState(wisprootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		wisprootLeaves.setProperties(1, blueWisprootLeavesProperties);
		darkblueWisprootLeavesProperties.setDynamicLeavesState(wisprootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		wisprootLeaves.setProperties(2, darkblueWisprootLeavesProperties);

		greenGreatrootLeavesProperties.setDynamicLeavesState(greatrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		greatrootLeaves.setProperties(0, greenGreatrootLeavesProperties);
		blueGreatrootLeavesProperties.setDynamicLeavesState(greatrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		greatrootLeaves.setProperties(1, blueGreatrootLeavesProperties);
		darkblueGreatrootLeavesProperties.setDynamicLeavesState(greatrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		greatrootLeaves.setProperties(2, darkblueGreatrootLeavesProperties);

		TreeFamily skyrootTree = new TreeSkyroot();
		TreeFamily wisprootTree = new TreeWisproot();
		TreeFamily greatrootTree = new TreeGreatroot();
		TreeFamily goldenOakTree = new TreeAmberoot();
		TreeFamily holidayTree = new TreeHoliday();
		TreeFamily therawoodTree = new TreeTherawood();
		Collections.addAll(trees, skyrootTree, wisprootTree, greatrootTree, goldenOakTree, holidayTree, therawoodTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTheAether2.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));

		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:aether_grass")), DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:aether_dirt")), DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:aether_grass")), AETHERLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:aether_dirt")), AETHERLIKE);

		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:thera_grass")), DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:thera_dirt")), DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:thera_grass")), AETHERLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:thera_dirt")), AETHERLIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:thera_grass")), THERALIKE);
		DirtHelper.registerSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("aether:thera_dirt")), THERALIKE);
	}

	private static ILeavesProperties setUpLeaves (IBlockState leavesState, String cellKit, int smother, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesState,
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
			@Override public int getLightRequirement() { return light; } //Default: 13
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return stateToStack(leavesState);
			}
		};
		return leavesProperties;
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		//setUpSeedRecipes("greenskyroot", stateToStack(TreeSkyroot.saplingBlock.getDefaultState()));
		//setUpSeedRecipes("blueskyroot", stateToStack(TreeSkyroot.saplingBlock.getDefaultState()));
		//setUpSeedRecipes("darkblueskyroot", stateToStack(TreeSkyroot.saplingBlock.getDefaultState()));
		//setUpSeedRecipes("greenwisproot", stateToStack(TreeWisproot.saplingBlock.getDefaultState()));

		//setUpSeedRecipes("greengreatroot", stateToStack(TreeGreatroot.saplingBlock.getDefaultState()));

		setUpSeedRecipes("amberoot", stateToStack(TreeAmberoot.saplingBlock.getDefaultState()));
		setUpSeedRecipes("holiday", stateToStack(TreeHoliday.saplingBlock.getDefaultState()));
	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether2.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}
	private static ItemStack stateToStack (IBlockState state){
		return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (TreeFamily tree : trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		LeavesPaging.getLeavesMapForModId(DynamicTreesTheAether2.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(specialLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(skyrootLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(wisprootLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(greatrootLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());

	}
}
