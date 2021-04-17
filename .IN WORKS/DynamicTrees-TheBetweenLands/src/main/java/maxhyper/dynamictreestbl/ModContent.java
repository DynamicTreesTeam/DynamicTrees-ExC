package maxhyper.dynamictreestbl;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestbl.blocks.*;
import maxhyper.dynamictreestbl.items.ItemHearthgroveSeed;
import maxhyper.dynamictreestbl.trees.TreeHearthgrove;
import maxhyper.dynamictreestbl.trees.TreeNibbletwig;
import maxhyper.dynamictreestbl.trees.TreeRubber;
import maxhyper.dynamictreestbl.trees.TreeSap;
import maxhyper.dynamictreestbl.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = DynamicTreesTBL.MODID)
@ObjectHolder(DynamicTreesTBL.MODID)
public class ModContent {

	public static BlockDynamicLeaves betweenlandsLeaves;
	public static BlockBranch rubberBranch;
	public static ILeavesProperties rubberLeavesProperties, sapLeavesProperties, hearthgroveLeavesProperties, nibbletwigLeavesProperties;

	public static Block dynamicWeedwoodRubberTap, dynamicSyrmoriteRubberTap;

	public static BlockRootyWater blockRootyWater, blockRootyWaterSwamp, blockRootyWaterStagnant;
	public static BlockRootyMud blockRootyMud;

	public static Seed hearthgroveSeed;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		rubberBranch = new BlockDynamicBranchRubber();

		blockRootyWater = new BlockRootyWater(false, "rootywater");
		blockRootyWaterSwamp = new BlockRootyWaterTBL("rootywaterswamp", BlockRegistry.SWAMP_WATER);
		blockRootyWaterStagnant = new BlockRootyWaterTBL("rootywaterstagnant", BlockRegistry.STAGNANT_WATER);
		registry.register(blockRootyWater);
		registry.register(blockRootyWaterSwamp);
		registry.register(blockRootyWaterStagnant);
		blockRootyMud = new BlockRootyMud(false);
		registry.register(blockRootyMud);
		betweenlandsLeaves = new BlockDynamicLeavesBetweenlands();
		registry.register(betweenlandsLeaves);

		dynamicWeedwoodRubberTap = new BlockDynamicRubberTap(BlockRegistry.WEEDWOOD_PLANKS.getDefaultState(), 540) {
			@Override protected ItemStack getBucket(boolean withRubber) {
				return new ItemStack(withRubber ? ItemRegistry.BL_BUCKET_RUBBER: ItemRegistry.BL_BUCKET, 1, 0);
			}
		};
		dynamicSyrmoriteRubberTap = new BlockDynamicRubberTap(BlockRegistry.SYRMORITE_BLOCK.getDefaultState(), 260) {
			@Override protected ItemStack getBucket(boolean withRubber) {
				return new ItemStack(withRubber ? ItemRegistry.BL_BUCKET_RUBBER: ItemRegistry.BL_BUCKET, 1, 1);
			}
		};
		registry.register(dynamicWeedwoodRubberTap.setRegistryName(DynamicTreesTBL.MODID, "dynamic_weedwood_rubber_tap"));
		registry.register(dynamicSyrmoriteRubberTap.setRegistryName(DynamicTreesTBL.MODID, "dynamic_syrmorite_rubber_tap"));

		hearthgroveSeed = new ItemHearthgroveSeed("hearthgroveseed");

		rubberLeavesProperties = setUpLeaves(TreeRubber.leavesBlock, "deciduous", 3, 13, true);
		sapLeavesProperties = setUpLeaves(TreeSap.leavesBlock,  new ResourceLocation(DynamicTreesTBL.MODID, "sapTree").toString(), 1, 13);
		hearthgroveLeavesProperties = setUpLeaves(TreeHearthgrove.leavesBlock, "acacia", 4, 13);
		nibbletwigLeavesProperties = setUpLeaves(TreeNibbletwig.leavesBlock, "conifer", 4, 13);

		rubberLeavesProperties.setDynamicLeavesState(betweenlandsLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		sapLeavesProperties.setDynamicLeavesState(betweenlandsLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		hearthgroveLeavesProperties.setDynamicLeavesState(betweenlandsLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		nibbletwigLeavesProperties.setDynamicLeavesState(betweenlandsLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 3));
		betweenlandsLeaves.setProperties(0, rubberLeavesProperties);
		betweenlandsLeaves.setProperties(1, sapLeavesProperties);
		betweenlandsLeaves.setProperties(2, hearthgroveLeavesProperties);
		betweenlandsLeaves.setProperties(3, nibbletwigLeavesProperties);

		TreeFamily rubberTree = new TreeRubber();
		TreeFamily sapTree = new TreeSap();
		TreeFamily hearthgroveTree = new TreeHearthgrove();
		TreeFamily nibbletwigTree = new TreeNibbletwig();

		Collections.addAll(trees, rubberTree, sapTree, hearthgroveTree, nibbletwigTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTBL.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));

		//(soilBlockState.getBlock() instanceof BlockCragrock && soilBlockState.getValue(BlockCragrock.VARIANT) != BlockCragrock.EnumCragrockType.DEFAULT)
		DirtHelper.registerSoil(BlockRegistry.SWAMP_DIRT, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(BlockRegistry.COARSE_SWAMP_DIRT, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(BlockRegistry.SWAMP_GRASS, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(BlockRegistry.DEAD_GRASS, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(BlockRegistry.SLUDGY_DIRT, DirtHelper.MUDLIKE);
		DirtHelper.registerSoil(BlockRegistry.SPREADING_SLUDGY_DIRT, DirtHelper.MUDLIKE);
		DirtHelper.registerSoil(BlockRegistry.SLUDGY_DIRT, DirtHelper.SLIMELIKE);
		DirtHelper.registerSoil(BlockRegistry.SPREADING_SLUDGY_DIRT, DirtHelper.SLIMELIKE);
		DirtHelper.registerSoil(BlockRegistry.MUD, DirtHelper.MUDLIKE);
		DirtHelper.registerSoil(BlockRegistry.SILT, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(BlockRegistry.SILT, DirtHelper.MUDLIKE);
		DirtHelper.registerSoil(BlockRegistry.SILT, DirtHelper.SANDLIKE);
		DirtHelper.registerSoil(BlockRegistry.PEAT, DirtHelper.MUDLIKE);
		DirtHelper.registerSoil(BlockRegistry.SLIMY_DIRT, DirtHelper.SLIMELIKE);
		DirtHelper.registerSoil(BlockRegistry.SLIMY_GRASS, DirtHelper.SLIMELIKE);
		DirtHelper.registerSoil(BlockRegistry.SWAMP_WATER, DirtHelper.WATERLIKE);
		DirtHelper.registerSoil(BlockRegistry.STAGNANT_WATER, DirtHelper.WATERLIKE);
		DirtHelper.registerSoil(blockRootyWater, DirtHelper.WATERLIKE);
		DirtHelper.registerSoil(blockRootyWaterSwamp, DirtHelper.WATERLIKE);
		DirtHelper.registerSoil(blockRootyWaterStagnant, DirtHelper.WATERLIKE);
		DirtHelper.registerSoil(blockRootyMud, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(blockRootyMud, DirtHelper.MUDLIKE);
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit, int smother, int light){
		return setUpLeaves (leavesBlock, cellKit, smother, light,false);
	}
	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit, int smother, int light, boolean foliageColors){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(leavesBlock.getDefaultState(), TreeRegistry.findCellKit(cellKit))
		{
			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
			@Override public int getLightRequirement() { return light; } //Default: 13
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock);
			}
			@Override public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
				if (foliageColors) return super.foliageColorMultiplier(state, world, pos);
				return 0xFFFFFF;
			}
		};
		return leavesProperties;
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		setUpSeedRecipes("rubber", new ItemStack(TreeRubber.saplingBlock, 1, 0));
		setUpSeedRecipes("sap", new ItemStack(TreeSap.saplingBlock, 1, 0));
		setUpSeedRecipes("hearthgrove", new ItemStack(TreeHearthgrove.saplingBlock, 1, 0));
		setUpSeedRecipes("nibbletwig", new ItemStack(TreeNibbletwig.saplingBlock, 1, 0));

	}
	private static void setUpSeedRecipes(String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTBL.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesTBL.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(blockRootyWater, new StateMap.Builder().ignore(BlockRootyWater.LIFE, BlockLiquid.LEVEL).build());
		ModelLoader.setCustomStateMapper(blockRootyWaterSwamp, new StateMap.Builder().ignore(BlockRootyWater.LIFE, BlockLiquid.LEVEL).build());
		ModelLoader.setCustomStateMapper(blockRootyWaterStagnant, new StateMap.Builder().ignore(BlockRootyWater.LIFE, BlockLiquid.LEVEL).build());
		ModelLoader.setCustomStateMapper(blockRootyMud, new StateMap.Builder().ignore(BlockRootyWater.LIFE).build());
		ModelLoader.setCustomStateMapper(betweenlandsLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
	}
}
