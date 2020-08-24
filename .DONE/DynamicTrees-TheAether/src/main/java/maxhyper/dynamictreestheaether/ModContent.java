package maxhyper.dynamictreestheaether;

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
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import com.gildedgames.the_aether.blocks.natural.BlockAetherLeaves;
import com.gildedgames.the_aether.blocks.natural.BlockCrystalLeaves;
import com.gildedgames.the_aether.blocks.natural.BlockHolidayLeaves;
import com.gildedgames.the_aether.blocks.util.EnumCrystalType;
import com.gildedgames.the_aether.blocks.util.EnumHolidayType;
import com.gildedgames.the_aether.blocks.util.EnumLeafType;
import com.gildedgames.the_aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.blocks.BlockDynamicLeavesAether;
import maxhyper.dynamictreestheaether.blocks.BlockDynamicLeavesCrystal;
import maxhyper.dynamictreestheaether.trees.ALTreeCrystal;
import maxhyper.dynamictreestheaether.trees.ALTreeGoldenOak;
import maxhyper.dynamictreestheaether.trees.ALTreeHoliday;
import maxhyper.dynamictreestheaether.trees.ALTreeSkyroot;
import maxhyper.dynamictreestheaether.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Items;
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

@Mod.EventBusSubscriber(modid = DynamicTreesTheAether.MODID)
@ObjectHolder(DynamicTreesTheAether.MODID)
public class ModContent {

	public static BlockDynamicLeaves crystalLeaves, aetherLeaves;
	public static ILeavesProperties skyrootLeavesProperties, goldenOakLeavesProperties, crystalLeavesProperties, holidayLeavesProperties,
			crystalFruitLeavesProperties, holidayDecorLeavesProperties;
	public static BlockFruit blockWhiteApple;
	public static BlockRootyDirt rootyDirtAether;

	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		blockWhiteApple = (new BlockFruit("fruitwhiteapple"));
		registry.register(blockWhiteApple);
		crystalLeaves = new BlockDynamicLeavesCrystal();
		registry.register(crystalLeaves);
		aetherLeaves = new BlockDynamicLeavesAether();
		registry.register(aetherLeaves);
		rootyDirtAether = new BlockRootyDirt("rootydirtaether",false);
		registry.register(rootyDirtAether);

		skyrootLeavesProperties = setUpLeaves(
				ALTreeSkyroot.leavesBlock,
				ALTreeSkyroot.leavesBlock.getDefaultState().withProperty(BlockAetherLeaves.leaf_type, EnumLeafType.Green),
				"deciduous",
				4, 13);
		goldenOakLeavesProperties = setUpLeaves(
				ALTreeGoldenOak.leavesBlock,
				ALTreeGoldenOak.leavesBlock.getDefaultState().withProperty(BlockAetherLeaves.leaf_type, EnumLeafType.Golden),
				"deciduous",
				4, 13);
		crystalLeavesProperties = setUpLeaves(
				ALTreeCrystal.leavesBlock,
				ALTreeCrystal.leavesBlock.getDefaultState().withProperty(BlockCrystalLeaves.leaf_type, EnumCrystalType.Crystal),
				"conifer",
				4, 13);
		crystalFruitLeavesProperties = setUpLeaves(
				ALTreeCrystal.leavesBlock,
				ALTreeCrystal.leavesBlock.getDefaultState().withProperty(BlockCrystalLeaves.leaf_type, EnumCrystalType.Crystal_Fruited),
				"conifer",
				4, 13);
		holidayLeavesProperties = setUpLeaves(
				ALTreeHoliday.leavesBlock,
				ALTreeHoliday.leavesBlock.getDefaultState().withProperty(BlockHolidayLeaves.leaf_type, EnumHolidayType.Holiday_Leaves),
				"conifer",
				4, 4);
		holidayDecorLeavesProperties = setUpLeaves(
				ALTreeHoliday.leavesBlock,
				ALTreeHoliday.leavesBlock.getDefaultState().withProperty(BlockHolidayLeaves.leaf_type, EnumHolidayType.Decorated_Leaves),
				"conifer",
				4, 4);

		crystalLeavesProperties.setDynamicLeavesState(crystalLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		crystalFruitLeavesProperties.setDynamicLeavesState(crystalLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		skyrootLeavesProperties.setDynamicLeavesState(aetherLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		goldenOakLeavesProperties.setDynamicLeavesState(aetherLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		holidayLeavesProperties.setDynamicLeavesState(aetherLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		holidayDecorLeavesProperties.setDynamicLeavesState(aetherLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 3));
		crystalLeaves.setProperties(0, crystalLeavesProperties);
		crystalLeaves.setProperties(1, crystalFruitLeavesProperties);
		aetherLeaves.setProperties(0, skyrootLeavesProperties);
		aetherLeaves.setProperties(1, goldenOakLeavesProperties);
		aetherLeaves.setProperties(2, holidayLeavesProperties);
		aetherLeaves.setProperties(3, holidayDecorLeavesProperties);

		TreeFamily skyrootTree = new ALTreeSkyroot();
		TreeFamily goldenOakTree = new ALTreeGoldenOak();
		TreeFamily crystalTree = new ALTreeCrystal();
		TreeFamily holidayTree = new ALTreeHoliday();
		Collections.addAll(trees, skyrootTree, goldenOakTree, crystalTree, holidayTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTheAether.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	private static ILeavesProperties setUpLeaves (Block leavesBlock, IBlockState leavesState, String cellKit, int smother, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesState,
				new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState)),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
			@Override public int getLightRequirement() { return light; } //Default: 13
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState));
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

		setUpSeedRecipes("skyroot", new ItemStack(ALTreeSkyroot.saplingBlock));
		setUpSeedRecipes("goldenoak", new ItemStack(ALTreeGoldenOak.saplingBlock));
	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesTheAether.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(crystalLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(aetherLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(rootyDirtAether, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
	}
}
