package maxhyper.dynamictreesexc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import com.ferreusveritas.dynamictrees.ModBlocks;
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

import maxhyper.dynamictreesexc.blocks.*;
import maxhyper.dynamictreesexc.items.ItemDynamicSeedBurntFeJuniper;
import maxhyper.dynamictreesexc.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesExC.MODID)
@ObjectHolder(DynamicTreesExC.MODID)
public class ModContent {

	public static BlockDynamicLeaves menrilLeaves, fejuniperLeaves;
	public static BlockBranch fejuniperBranchRaw, fejuniperBranchBurnt,
			menrilBranch, menrilBranchFilled, menrilBranchEmpty,
			rubberBranch, rubberBranchFilled, rubberBranchEmpty,
			rubberICBranch, rubberICBranchFilled, rubberICBranchEmpty,
			slimeBlueBranch, slimePurpleBranch, slimeMagmaBranch;
	public static Seed fejuniperSeedBurnt;
	public static BlockDynamicSapling fejuniperSaplingBurnt;
	public static BlockSurfaceRoot menrilRoot;
	public static BlockRooty rootySlimyDirt;
	public static BlockFruit blockGoldenApple, blockEnderPearl, blockMagmaCream;
	public static ILeavesProperties menrilLeavesProperties, rubberLeavesProperties, rubberICLeavesProperties,
			blueSlimeLeavesProperties, purpleSlimeLeavesProperties, magmaSlimeLeavesProperties,
			blossomingLeavesProperties, swampOakLeavesProperties,
			goldenOakLeavesProperties, enderOakLeavesProperties, hellishOakLeavesProperties,
			fejuniperLeavesRawProperties, fejuniperLeavesBurntProperties,
			palmLeavesProperties, sugiLeavesProperties, teaLeavesProperties,
			alicioLeavesProperties, mulberryLeavesProperties, sakuraLeavesProperties, bloodwoodLeavesProperties,
			cherrywoodLeavesProperties, mysterywoodLeavesProperties,
			calamitesLeavesProperties, cordaitesLeavesProperties, palaeorapheLeavesProperties, sigillariaLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {

	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		if (Loader.isModLoaded("integrateddynamics")) {
			//Branches
			menrilBranch = new BlockDynamicBranchMenril();
			registry.register(menrilBranch);
			menrilBranchFilled = new BlockDynamicBranchMenril(true);
			registry.register(menrilBranchFilled);

			//Leaves
			menrilLeaves = new BlockDynamicLeavesMenril();
			registry.register(menrilLeaves);

			menrilLeavesProperties = new LeavesProperties(
					IDTreeMenril.leavesBlock.getDefaultState(),
					new ItemStack(IDTreeMenril.leavesBlock),
					TreeRegistry.findCellKit("deciduous"))
			{
				@Override public int getSmotherLeavesMax() {
					return 8;
				}
				@Override public ItemStack getPrimitiveLeavesItemStack() {
					return new ItemStack(IDTreeMenril.leavesBlock);
				}
				@Override public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
					return 0xffffff;
				}
			};

			menrilLeavesProperties.setDynamicLeavesState(menrilLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
			menrilLeaves.setProperties(0, menrilLeavesProperties);

			TreeFamily menrilTree = new IDTreeMenril();
			Collections.addAll(trees, menrilTree);
		}
		if (Loader.isModLoaded("tconstruct")) {
			slimeBlueBranch = new BlockDynamicBranchSlime("slimebluebranch");
			registry.register(slimeBlueBranch);
			slimePurpleBranch = new BlockDynamicBranchSlime("slimepurplebranch");
			registry.register(slimePurpleBranch);
			slimeMagmaBranch = new BlockDynamicBranchSlime("slimemagmabranch");
			registry.register(slimeMagmaBranch);

			rootySlimyDirt = new BlockRootySlimyDirt(false);
			registry.register(rootySlimyDirt);

			blueSlimeLeavesProperties = new LeavesProperties(
					TCTreeSlimeBlue.leavesBlock.getDefaultState(),
					new ItemStack(TCTreeSlimeBlue.leavesBlock, 1, 0),
					TreeRegistry.findCellKit("conifer"))
			{
				@Override public int getSmotherLeavesMax() {
					return 8;
				}
				@Override public ItemStack getPrimitiveLeavesItemStack() {
					return new ItemStack(TCTreeSlimeBlue.leavesBlock, 1, 0);
				}
				@Override public int getFlammability() {
					return 0;
				}
				@Override public int getFireSpreadSpeed() { return 0; }
			};
			purpleSlimeLeavesProperties = new LeavesProperties(
					TCTreeSlimeBlue.leavesBlock.getDefaultState(),
					new ItemStack(TCTreeSlimeBlue.leavesBlock, 1, 1),
					TreeRegistry.findCellKit("conifer"))
			{
				@Override public int getSmotherLeavesMax() {
					return 8;
				}
				@Override public ItemStack getPrimitiveLeavesItemStack() {
					return new ItemStack(TCTreeSlimePurple.leavesBlock, 1, 1);
				}
				@Override public int getFlammability() {
					return 0;
				}
				@Override public int getFireSpreadSpeed() { return 0; }
			};
			magmaSlimeLeavesProperties = new LeavesProperties(
					TCTreeSlimeMagma.leavesBlock.getDefaultState(),
					new ItemStack(TCTreeSlimeMagma.leavesBlock, 1, 2),
					TreeRegistry.findCellKit("conifer"))
			{
				@Override public int getSmotherLeavesMax() {
					return 8;
				}
				@Override public ItemStack getPrimitiveLeavesItemStack() {
					return new ItemStack(TCTreeSlimeMagma.leavesBlock, 1, 2);
				}
				@Override public int getFlammability() {
					return 0;
				}
				@Override public int getFireSpreadSpeed() { return 0; }
			};

			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 1, blueSlimeLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 2, purpleSlimeLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 3, magmaSlimeLeavesProperties);

			TreeFamily blueSlimeTree = new TCTreeSlimeBlue();
			TreeFamily purpleSlimeTree = new TCTreeSlimePurple();
			TreeFamily magmaSlimeTree = new TCTreeSlimeMagma();
			Collections.addAll(trees, blueSlimeTree, purpleSlimeTree, magmaSlimeTree);
		}
		if (Loader.isModLoaded("thaumicbases")) {
			blockGoldenApple = (new BlockFruit("fruitgolden")).setDroppedItem(new ItemStack(Items.GOLDEN_APPLE));
			registry.register(blockGoldenApple);
			blockEnderPearl = (new BlockFruit("fruitender")).setDroppedItem(new ItemStack(Items.ENDER_PEARL));
			registry.register(blockEnderPearl);
			blockMagmaCream = (new BlockFruit("fruitmagma")).setDroppedItem(new ItemStack(Items.MAGMA_CREAM));
			registry.register(blockMagmaCream);

			goldenOakLeavesProperties = setUpLeaves(TBTreeGoldenOak.leavesBlock,  "deciduous");
			enderOakLeavesProperties = setUpLeaves(TBTreeEnderOak.leavesBlock,  "deciduous");
			hellishOakLeavesProperties = setUpLeaves(TBTreeHellishOak.leavesBlock,  "deciduous");

			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 8, goldenOakLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 9, enderOakLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 10, hellishOakLeavesProperties);

			TreeFamily goldenOakTree = new TBTreeGoldenOak();
			TreeFamily enderOakTree = new TBTreeEnderOak();
			TreeFamily hellishOakTree = new TBTreeHellishOak();
			Collections.addAll(trees, goldenOakTree, enderOakTree, hellishOakTree);
		}
		if (Loader.isModLoaded("extrautils2")) {

			fejuniperBranchRaw = new BlockDynamicBranchFeJuniper();
			registry.register(fejuniperBranchRaw);
			fejuniperBranchBurnt = new BlockDynamicBranchFeJuniper(true);
			registry.register(fejuniperBranchBurnt);

			fejuniperLeaves = new BlockDynamicLeavesFeJuniper();
			registry.register(fejuniperLeaves);

			fejuniperSaplingBurnt = new BlockDynamicSaplingBurntFeJuniper();
			registry.register(fejuniperSaplingBurnt);

			fejuniperLeavesRawProperties = setUpLeaves(EU2TreeFeJuniper.leavesBlock, EU2TreeFeJuniper.leavesStateRaw, 0, "conifer", 4, 13);
			fejuniperLeavesBurntProperties = setUpLeaves(EU2TreeFeJuniper.leavesBlock, EU2TreeFeJuniper.leavesStateBurnt, 1,"conifer", 4, 13);

			fejuniperLeavesRawProperties.setDynamicLeavesState(fejuniperLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
			fejuniperLeavesBurntProperties.setDynamicLeavesState(fejuniperLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
			fejuniperLeaves.setProperties(0, fejuniperLeavesRawProperties);
			fejuniperLeaves.setProperties(1, fejuniperLeavesBurntProperties);

			TreeFamily feJuniperTree = new EU2TreeFeJuniper();
			Collections.addAll(trees, feJuniperTree);
		}
		if (Loader.isModLoaded("techreborn")) {
			rubberBranch = new BlockDynamicBranchRubber();
			registry.register(rubberBranch);
			rubberBranchFilled = new BlockDynamicBranchRubber(true);
			registry.register(rubberBranchFilled);

			rubberLeavesProperties = setUpLeaves(TRTreeRubber.leavesBlock, "deciduous", 2, 13);

			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 0, rubberLeavesProperties);

			TreeFamily rubberTree = new TRTreeRubber();
			Collections.addAll(trees, rubberTree);
		}
		if (Loader.isModLoaded("quark")) {
			swampOakLeavesProperties = setUpLeaves(QTreeSwampOak.leavesBlock, QTreeSwampOak.leavesState, 0, "deciduous", 3, 13);
			blossomingLeavesProperties = setUpLeaves(QTreeBlossoming.leavesBlock, QTreeBlossoming.leavesState, 1, "deciduous", 4, 13);

			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 4, blossomingLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 5, swampOakLeavesProperties);

			TreeFamily blossomingTree = new QTreeBlossoming();
			TreeFamily swampOakTree = new QTreeSwampOak();
			Collections.addAll(trees, blossomingTree, swampOakTree);
		}
		if (Loader.isModLoaded("ic2")) {
			rubberICBranch = new BlockDynamicBranchRubberIC();
			registry.register(rubberICBranch);
			rubberICBranchFilled = new BlockDynamicBranchRubberIC(true);
			registry.register(rubberICBranchFilled);

			rubberICLeavesProperties = setUpLeaves(IC2TreeRubber.leavesBlock, "deciduous", 2, 13);

			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 6, rubberICLeavesProperties);

			TreeFamily rubberICTree = new IC2TreeRubber();
			Collections.addAll(trees, rubberICTree);
		}
		if (Loader.isModLoaded("atum")) {
			palmLeavesProperties = new LeavesProperties(
					A2TreePalm.leavesBlock.getDefaultState(),
					new ItemStack(A2TreePalm.leavesBlock),
					TreeRegistry.findCellKit("palm") ) {
				@Override
				public boolean appearanceChangesWithHydro() {
					return true;
				}
			};
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 7, palmLeavesProperties); //leaves1 - 3

			TreeFamily palmTree = new A2TreePalm();
			Collections.addAll(trees, palmTree);
		}
		if (Loader.isModLoaded("forbidden_arcanus")) {
			cherrywoodLeavesProperties = setUpLeaves(FnATreeCherrywood.leavesBlock, "deciduous");
			mysterywoodLeavesProperties = setUpLeaves(FnATreeMysterywood.leavesBlock, "deciduous");
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 20, cherrywoodLeavesProperties); //leaves5 - 0
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 21, mysterywoodLeavesProperties); //leaves5 - 1

			TreeFamily cherrywoodTree = new FnATreeCherrywood();
			TreeFamily mysterywoodTree = new FnATreeMysterywood();
			Collections.addAll(trees, cherrywoodTree, mysterywoodTree);
		}
		if (Loader.isModLoaded("simplytea")) {
			teaLeavesProperties = new LeavesProperties(
					null,
					null,
					TreeRegistry.findCellKit("deciduous"))
			{
			};
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 22, teaLeavesProperties); //leaves5 - 2

			TreeFamily teaTree = new STTreeTea();
			Collections.addAll(trees, teaTree);
		}
		if (Loader.isModLoaded("sugiforest")) {
			sugiLeavesProperties = setUpLeaves(SFTreeSugi.leavesBlock, "deciduous");
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 11, sugiLeavesProperties); //leaves2 - 3

			TreeFamily sugiTree = new SFTreeSugi();
			Collections.addAll(trees, sugiTree);
		}
		if (Loader.isModLoaded("betterwithmods")) {
			bloodwoodLeavesProperties = setUpLeaves(BWMTreeBloodwood.leavesBlock, "deciduous");
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 12, bloodwoodLeavesProperties); //leaves3 - 0

			TreeFamily bloodTree = new BWMTreeBloodwood();
			Collections.addAll(trees, bloodTree);
		}
		if (Loader.isModLoaded("betterwithaddons")) {
			alicioLeavesProperties = setUpLeaves(BWATreeAlicio.leavesBlock, "deciduous");
			mulberryLeavesProperties = setUpLeaves(BWATreeMulberry.leavesBlock, "deciduous");
			sakuraLeavesProperties = setUpLeaves(BWATreeSakura.leavesBlock, "deciduous");
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 13, alicioLeavesProperties); //leaves3 - 1
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 14, mulberryLeavesProperties); //leaves3 - 2
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 15, sakuraLeavesProperties); //leaves3 - 3

			TreeFamily alicioTree = new BWATreeAlicio();
			TreeFamily mulberryTree = new BWATreeMulberry();
			TreeFamily sakuraTree = new BWATreeSakura();
			Collections.addAll(trees, alicioTree, mulberryTree, sakuraTree);
		}
		if (Loader.isModLoaded("fossil")) {
			calamitesLeavesProperties = setUpLeaves(FsArTreeCalamites.leavesBlock, "deciduous");
			cordaitesLeavesProperties = setUpLeaves(FsArTreeCordaites.leavesBlock, "deciduous");
			palaeorapheLeavesProperties = setUpLeaves(FsArTreePalaeoraphe.leavesBlock, "deciduous");
			sigillariaLeavesProperties = setUpLeaves(FsArTreeSigillaria.leavesBlock, "deciduous");
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 16, calamitesLeavesProperties); //leaves4 - 0
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 17, cordaitesLeavesProperties); //leaves4 - 1
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 18, palaeorapheLeavesProperties); //leaves4 - 2
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 19, sigillariaLeavesProperties); //leaves4 - 3

			TreeFamily calaTree = new FsArTreeCalamites();
			TreeFamily cordTree = new FsArTreeCordaites();
			TreeFamily palaTree = new FsArTreePalaeoraphe();
			TreeFamily sigiTree = new FsArTreeSigillaria();
			Collections.addAll(trees, calaTree, cordTree, palaTree, sigiTree);
		}

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesExC.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getDefaultState(),
				new ItemStack(leavesBlock, 1, 0),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, 0);
			}
		};
		return leavesProperties;
	}
	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit, int smother, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getDefaultState(),
				new ItemStack(leavesBlock, 1, 0),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
			@Override public int getLightRequirement() { return light; } //Default: 13
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, 0);
			}
		};
		return leavesProperties;
	}
	public static ILeavesProperties setUpLeaves (Block leavesBlock, IBlockState leavesState, int leavesMeta, String cellKit, int smother, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesState,
				new ItemStack(leavesBlock, 1, leavesMeta),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
			@Override public int getLightRequirement() { return light; } //Default: 13
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesMeta);
			}
		};
		return leavesProperties;
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		if (Loader.isModLoaded("extrautils2")) {
			fejuniperSeedBurnt = new ItemDynamicSeedBurntFeJuniper();
			registry.register(fejuniperSeedBurnt);

			registry.register(new ItemBlock(fejuniperBranchBurnt).setRegistryName(Objects.requireNonNull(fejuniperBranchBurnt.getRegistryName())));

		}
		if (Loader.isModLoaded("integrateddynamics")) {
			registry.register(new ItemBlock(menrilBranchFilled).setRegistryName(Objects.requireNonNull(menrilBranchFilled.getRegistryName())));

		}
		if (Loader.isModLoaded("techreborn")) {
			registry.register(new ItemBlock(rubberBranchFilled).setRegistryName(Objects.requireNonNull(rubberBranchFilled.getRegistryName())));

		}
		if (Loader.isModLoaded("ic2")) {
			registry.register(new ItemBlock(rubberICBranchFilled).setRegistryName(Objects.requireNonNull(rubberICBranchFilled.getRegistryName())));

		}

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		if (Loader.isModLoaded("integrateddynamics")) {
			setUpSeedRecipes("menril", new ItemStack(IDTreeMenril.saplingBlock));
		}
		if (Loader.isModLoaded("tconstruct")) {
			setUpSeedRecipes("slimeBlue", new ItemStack(TCTreeSlimeBlue.saplingBlock, 1, 0));
			setUpSeedRecipes("slimePurple", new ItemStack(TCTreeSlimePurple.saplingBlock, 1, 1));
			setUpSeedRecipes("slimeMagma", new ItemStack(TCTreeSlimeMagma.saplingBlock, 1, 2));
		}
		if (Loader.isModLoaded("thaumicbases")) {
			setUpSeedRecipes("goldenOak", new ItemStack(TBTreeGoldenOak.saplingBlock));
			setUpSeedRecipes("enderOak", new ItemStack(TBTreeEnderOak.saplingBlock));
			setUpSeedRecipes("hellishOak", new ItemStack(TBTreeHellishOak.saplingBlock));
		}
		if (Loader.isModLoaded("extrautils2")) {
			setUpSeedRecipes("ferrousJuniper", new ItemStack(EU2TreeFeJuniper.saplingBlock));
			setUpSeedRecipes("ferrousJuniperBurnt", new ItemStack(EU2TreeFeJuniper.saplingBlock, 1, 1), new ItemStack(fejuniperSeedBurnt));
		}
		if (Loader.isModLoaded("techreborn")) {
			setUpSeedRecipes("rubber", new ItemStack(TRTreeRubber.saplingBlock));
		}
		if (Loader.isModLoaded("quark")) {
			setUpSeedRecipes("blossoming", new ItemStack(QTreeBlossoming.saplingBlock, 1, 1));
			setUpSeedRecipes("swampOak", new ItemStack(QTreeSwampOak.saplingBlock, 1, 0));
		}
		if (Loader.isModLoaded("ic2")) {
			setUpSeedRecipes("rubberIC", new ItemStack(IC2TreeRubber.saplingBlock));
		}
		if (Loader.isModLoaded("atum")) {
			setUpSeedRecipes("palm", new ItemStack(A2TreePalm.saplingBlock));
		}
		if (Loader.isModLoaded("forbidden_arcanus")) {
			setUpSeedRecipes("cherrywood", new ItemStack(FnATreeCherrywood.saplingBlock));
			setUpSeedRecipes("mysterywood", new ItemStack(FnATreeMysterywood.saplingBlock));
		}
		if (Loader.isModLoaded("simplytea")) {
			setUpSeedRecipes("tea", new ItemStack(STTreeTea.saplingBlock));
		}
		if (Loader.isModLoaded("sugiforest")) {
			setUpSeedRecipes("sugi", new ItemStack(SFTreeSugi.saplingBlock));
		}
		if (Loader.isModLoaded("betterwithmods")) {
			setUpSeedRecipes("bloodwood", new ItemStack(BWMTreeBloodwood.saplingBlock));
		}
		if (Loader.isModLoaded("betterwithaddons")) {
			setUpSeedRecipes("alicio", new ItemStack(BWATreeAlicio.saplingBlock));
			setUpSeedRecipes("mulberry", new ItemStack(BWATreeMulberry.saplingBlock));
			setUpSeedRecipes("sakura", new ItemStack(BWATreeSakura.saplingBlock));
		}
		if (Loader.isModLoaded("fossil")) {
			setUpSeedRecipes("calamites", new ItemStack(FsArTreeCalamites.saplingBlock));
			setUpSeedRecipes("cordaites", new ItemStack(FsArTreeCordaites.saplingBlock));
			setUpSeedRecipes("palaeoraphe", new ItemStack(FsArTreePalaeoraphe.saplingBlock));
			setUpSeedRecipes("sigillaria", new ItemStack(FsArTreeSigillaria.saplingBlock));
		}

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}
	public static void setUpSeedRecipes (String name, ItemStack sapling, ItemStack seed){
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesExC.MODID, name+"seed"), (ResourceLocation)null, seed, Ingredient.fromStacks(sapling), Ingredient.fromItem(ModItems.dirtBucket));
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesExC.MODID, name+"sapling"), (ResourceLocation)null, sapling, Ingredient.fromStacks(seed), Ingredient.fromItem(ModItems.dirtBucket));
		OreDictionary.registerOre("treeSapling", new ItemStack(fejuniperSeedBurnt));
	} //Fake Seed

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (TreeFamily tree : trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		LeavesPaging.getLeavesMapForModId(DynamicTreesExC.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
		if (Loader.isModLoaded("integrateddynamics")) {
			ModelHelper.regModel(menrilBranchFilled);
			ModelLoader.setCustomStateMapper(menrilLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		}
		if (Loader.isModLoaded("tconstruct")) {
			ModelLoader.setCustomStateMapper(rootySlimyDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
		}
		if (Loader.isModLoaded("extrautils2")) {
			ModelHelper.regModel(fejuniperSeedBurnt);
			ModelHelper.regModel(fejuniperBranchBurnt);
			ModelLoader.setCustomStateMapper(fejuniperLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		}
		if (Loader.isModLoaded("techreborn")) {
			ModelHelper.regModel(rubberBranchFilled);
		}


	}
}
