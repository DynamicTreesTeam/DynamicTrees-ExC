package maxhyper.dynamictreesttf;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
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
import maxhyper.dynamictreesttf.blocks.*;
import maxhyper.dynamictreesttf.items.ItemMangroveSeed;
import maxhyper.dynamictreesttf.trees.*;
import maxhyper.dynamictreesttf.trees.species.SpeciesBirchShadow;
import maxhyper.dynamictreesttf.trees.species.SpeciesOakShadow;
import maxhyper.dynamictreesttf.trees.species.SpeciesOakSpooky;
import maxhyper.dynamictreesttf.trees.species.SpeciesSpruceHuge;
import maxhyper.dynamictreesttf.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
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
import twilightforest.block.BlockTFCritter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(modid = DynamicTreesTTF.MODID)
@ObjectHolder(DynamicTreesTTF.MODID)
public class ModContent {

	public static ILeavesProperties sicklyTwilightOakLeavesProperties, canopyLeavesProperties, spookyCanopyLeavesProperties, mangroveLeavesProperties,
			darkwoodLeavesProperties, rainbowOakLeavesProperties, robustTwilightOakLeavesProperties,
	timeLeavesProperties, transformationLeavesProperties, minersLeavesProperties, sortingLeavesProperties;

	public static ILeavesProperties shadowOakLeavesProperties, shadowBirchLeavesProperties;

	public static BlockTFCritter dynamicCicada, dynamicFirefly;
	public static BlockDynamicTwilightRoots undergroundRoot, undergroundRootExposed;
	@Deprecated
	public static BlockDynamicTwilightRoots undergroundMangroveRoot;

	public static BlockBranch minerCoreBranch, minerCoreBranchOff,
			sortingCoreBranch, sortingCoreBranchOff, transformCoreBranch, transformCoreBranchOff, timeCoreBranch, timeCoreBranchOff;

	public static BlockRootyWater blockRootyWater;

	public static Species hugeSpruce, spookyOak, shadowOak, shadowBirch;

	public static Seed mangroveSeed;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		minerCoreBranch = new BlockBranchMagicCore("minersTreeCoreBranch", BlockBranchMagicCore.Types.MINE){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicMiners.branch.getPickBlock(state, target, world, pos, player);
			}
		};
		minerCoreBranchOff = new BlockBranchMagicCore("minersTreeCoreBranchOff", BlockBranchMagicCore.Types.MINE){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicMiners.branch.getPickBlock(state, target, world, pos, player);
			}
		};
		sortingCoreBranch = new BlockBranchMagicCore("sortingTreeCoreBranch", BlockBranchMagicCore.Types.SORT){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicSorting.branch.getPickBlock(state, target, world, pos, player);
			}
		};
		sortingCoreBranchOff = new BlockBranchMagicCore("sortingTreeCoreBranchOff", BlockBranchMagicCore.Types.SORT){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicSorting.branch.getPickBlock(state, target, world, pos, player);
			}
		};
		timeCoreBranch = new BlockBranchMagicCoreThick("treeOfTimeCoreBranch", BlockBranchMagicCore.Types.TIME){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicTime.branch.getPickBlock(state, target, world, pos, player);
			}
		};
		timeCoreBranchOff = new BlockBranchMagicCoreThick("treeOfTimeCoreBranchOff", BlockBranchMagicCore.Types.TIME){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicTime.branch.getPickBlock(state, target, world, pos, player);
			}
		};
		transformCoreBranch = new BlockBranchMagicCore("treeOfTransformationCoreBranch", BlockBranchMagicCore.Types.TRANS){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicTransformation.branch.getPickBlock(state, target, world, pos, player);
			}
		};
		transformCoreBranchOff = new BlockBranchMagicCore("treeOfTransformationCoreBranchOff", BlockBranchMagicCore.Types.TRANS){
			@Override public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				return TreeMagicTransformation.branch.getPickBlock(state, target, world, pos, player);
			}
		};

		((BlockBranchMagicCore) minerCoreBranch).setOffBlock((BlockBranchMagicCore)minerCoreBranchOff);
		((BlockBranchMagicCore) sortingCoreBranch).setOffBlock((BlockBranchMagicCore)sortingCoreBranchOff);
		((BlockBranchMagicCoreThick) timeCoreBranch).setOffBlock((BlockBranchMagicCoreThick)timeCoreBranchOff);
		((BlockBranchMagicCore) transformCoreBranch).setOffBlock((BlockBranchMagicCore)transformCoreBranchOff);

		blockRootyWater = new BlockRootyWater(false);
		registry.register(blockRootyWater);

		undergroundRoot = new BlockDynamicTwilightRoots();
		registry.register(undergroundRoot.setRegistryName(DynamicTreesTTF.MODID,"underground_roots"));

		{ //Depricated
			undergroundMangroveRoot = new BlockDynamicTwilightRoots(){
				@Override
				public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
					if (state.getValue(GRASSY)){
						worldIn.setBlockState(pos, Blocks.GRASS.getDefaultState());
						if (worldIn.isAirBlock(pos.up()) && worldIn.rand.nextFloat() < 0.5){
							TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "mangrove")).plantSapling(worldIn, pos.up());
						}
					} else {
						worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
					}
				}
				@Override public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
					super.onBlockDestroyedByPlayer(world, pos, state);
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
			};
			registry.register(undergroundMangroveRoot.setRegistryName(DynamicTreesTTF.MODID,"underground_roots_mangrove"));
		}

		undergroundRootExposed = new BlockDynamicTwilightRootsExposed();
		registry.register(undergroundRootExposed.setRegistryName(DynamicTreesTTF.MODID,"underground_roots_exposed"));

		dynamicCicada = new BlockDynamicTFCicada();
		registry.register(dynamicCicada.setRegistryName(DynamicTreesTTF.MODID, "dynamic_cicada"));
		dynamicFirefly = new BlockDynamicTFFirefly();
		registry.register(dynamicFirefly.setRegistryName(DynamicTreesTTF.MODID, "dynamic_firefly"));

		sicklyTwilightOakLeavesProperties = setUpLeaves(TreeTwilightOak.leavesState, "deciduous");
		canopyLeavesProperties = setUpLeaves(TreeCanopy.leavesState, "acacia");
		spookyCanopyLeavesProperties  = new LeavesProperties(
				TreeCanopy.leavesBlock.getDefaultState(),
				ItemStack.EMPTY,
				TreeRegistry.findCellKit("bare") ) {
		};
		mangroveLeavesProperties = setUpLeaves(TreeMangrove.leavesState, "acacia");
		darkwoodLeavesProperties = setUpLeaves(TreeDarkwood.leavesState, "acacia", 0, 0);
		robustTwilightOakLeavesProperties = setUpLeaves(TreeTwilightOak.leavesState, "deciduous",0, 13);
		rainbowOakLeavesProperties = setUpLeaves(TreeRainbowOak.leavesState, "deciduous");

		timeLeavesProperties = setUpLeaves(TreeMagicTime.leavesBlock, 0, "deciduous");
		transformationLeavesProperties = setUpLeaves(TreeMagicTransformation.leavesBlock, 1, "acacia");
		minersLeavesProperties = setUpLeaves(TreeMagicMiners.leavesBlock, 2,"deciduous", 4, 5);
		sortingLeavesProperties = setUpLeaves(TreeMagicSorting.leavesBlock, 3, "deciduous", 8, 13);

		shadowOakLeavesProperties = setUpLeaves(Blocks.LEAVES.getStateFromMeta(0), "deciduous", 4, 0);
		shadowBirchLeavesProperties = setUpLeaves(Blocks.LEAVES.getStateFromMeta(1), "deciduous", 4, 0);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 0, sicklyTwilightOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 1, robustTwilightOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 2, rainbowOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 3, shadowOakLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 4, canopyLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 5, mangroveLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 6, shadowBirchLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 7, spookyCanopyLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 8, timeLeavesProperties);
		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 9, );
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 10, minersLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTTF.MODID, 11, sortingLeavesProperties);

		TreeFamily oakTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).getFamily();
		TreeFamily birchTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "birch")).getFamily();
		TreeFamily spruceTree = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "spruce")).getFamily();

		mangroveSeed = new ItemMangroveSeed("mangroveseed");

		Species hugeSpruce = new SpeciesSpruceHuge(spruceTree);
		Species spookyOak = new SpeciesOakSpooky(oakTree);
		Species shadowOak = new SpeciesOakShadow(oakTree);
		Species shadowBirch = new SpeciesBirchShadow(birchTree);

		Species.REGISTRY.registerAll(hugeSpruce, spookyOak, shadowOak, shadowBirch);

		TreeFamily twilightOakTree = new TreeTwilightOak();
		TreeFamily canopyTree = new TreeCanopy();
		TreeFamily mangroveTree = new TreeMangrove();
		TreeFamily darkwoodTree = new TreeDarkwood();
		TreeFamily timeTree = new TreeMagicTime();
		TreeFamily transformationTree = new TreeMagicTransformation();
		TreeFamily minersTree = new TreeMagicMiners();
		TreeFamily sortingTree = new TreeMagicSorting();
		TreeFamily rainbowOakTree = new TreeRainbowOak();

		Collections.addAll(trees, twilightOakTree, canopyTree, mangroveTree, darkwoodTree, timeTree, transformationTree, minersTree, sortingTree, rainbowOakTree);

		//Depricated code, will be removed in future version
		{
			TreeFamily robustTwilightOakTree = new TreeRobustTwilightOak();
			TreeFamily sicklytwilightOakTree = new TreeSicklyTwilightOak();
			Collections.addAll(trees, sicklytwilightOakTree, robustTwilightOakTree);
		}

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTTF.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));

		DirtHelper.registerSoil(undergroundRoot, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(blockRootyWater, DirtHelper.WATERLIKE);
	}

	private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit, int smother, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(leavesBlock.getStateFromMeta(leavesMeta), TreeRegistry.findCellKit(cellKit))
		{
			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
			@Override public int getLightRequirement() { return light; } //Default: 13
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesMeta);
			}
		};
		return leavesProperties;
	}
	private static ILeavesProperties setUpLeaves (IBlockState leavesState, String cellKit, int smother, int light){
		return setUpLeaves(leavesState.getBlock(), leavesState.getBlock().getMetaFromState(leavesState), cellKit, smother, light);
	}
	private static ILeavesProperties setUpLeaves (IBlockState leavesState, String cellKit){
		return setUpLeaves(leavesState, cellKit, 4, 13);
	}
	private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit){
		return setUpLeaves(leavesBlock.getStateFromMeta(leavesMeta), cellKit);
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new ItemBlock(minerCoreBranch).setRegistryName(Objects.requireNonNull(minerCoreBranch.getRegistryName())));
		registry.register(new ItemBlock(sortingCoreBranch).setRegistryName(Objects.requireNonNull(sortingCoreBranch.getRegistryName())));
		registry.register(new ItemBlock(timeCoreBranch).setRegistryName(Objects.requireNonNull(timeCoreBranch.getRegistryName())));
		registry.register(new ItemBlock(transformCoreBranch).setRegistryName(Objects.requireNonNull(transformCoreBranch.getRegistryName())));

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		setUpSeedRecipes("twilightOakSickly", new ItemStack(TreeTwilightOak.saplingBlock, 1, TreeTwilightOak.saplingMeta));
		setUpSeedRecipes("twilightOakRobust", new ItemStack(TreeTwilightOak.saplingBlock, 1, TreeTwilightOak.saplingMetaRobust));
		setUpSeedRecipes("canopy", new ItemStack(TreeCanopy.saplingBlock, 1, TreeCanopy.saplingMeta));
		setUpSeedRecipes("mangrove", new ItemStack(TreeMangrove.saplingBlock, 1, TreeMangrove.saplingMeta));
		setUpSeedRecipes("darkwood", new ItemStack(TreeDarkwood.saplingBlock, 1, TreeDarkwood.saplingMeta));
		setUpSeedRecipes("treeOfTime", new ItemStack(TreeMagicTime.saplingBlock, 1, TreeMagicTime.saplingMeta));
		setUpSeedRecipes("treeOfTransformation", new ItemStack(TreeMagicTransformation.saplingBlock, 1, TreeMagicTransformation.saplingMeta));
		setUpSeedRecipes("minersTree", new ItemStack(TreeMagicMiners.saplingBlock, 1, TreeMagicMiners.saplingMeta));
		setUpSeedRecipes("sortingTree", new ItemStack(TreeMagicSorting.saplingBlock, 1, TreeMagicSorting.saplingMeta));
		setUpSeedRecipes("rainbowOak", new ItemStack(TreeRainbowOak.saplingBlock, 1, TreeRainbowOak.saplingMeta));

	}
	private static void setUpSeedRecipes(String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, name));
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
		ModelHelper.regModel(TreeTwilightOak.sicklySpecies.getSeed());
		LeavesPaging.getLeavesMapForModId(DynamicTreesTTF.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(TreeDarkwood.darkwoodLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(TreeMagicTransformation.transformationLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());

		ModelLoader.setCustomStateMapper(dynamicCicada, new StateMap.Builder().ignore(BlockDirectional.FACING).build());
		ModelLoader.setCustomStateMapper(dynamicFirefly, new StateMap.Builder().ignore(BlockDirectional.FACING).build());

		ModelLoader.setCustomStateMapper(undergroundMangroveRoot, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(undergroundRootExposed, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(undergroundRoot, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());

		ModelLoader.setCustomStateMapper(minerCoreBranch, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(minerCoreBranchOff, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(sortingCoreBranch, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(sortingCoreBranchOff, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(timeCoreBranch, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUSNYBBLE).build());
		ModelLoader.setCustomStateMapper(((BlockBranchThick)timeCoreBranch).otherBlock, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUSNYBBLE).build());
		ModelLoader.setCustomStateMapper(timeCoreBranchOff, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUSNYBBLE).build());
		ModelLoader.setCustomStateMapper(((BlockBranchThick)timeCoreBranchOff).otherBlock, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUSNYBBLE).build());
		ModelLoader.setCustomStateMapper(transformCoreBranch, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(transformCoreBranchOff, new StateMap.Builder().ignore(BlockDynamicTwilightRoots.RADIUS).build());

		ModelLoader.setCustomStateMapper(blockRootyWater, new StateMap.Builder().ignore(BlockRootyWater.LIFE, BlockLiquid.LEVEL).build());
	}
}
