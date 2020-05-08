package maxhyper.dynamictreesforestry;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@Mod.EventBusSubscriber(modid = DynamicTreesForestry.MODID)
@ObjectHolder(DynamicTreesForestry.MODID)
public class ModContent {
//
//	public static BlockDynamicLeaves darkwoodLeaves, bloodwoodLeaves;
//	public static BlockBranch bloodwoodBranch, fusewoodBranch;
//	public static BlockDynamicSapling bloodwoodSapling;
//	public static Seed bloodwoodSeed;
//	public static BlockRooty rootyUpsidedownDirt, rootyNetherDirt;
//	public static BlockFruit blockPotashApple;
//	public static ILeavesProperties mapleLeavesProperties, silverbellLeavesProperties, amaranthLeavesProperties, tigerwoodLeavesProperties,
//			willowLeavesProperties, eucalyptusLeavesProperties, hopseedLeavesProperties, sakuraLeavesProperties,
//			ghostwoodLeavesProperties, bloodwoodLeavesProperties, fusewoodLeavesProperties,
//			darkwoodLeavesProperties, darkwoodFloweringLeavesProperties, darkwoodFruitLeavesProperties;
//
//	// trees added by this mod
//	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
//	@SubscribeEvent
//	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
//		event.register(new BiomeDataBasePopulator());
//	}
//
//	@SubscribeEvent
//	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
//		IForgeRegistry<Block> registry = event.getRegistry();
//
//		rootyUpsidedownDirt = new BlockRootyNetherUpsideDown(false);
//		registry.register(rootyUpsidedownDirt);
//		rootyNetherDirt = new BlockRootyNether(false);
//		registry.register(rootyNetherDirt);
//
//		bloodwoodBranch = new BlockDynamicBranchBloodwood();
//		registry.register(bloodwoodBranch);
//		fusewoodBranch = new BlockDynamicBranchFusewood();
//		registry.register(fusewoodBranch);
//
//		darkwoodLeaves = new BlockDynamicLeavesDarkwood();
//		registry.register(darkwoodLeaves);
//		bloodwoodLeaves = new BlockDynamicLeavesBloodwood();
//		registry.register(bloodwoodLeaves);
//
//		bloodwoodSapling = new BlockDynamicSaplingBloodwood();
//		registry.register(bloodwoodSapling);
//
//		blockPotashApple = new BlockFruit("fruitpotash"){
//			@Override
//			public ItemStack getFruitDrop() {
//				return NaturaCommons.potashApple;
//			}
//		};
//		registry.register(blockPotashApple);
//
//		bloodwoodSeed = new ItemDynamicSeedBloodwood();
//
//		mapleLeavesProperties = setUpLeaves(TreeMaple.leavesBlock, TreeMaple.leavesState, "deciduous");
//		silverbellLeavesProperties = setUpLeaves(TreeSilverbell.leavesBlock, TreeSilverbell.leavesState, "deciduous");
//		amaranthLeavesProperties = setUpLeaves(TreeAmaranth.leavesBlock, TreeAmaranth.leavesState, "deciduous");
//		tigerwoodLeavesProperties = setUpLeaves(TreeTigerwood.leavesBlock, TreeTigerwood.leavesState, "deciduous");
//		willowLeavesProperties = setUpLeaves(TreeWillow.leavesBlock, TreeWillow.leavesState, "deciduous", 3, 13);
//		eucalyptusLeavesProperties = setUpLeaves(TreeEucalyptus.leavesBlock, TreeEucalyptus.leavesState, "acacia");
//		hopseedLeavesProperties = setUpLeaves(TreeHopseed.leavesBlock, TreeHopseed.leavesState, "deciduous");
//		sakuraLeavesProperties = setUpLeaves(TreeSakura.leavesBlock, TreeSakura.leavesState, "deciduous");
//
//		ghostwoodLeavesProperties = setUpLeavesNether(TreeGhostwood.leavesBlock, TreeGhostwood.leavesState, "deciduous");
//		bloodwoodLeavesProperties = setUpLeavesNether(TreeBloodwood.leavesBlock, TreeBloodwood.leavesState, "bloodwood");
//		fusewoodLeavesProperties = setUpLeavesNether(TreeFusewood.leavesBlock, TreeFusewood.leavesState, "deciduous");
//		darkwoodLeavesProperties = setUpLeavesNether(TreeDarkwood.leavesBlock, TreeDarkwood.leavesState, "deciduous");
//		darkwoodFloweringLeavesProperties = setUpLeavesNether(TreeDarkwood.leavesBlock, TreeDarkwood.leavesState, "deciduous");
//		darkwoodFruitLeavesProperties = setUpLeavesNether(TreeDarkwood.leavesBlock, TreeDarkwood.leavesState, "deciduous");
//
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 0, mapleLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 1, silverbellLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 2, amaranthLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 3, tigerwoodLeavesProperties);
//
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 4, willowLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 5, eucalyptusLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 6, hopseedLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 7, sakuraLeavesProperties);
//
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 8, ghostwoodLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 9, bloodwoodLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 10, fusewoodLeavesProperties);
//
//		darkwoodLeavesProperties.setDynamicLeavesState(darkwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
//		darkwoodFloweringLeavesProperties.setDynamicLeavesState(darkwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
//		darkwoodFruitLeavesProperties.setDynamicLeavesState(darkwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
//		darkwoodLeaves.setProperties(0, darkwoodLeavesProperties);
//		darkwoodLeaves.setProperties(1, darkwoodFloweringLeavesProperties);
//		darkwoodLeaves.setProperties(2, darkwoodFruitLeavesProperties);
//		bloodwoodLeavesProperties.setDynamicLeavesState(bloodwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
//		bloodwoodLeaves.setProperties(0, bloodwoodLeavesProperties);
//
//		//TreeFamily redwoodTree = new TreeRedwood();
//		TreeFamily mapleTree = new TreeMaple();
//		TreeFamily silverbellTree = new TreeSilverbell();
//		TreeFamily amaranthTree = new TreeAmaranth();
//		TreeFamily tigerwoodTree = new TreeTigerwood();
//		TreeFamily willowTree = new TreeWillow();
//		TreeFamily eucalyptusTree = new TreeEucalyptus();
//		TreeFamily hopseedTree = new TreeHopseed();
//		TreeFamily sakuraTree = new TreeSakura();
//		TreeFamily ghostwoodTree = new TreeGhostwood();
//		TreeFamily bloodwoodTree = new TreeBloodwood();
//		TreeFamily fusewoodTree = new TreeFusewood();
//		TreeFamily darkwoodTree = new TreeDarkwood();
//
//		Collections.addAll(trees, mapleTree, silverbellTree, amaranthTree, tigerwoodTree, willowTree, eucalyptusTree, hopseedTree, sakuraTree, ghostwoodTree, bloodwoodTree, fusewoodTree, darkwoodTree);
//
//		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
//		ArrayList<Block> treeBlocks = new ArrayList<>();
//		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
//		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesForestry.MODID).values());
//		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
//	}
//
//	private static ILeavesProperties setUpLeaves (Block leavesBlock, IBlockState leavesState, String cellKit){
//		ILeavesProperties leavesProperties;
//		leavesProperties = new LeavesProperties(
//				leavesState,
//				new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState)),
//				TreeRegistry.findCellKit(cellKit))
//		{
//			@Override public ItemStack getPrimitiveLeavesItemStack() {
//				return new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState));
//			}
//		};
//		return leavesProperties;
//	}
//	private static ILeavesProperties setUpLeaves (Block leavesBlock, IBlockState leavesState, String cellKit, int smother, int light){
//		ILeavesProperties leavesProperties;
//		leavesProperties = new LeavesProperties(
//				leavesState,
//				new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState)),
//				TreeRegistry.findCellKit(cellKit))
//		{
//			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
//			@Override public int getLightRequirement() { return light; } //Default: 13
//			@Override public ItemStack getPrimitiveLeavesItemStack() {
//				return new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState));
//			}
//		};
//		return leavesProperties;
//	}
//	private static ILeavesProperties setUpLeavesNether (Block leavesBlock, IBlockState leavesState, String cellKit){
//		ILeavesProperties leavesProperties;
//		leavesProperties = new LeavesProperties(
//				leavesState,
//				new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState)),
//				TreeRegistry.findCellKit(cellKit))
//		{
//			@Override public ItemStack getPrimitiveLeavesItemStack() {
//				return new ItemStack(leavesBlock, 1, leavesBlock.getMetaFromState(leavesState));
//			}
//			@Override public int getLightRequirement() {
//				return 0;
//			}
//			@Override public int getFlammability() { return 0; }
//			@Override public int getFireSpreadSpeed() { return 0; }
//		};
//		return leavesProperties;
//	}
//
//
//	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
//		IForgeRegistry<Item> registry = event.getRegistry();
//
//		registry.register(bloodwoodSeed);
//
//		ArrayList<Item> treeItems = new ArrayList<>();
//		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
//		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
//	}
//
//	@SubscribeEvent
//	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
//		//setUpSeedRecipes("giantRedwood", new ItemStack(TreeRedwood.saplingBlock));
//		setUpSeedRecipes("maple", new ItemStack(TreeMaple.saplingBlock, 1, 0));
//		setUpSeedRecipes("silverbell", new ItemStack(TreeSilverbell.saplingBlock, 1, 1));
//		setUpSeedRecipes("amaranth", new ItemStack(TreeAmaranth.saplingBlock, 1, 2));
//		setUpSeedRecipes("tigerwood", new ItemStack(TreeTigerwood.saplingBlock, 1, 3));
//		setUpSeedRecipes("willow", new ItemStack(TreeWillow.saplingBlock, 1, 0));
//		setUpSeedRecipes("eucalyptus", new ItemStack(TreeEucalyptus.saplingBlock, 1, 1));
//		setUpSeedRecipes("hopseed", new ItemStack(TreeHopseed.saplingBlock, 1, 2));
//		setUpSeedRecipes("sakura", new ItemStack(TreeSakura.saplingBlock, 1, 3));
//		setUpSeedRecipes("ghostwood", new ItemStack(TreeGhostwood.saplingBlock, 1, 0));
//		setUpSeedRecipes("bloodwood", new ItemStack(TreeBloodwood.saplingBlock, 1, 1));
//		setUpSeedRecipes("fusewood", new ItemStack(TreeFusewood.saplingBlock, 1, 2));
//		setUpSeedRecipes("darkwood", new ItemStack(TreeDarkwood.saplingBlock, 1, 0));
//	}
//	private static void setUpSeedRecipes(String name, ItemStack treeSapling){
//		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, name));
//		ItemStack treeSeed = treeSpecies.getSeedStack(1);
//		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
//		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
//		ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
//	}
//
//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent
//	public static void registerModels(ModelRegistryEvent event) {
//		for (TreeFamily tree : trees) {
//			ModelHelper.regModel(tree.getDynamicBranch());
//			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
//			ModelHelper.regModel(tree);
//		}
//		LeavesPaging.getLeavesMapForModId(DynamicTreesForestry.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
//
//		ModelLoader.setCustomStateMapper(rootyUpsidedownDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
//		ModelLoader.setCustomStateMapper(rootyNetherDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
//		ModelLoader.setCustomStateMapper(darkwoodLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
//		ModelLoader.setCustomStateMapper(bloodwoodLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
//	}
}
