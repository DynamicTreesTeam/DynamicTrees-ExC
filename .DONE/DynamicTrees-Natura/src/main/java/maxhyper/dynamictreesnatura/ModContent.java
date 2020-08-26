package maxhyper.dynamictreesnatura;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.overworld.NaturaOverworld;
import maxhyper.dynamictreesnatura.blocks.*;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedBloodwood;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedFusewood;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedHopseed;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedMaple;
import maxhyper.dynamictreesnatura.trees.*;
import com.progwml6.natura.shared.NaturaCommons;
import maxhyper.dynamictreesnatura.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = DynamicTreesNatura.MODID)
@ObjectHolder(DynamicTreesNatura.MODID)
public class ModContent {

	public static BlockDynamicLeaves darkwoodLeaves, bloodwoodLeaves;
	public static BlockBranch bloodwoodBranch, fusewoodBranch;
	public static BlockDynamicSapling bloodwoodSapling;
	public static Seed bloodwoodSeed, mapleSeed, hopseedSeed, fusewoodSeed;
	public static BlockRooty rootyUpsidedownDirt, rootyNetherDirt;
	public static BlockFruit blockPotashApple;
	public static ILeavesProperties mapleLeavesProperties, silverbellLeavesProperties, amaranthLeavesProperties, tigerwoodLeavesProperties,
			willowLeavesProperties, eucalyptusLeavesProperties, hopseedLeavesProperties, sakuraLeavesProperties,
			ghostwoodLeavesProperties, bloodwoodLeavesProperties, fusewoodLeavesProperties,
			darkwoodLeavesProperties, darkwoodFloweringLeavesProperties, darkwoodFruitLeavesProperties, cactusLeavesProperties;

	public static CactusSaguaro saguaroCactus;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	public static boolean generateRedwood = false;
	public static boolean generateMaple;
	public static boolean generateSilverbell;
	public static boolean generateAmaranth;
	public static boolean generateTiger;
	public static boolean generateWillow;
	public static boolean generateEucalyptus;
	public static boolean generateHopseed;
	public static boolean generateSakura;
	public static boolean generateBloodwood;
	public static boolean generateDarkwood;
	public static boolean generateFusewood;
	public static boolean generateGhostwood;
	public static boolean generateSaguaro;

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		rootyUpsidedownDirt = new BlockRootyNetherUpsideDown(false);
		registry.register(rootyUpsidedownDirt);
		rootyNetherDirt = new BlockRootyNether(false);
		registry.register(rootyNetherDirt);

		bloodwoodBranch = new BlockDynamicBranchBloodwood();
		fusewoodBranch = new BlockDynamicBranchFusewood();
		
		darkwoodLeaves = new BlockDynamicLeavesDarkwood();
		registry.register(darkwoodLeaves);
		bloodwoodLeaves = new BlockDynamicLeavesBloodwood();
		registry.register(bloodwoodLeaves);

		bloodwoodSapling = new BlockDynamicSaplingBloodwood();

		blockPotashApple = new BlockFruit("fruitpotash"){
			@Override
			public ItemStack getFruitDrop() {
				return NaturaCommons.potashApple;
			}
		};
		registry.register(blockPotashApple);

		bloodwoodSeed = new ItemDynamicSeedBloodwood();
		mapleSeed = new ItemDynamicSeedMaple();
		hopseedSeed = new ItemDynamicSeedHopseed();
		fusewoodSeed = new ItemDynamicSeedFusewood();

		mapleLeavesProperties = setUpLeaves(TreeMaple.leavesBlock, TreeMaple.leavesState, "deciduous");
		silverbellLeavesProperties = setUpLeaves(TreeSilverbell.leavesBlock, TreeSilverbell.leavesState, "deciduous");
		amaranthLeavesProperties = setUpLeaves(TreeAmaranth.leavesBlock, TreeAmaranth.leavesState, "deciduous");
		tigerwoodLeavesProperties = setUpLeaves(TreeTigerwood.leavesBlock, TreeTigerwood.leavesState, "deciduous");
		willowLeavesProperties = setUpLeaves(TreeWillow.leavesBlock, TreeWillow.leavesState, "deciduous", 3, 13);
		eucalyptusLeavesProperties = setUpLeaves(TreeEucalyptus.leavesBlock, TreeEucalyptus.leavesState, "acacia");
		hopseedLeavesProperties = setUpLeaves(TreeHopseed.leavesBlock, TreeHopseed.leavesState, "deciduous");
		sakuraLeavesProperties = setUpLeaves(TreeSakura.leavesBlock, TreeSakura.leavesState, "deciduous");

		ghostwoodLeavesProperties = setUpLeavesNether(TreeGhostwood.leavesBlock, TreeGhostwood.leavesState, "deciduous");
		bloodwoodLeavesProperties = setUpLeavesNether(TreeBloodwood.leavesBlock, TreeBloodwood.leavesState, "bloodwood");
		fusewoodLeavesProperties = setUpLeavesNether(TreeFusewood.leavesBlock, TreeFusewood.leavesState, "deciduous");
		darkwoodLeavesProperties = setUpLeavesNether(TreeDarkwood.leavesBlock, TreeDarkwood.leavesState, "deciduous");
		darkwoodFloweringLeavesProperties = setUpLeavesNether(TreeDarkwood.leavesBlock, TreeDarkwood.leavesState, "deciduous");
		darkwoodFruitLeavesProperties = setUpLeavesNether(TreeDarkwood.leavesBlock, TreeDarkwood.leavesState, "deciduous");

		cactusLeavesProperties = new LeavesProperties(null, ItemStack.EMPTY, TreeRegistry.findCellKit("bare"));

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 0, mapleLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 1, silverbellLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 2, amaranthLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 3, tigerwoodLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 4, willowLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 5, eucalyptusLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 6, hopseedLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 7, sakuraLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 8, ghostwoodLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 9, bloodwoodLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesNatura.MODID, 10, fusewoodLeavesProperties);

		darkwoodLeavesProperties.setDynamicLeavesState(darkwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		darkwoodFloweringLeavesProperties.setDynamicLeavesState(darkwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		darkwoodFruitLeavesProperties.setDynamicLeavesState(darkwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		darkwoodLeaves.setProperties(0, darkwoodLeavesProperties);
		darkwoodLeaves.setProperties(1, darkwoodFloweringLeavesProperties);
		darkwoodLeaves.setProperties(2, darkwoodFruitLeavesProperties);
		bloodwoodLeavesProperties.setDynamicLeavesState(bloodwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		bloodwoodLeaves.setProperties(0, bloodwoodLeavesProperties);

		//TreeFamily redwoodTree = new TreeRedwood();
		TreeFamily mapleTree = new TreeMaple();
		TreeFamily silverbellTree = new TreeSilverbell();
		TreeFamily amaranthTree = new TreeAmaranth();
		TreeFamily tigerwoodTree = new TreeTigerwood();
		TreeFamily willowTree = new TreeWillow();
		TreeFamily eucalyptusTree = new TreeEucalyptus();
		TreeFamily hopseedTree = new TreeHopseed();
		TreeFamily sakuraTree = new TreeSakura();
		TreeFamily ghostwoodTree = new TreeGhostwood();
		TreeFamily bloodwoodTree = new TreeBloodwood();
		TreeFamily fusewoodTree = new TreeFusewood();
		TreeFamily darkwoodTree = new TreeDarkwood();

		saguaroCactus = new CactusSaguaro();
		saguaroCactus.registerSpecies(Species.REGISTRY);

		Collections.addAll(trees, mapleTree, silverbellTree, amaranthTree, tigerwoodTree, willowTree, eucalyptusTree, hopseedTree, sakuraTree, ghostwoodTree, bloodwoodTree, fusewoodTree, darkwoodTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));

		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		saguaroCactus.getRegisterableBlocks(treeBlocks);
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesNatura.MODID).values());
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
	private static ILeavesProperties setUpLeavesNether (Block leavesBlock, IBlockState leavesState, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesState,
				new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState)),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState));
			}
			@Override public int getLightRequirement() {
				return 0;
			}
			@Override public int getFlammability() { return 0; }
			@Override public int getFireSpreadSpeed() { return 0; }
		};
		return leavesProperties;
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		saguaroCactus.getRegisterableItems(treeItems);

		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesNatura.MODID, "mapleseed"), ItemDynamicSeedMaple.EntityItemMapleSeed.class, "maple_seed", id++, DynamicTreesNatura.MODID, 32, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesNatura.MODID, "bloodwoodseed"), ItemDynamicSeedBloodwood.EntityItemBloodwoodSeed.class, "magic_seed", id++, DynamicTreesNatura.MODID, 32, 1, true);
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		//setUpSeedRecipes("giantRedwood", new ItemStack(TreeRedwood.saplingBlock));
		setUpSeedRecipes("maple", new ItemStack(TreeMaple.saplingBlock, 1, 0));
		setUpSeedRecipes("silverbell", new ItemStack(TreeSilverbell.saplingBlock, 1, 1));
		setUpSeedRecipes("amaranth", new ItemStack(TreeAmaranth.saplingBlock, 1, 2));
		setUpSeedRecipes("tigerwood", new ItemStack(TreeTigerwood.saplingBlock, 1, 3));
		setUpSeedRecipes("willow", new ItemStack(TreeWillow.saplingBlock, 1, 0));
		setUpSeedRecipes("eucalyptus", new ItemStack(TreeEucalyptus.saplingBlock, 1, 1));
		setUpSeedRecipes("hopseed", new ItemStack(TreeHopseed.saplingBlock, 1, 2));
		setUpSeedRecipes("sakura", new ItemStack(TreeSakura.saplingBlock, 1, 3));
		setUpSeedRecipes("ghostwood", new ItemStack(TreeGhostwood.saplingBlock, 1, 0));
		setUpSeedRecipes("bloodwood", new ItemStack(TreeBloodwood.saplingBlock, 1, 1));
		setUpSeedRecipes("fusewood", new ItemStack(TreeFusewood.saplingBlock, 1, 2));
		setUpSeedRecipes("darkwood", new ItemStack(TreeDarkwood.saplingBlock, 1, 0));

		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "darkwood"));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesNatura.MODID, "potashAppleSeed"), null, treeSeed, Ingredient.fromStacks(NaturaCommons.potashApple));

		Species cactusSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "saguaro"));
		ItemStack cactusSeed = cactusSpecies.getSeedStack(1);
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesNatura.MODID, "SaguaroFruitSeed"), null, cactusSeed, Ingredient.fromItem(NaturaOverworld.saguaroFruitItem));

	}
	private static void setUpSeedRecipes(String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesNatura.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(rootyUpsidedownDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
		ModelLoader.setCustomStateMapper(rootyNetherDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
		ModelLoader.setCustomStateMapper(darkwoodLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(bloodwoodLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());

		ModelLoader.setCustomStateMapper(saguaroCactus.getDynamicBranch(), new StateMap.Builder().ignore(BlockBranchCactus.TRUNK, BlockBranchCactus.ORIGIN).build());
		ModelHelper.regModel(saguaroCactus.getDynamicBranch());
		ModelHelper.regModel(saguaroCactus.getCommonSpecies().getSeed());
	}
}
