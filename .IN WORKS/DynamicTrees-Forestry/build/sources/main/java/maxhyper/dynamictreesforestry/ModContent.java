package maxhyper.dynamictreesforestry;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesFruit;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesforestry.blocks.BlockFruitDate;
import maxhyper.dynamictreesforestry.crafting.RecipeFruitToSapling;
import maxhyper.dynamictreesforestry.crafting.RecipeSaplingToSeed;
import maxhyper.dynamictreesforestry.crafting.RecipeSeedToSapling;
import maxhyper.dynamictreesforestry.trees.*;
import maxhyper.dynamictreesforestry.trees.vanilla.*;
import maxhyper.dynamictreesforestry.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.BlockRenderLayer;
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


import java.util.*;

@Mod.EventBusSubscriber(modid = DynamicTreesForestry.MODID)
@ObjectHolder(DynamicTreesForestry.MODID)
public class ModContent {

	public static ILeavesProperties acaciaLeavesProperties, birchLeavesProperties, darkOakLeavesProperties,
			jungleLeavesProperties, oakLeavesProperties, spruceLeavesProperties,
			silverLimeLeavesProperties, walnutLeavesProperties, chestnutLeavesProperties, cherryLeavesProperties,
			lemonLeavesProperties, plumLeavesProperties, mapleLeavesProperties, larchLeavesProperties,
			bullPineLeavesProperties, coastSequoiaLeavesProperties, giantSequoiaLeavesProperties,
			teakLeavesProperties, ipeLeavesProperties, kapokLeavesProperties, ebonyLeavesProperties,
			zebrawoodLeavesProperties, merantiLeavesProperties, desertAcaciaLeavesProperties,
			padaukLeavesProperties, balsaLeavesProperties, cocoboloLeavesProperties, wengeLeavesProperties,
			baobabLeavesProperties, mahoeLeavesProperties, willowLeavesProperties, sipiriLeavesProperties,
			papayaLeavesProperties, palmLeavesProperties, poplarLeavesProperties;

	public static ILeavesProperties fruitAppleLeavesProperties, fruitWalnutLeavesProperties, fruitChestnutLeavesProperties,
			fruitCherryLeavesProperties, fruitLemonLeavesProperties, fruitPlumLeavesProperties;

	public static BlockDynamicLeavesFruit appleLeaves, walnutLeaves, chestnutLeaves, cherryLeaves, lemonLeaves, plumLeaves;
	public static BlockDynamicLeavesPalm palmFrondLeaves, papayaFrondLeaves;

	public static BlockFruitDate dateFruit, papayaFruit;
	public static BlockFruit chestnutFruit, walnutFruit, cherryFruit, lemonFruit, plumFruit;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	@SubscribeEvent
	public static void registerDataBasePopulators(final WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		chestnutFruit = new BlockFruit("fruitchestnut");
		registry.register(chestnutFruit);
		walnutFruit = new BlockFruit("fruitwalnut");
		registry.register(walnutFruit);
		cherryFruit = new BlockFruit("fruitcherry");
		registry.register(cherryFruit);
		lemonFruit = new BlockFruit("fruitlemon"){
			@Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
			{
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
		registry.register(lemonFruit);
		plumFruit = new BlockFruit("fruitplum"){
			@Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
			{
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
		registry.register(plumFruit);

		dateFruit = new BlockFruitDate("fruitdate", 5);
		registry.register(dateFruit);
		papayaFruit = new BlockFruitDate("fruitpapaya", 6);
		registry.register(papayaFruit);
		palmFrondLeaves = new BlockDynamicLeavesPalm("leaves_palm");
		registry.register(palmFrondLeaves);
		papayaFrondLeaves = new BlockDynamicLeavesPalm("leaves_papaya");
		registry.register(papayaFrondLeaves);

		appleLeaves = new BlockDynamicLeavesFruit("leaves_apple", BlockDynamicLeavesFruit.fruitTypes.APPLE);
		registry.register(appleLeaves);
		walnutLeaves = new BlockDynamicLeavesFruit("leaves_walnut", BlockDynamicLeavesFruit.fruitTypes.WALNUT);
		registry.register(walnutLeaves);
		chestnutLeaves = new BlockDynamicLeavesFruit("leaves_chestnut", BlockDynamicLeavesFruit.fruitTypes.CHESTNUT);
		registry.register(chestnutLeaves);
		cherryLeaves = new BlockDynamicLeavesFruit("leaves_cherry", BlockDynamicLeavesFruit.fruitTypes.CHERRY);
		registry.register(cherryLeaves);
		lemonLeaves = new BlockDynamicLeavesFruit("leaves_lemon", BlockDynamicLeavesFruit.fruitTypes.LEMON);
		registry.register(lemonLeaves);
		plumLeaves = new BlockDynamicLeavesFruit("leaves_plum", BlockDynamicLeavesFruit.fruitTypes.PLUM);
		registry.register(plumLeaves);

        oakLeavesProperties = setUpLeaves(TreeOak.leavesBlock, TreeOak.leavesMeta, "deciduous");
			fruitAppleLeavesProperties = setUpLeaves(TreeOak.leavesBlock, TreeOak.leavesMeta, "deciduous");
        spruceLeavesProperties = setUpLeaves(TreeSpruce.leavesBlock, TreeSpruce.leavesMeta, "conifer");
		birchLeavesProperties = setUpLeaves(TreeBirch.leavesBlock, TreeBirch.leavesMeta, "deciduous");
        jungleLeavesProperties = setUpLeaves(TreeJungle.leavesBlock, TreeJungle.leavesMeta, "acacia");
        darkOakLeavesProperties = setUpLeaves(TreeDarkOak.leavesBlock, TreeDarkOak.leavesMeta, "darkOak");
        acaciaLeavesProperties = setUpLeaves(TreeAcacia.leavesBlock, TreeAcacia.leavesMeta, "deciduous");

		silverLimeLeavesProperties = setUpLeaves(TreeSilverLime.leavesBlock, TreeSilverLime.leavesMeta, "deciduous");
		walnutLeavesProperties = setUpLeaves(TreeWalnut.leavesBlock, TreeWalnut.leavesMeta, "deciduous");
			fruitWalnutLeavesProperties = setUpLeaves(TreeWalnut.leavesBlock, TreeWalnut.leavesMeta, "deciduous");
		chestnutLeavesProperties = setUpLeaves(TreeChestnut.leavesBlock, TreeChestnut.leavesMeta, "deciduous");
			fruitChestnutLeavesProperties = setUpLeaves(TreeChestnut.leavesBlock, TreeChestnut.leavesMeta, "deciduous");
		cherryLeavesProperties = setUpLeaves(TreeCherry.leavesBlock, TreeCherry.leavesMeta, "acacia");
			fruitCherryLeavesProperties = setUpLeaves(TreeCherry.leavesBlock, TreeCherry.leavesMeta, "acacia");
		lemonLeavesProperties = setUpLeaves(TreeLemon.leavesBlock, TreeLemon.leavesMeta, "deciduous");
			fruitLemonLeavesProperties = setUpLeaves(TreeLemon.leavesBlock, TreeLemon.leavesMeta, "deciduous");
		plumLeavesProperties = setUpLeaves(TreePlum.leavesBlock, TreePlum.leavesMeta, "deciduous");
			fruitPlumLeavesProperties = setUpLeaves(TreePlum.leavesBlock, TreePlum.leavesMeta, "deciduous");
		mapleLeavesProperties = setUpLeaves(TreeMaple.leavesBlock, TreeMaple.leavesMeta, "deciduous", 6, 13);
		larchLeavesProperties = setUpLeaves(TreeLarch.leavesBlock, TreeLarch.leavesMeta, "conifer", 6, 10);
		bullPineLeavesProperties = setUpLeaves(TreeBullPine.leavesBlock, TreeBullPine.leavesMeta, "conifer");
		coastSequoiaLeavesProperties = setUpLeaves(TreeCoastSequoia.leavesBlock, TreeCoastSequoia.leavesMeta, "acacia", 16, 6);
		//giantSequoiaLeavesProperties = setUpLeaves(TreeGiantSequoia.leavesBlock, TreeGiantSequoia.leavesMeta, "acacia", 16, 6);
		teakLeavesProperties = setUpLeaves(TreeTeak.leavesBlock, TreeTeak.leavesMeta, "deciduous");
		ipeLeavesProperties = setUpLeaves(TreeIpe.leavesBlock, TreeIpe.leavesMeta, "acacia", 8, 8);
		kapokLeavesProperties = setUpLeaves(TreeKapok.leavesBlock, TreeKapok.leavesMeta, "acacia", 4, 8);
		ebonyLeavesProperties = setUpLeaves(TreeEbony.leavesBlock, TreeEbony.leavesMeta, "deciduous");
		zebrawoodLeavesProperties = setUpLeaves(TreeZebrawood.leavesBlock, TreeZebrawood.leavesMeta, "deciduous");
		merantiLeavesProperties = setUpLeaves(TreeMeranti.leavesBlock, TreeMeranti.leavesMeta, "deciduous");
		desertAcaciaLeavesProperties = setUpLeaves(TreeDesertAcacia.leavesBlock, TreeDesertAcacia.leavesMeta, "acacia");
		padaukLeavesProperties = setUpLeaves(TreePadauk.leavesBlock, TreePadauk.leavesMeta, "deciduous");
		balsaLeavesProperties = setUpLeaves(TreeBalsa.leavesBlock, TreeBalsa.leavesMeta, "dynamictreesforestry:poplar", 6, 10);
		cocoboloLeavesProperties=setUpLeaves(TreeCocobolo.leavesBlock, TreeCocobolo.leavesMeta, "acacia", 8, 8);
		wengeLeavesProperties = setUpLeaves(TreeWenge.leavesBlock, TreeWenge.leavesMeta, "acacia", 8, 8);
		baobabLeavesProperties = setUpLeaves(TreeBaobab.leavesBlock, TreeBaobab.leavesMeta, "acacia");
		mahoeLeavesProperties = setUpLeaves(TreeMahoe.leavesBlock, TreeMahoe.leavesMeta, "deciduous");
		willowLeavesProperties = setUpLeaves(TreeWillow.leavesBlock, TreeWillow.leavesMeta, "deciduous");
		sipiriLeavesProperties = setUpLeaves( TreeSipiri.leavesBlock, TreeSipiri.leavesMeta, "deciduous", 4, 14);
		papayaLeavesProperties = new LeavesProperties(
				TreePapaya.leavesBlock.getStateFromMeta(TreePapaya.leavesMeta),
				new ItemStack(TreePapaya.leavesBlock,1, TreePapaya.leavesMeta),
				TreeRegistry.findCellKit("palm") ) {
			@Override
			public boolean appearanceChangesWithHydro() {
				return true;
			}
		};
		palmLeavesProperties = new LeavesProperties(
				TreePalm.leavesBlock.getStateFromMeta(TreePalm.leavesMeta),
				new ItemStack(TreePalm.leavesBlock, 1, TreePalm.leavesMeta),
				TreeRegistry.findCellKit("palm") ) {
			@Override
			public boolean appearanceChangesWithHydro() {
				return true;
			}
		};
		poplarLeavesProperties = setUpLeaves(TreePoplar.leavesBlock, TreePoplar.leavesMeta, "dynamictreesforestry:poplar", 12, 10);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 0, oakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 1, spruceLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 2, birchLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 3, jungleLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 4, darkOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 5, acaciaLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 6, silverLimeLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 7, walnutLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 8, chestnutLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 9, cherryLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 10, lemonLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 11, plumLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 12, mapleLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 13, larchLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 14, bullPineLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 15, coastSequoiaLeavesProperties);

		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 16, giantSequoiaLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 17, teakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 18, ipeLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 19, kapokLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 20, ebonyLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 21, zebrawoodLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 22, merantiLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 23, desertAcaciaLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 24, padaukLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 25, balsaLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 26, cocoboloLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 27, wengeLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 28, baobabLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 29, mahoeLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 30, willowLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 31, sipiriLeavesProperties);

		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 32, papayaLeavesProperties);
		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 33, palmLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 34, poplarLeavesProperties);
		//

		palmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState());
		palmFrondLeaves.setProperties(0, palmLeavesProperties);
		papayaLeavesProperties.setDynamicLeavesState(papayaFrondLeaves.getDefaultState());
		papayaFrondLeaves.setProperties(0, papayaLeavesProperties);

		fruitAppleLeavesProperties.setDynamicLeavesState(appleLeaves.getDefaultState());
		appleLeaves.setProperties(fruitAppleLeavesProperties);
		fruitWalnutLeavesProperties.setDynamicLeavesState(walnutLeaves.getDefaultState());
		walnutLeaves.setProperties(fruitWalnutLeavesProperties);
		fruitChestnutLeavesProperties.setDynamicLeavesState(chestnutLeaves.getDefaultState());
		chestnutLeaves.setProperties(fruitChestnutLeavesProperties);
		fruitCherryLeavesProperties.setDynamicLeavesState(cherryLeaves.getDefaultState());
		cherryLeaves.setProperties(fruitCherryLeavesProperties);
		fruitLemonLeavesProperties.setDynamicLeavesState(lemonLeaves.getDefaultState());
		lemonLeaves.setProperties(fruitLemonLeavesProperties);
		fruitPlumLeavesProperties.setDynamicLeavesState(plumLeaves.getDefaultState());
		plumLeaves.setProperties(fruitPlumLeavesProperties);

		TreeFamily oak = new TreeOak();
        TreeFamily birch = new TreeBirch();
        TreeFamily spruce = new TreeSpruce();
        TreeFamily jungle = new TreeJungle();
        TreeFamily darkOak = new TreeDarkOak();
        TreeFamily acacia = new TreeAcacia();

		TreeFamily silverLime = new TreeSilverLime();
		TreeFamily walnut = new TreeWalnut();
		TreeFamily chestnut = new TreeChestnut();
		TreeFamily cherry = new TreeCherry();
		TreeFamily lemon = new TreeLemon();
		TreeFamily plum = new TreePlum();
		TreeFamily maple = new TreeMaple();
		TreeFamily larch = new TreeLarch();
		TreeFamily bullPine = new TreeBullPine();
		TreeFamily coastSequoia = new TreeCoastSequoia();
		//TreeFamily giantSequoia = new TreeGiantSequoia();
		TreeFamily teak = new TreeTeak();
		TreeFamily ipe = new TreeIpe();
		TreeFamily kapok = new TreeKapok();
		TreeFamily ebony = new TreeEbony();
		TreeFamily zebrawood = new TreeZebrawood();
		TreeFamily meranti = new TreeMeranti();
		TreeFamily desertAcacia = new TreeDesertAcacia();
		TreeFamily paduk = new TreePadauk();
		TreeFamily balsa = new TreeBalsa();
		TreeFamily cocobolo = new TreeCocobolo();
		TreeFamily wenge = new TreeWenge();
		TreeFamily baobab = new TreeBaobab();
		TreeFamily mahoe = new TreeMahoe();
		TreeFamily willow = new TreeWillow();
		TreeFamily sipiri = new TreeSipiri();
		TreeFamily papaya = new TreePapaya();
		TreeFamily palm = new TreePalm();
		TreeFamily poplar = new TreePoplar();

		Collections.addAll(trees, oak, birch, spruce, jungle, darkOak, acacia,
				silverLime, walnut, chestnut, cherry, lemon, plum, maple, larch, bullPine, coastSequoia
				//, giantSequoia
				, teak, ipe, kapok, ebony, zebrawood, meranti, desertAcacia, paduk, balsa, cocobolo, wenge
				, baobab, mahoe, willow, sipiri, papaya, palm, poplar
		);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesForestry.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getStateFromMeta(leavesMeta),
				new ItemStack(leavesBlock, 1, leavesMeta),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesMeta);
			}
		};
		return leavesProperties;
	}
	private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit, int smother, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getStateFromMeta(leavesMeta),
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

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		IRecipe seedRecipe = new RecipeSaplingToSeed();
		seedRecipe.setRegistryName(new ResourceLocation(DynamicTreesForestry.MODID, "saplingToSeed"));
		event.getRegistry().register(seedRecipe);

		IRecipe saplingRecipe = new RecipeSeedToSapling();
		saplingRecipe.setRegistryName(new ResourceLocation(DynamicTreesForestry.MODID, "seedToSapling"));
		event.getRegistry().register(saplingRecipe);

		IRecipe fruitRecipe = new RecipeFruitToSapling();
		fruitRecipe.setRegistryName(new ResourceLocation(DynamicTreesForestry.MODID, "fruitToSapling"));
		event.getRegistry().register(fruitRecipe);

		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedApple"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "oak")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Items.APPLE,1,0))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedCherry"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "cherry")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,0))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedWalnut"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "walnut")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,1)),
				Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedChestnut"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "chestnut")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,2)),
				Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedLemon"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "lemon")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,3))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedPlum"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "plum")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,4))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedDate"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "palm")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,5))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedPapaya"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "papaya")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,6))
		);
	}
	private static void setUpSeedRecipes(String name){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotion.DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotion.DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		//createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}

	private static Map<String,String> treeGenomes = new HashMap<String,String>(){{
		put("forestry.treeOak","oak");
		put("forestry.treeDarkOak","darkoak");
		put("forestry.treeBirch","birch");
		put("forestry.treeLime","silverlime");
		put("forestry.treeWalnut","walnut");
		put("forestry.treeChestnut","chestnut");
		put("forestry.treeCherry","cherry");
		put("forestry.treeLemon","lemon");
		put("forestry.treePlum","plum");
		put("forestry.treeMaple","maple");
		put("forestry.treeSpruce","spruce");
		put("forestry.treeLarch","larch");
		put("forestry.treePine","bullPine");
		put("forestry.treeSequoia","coastsequoia");
		put("forestry.treeGigant","giantsequoia");
		put("forestry.treeJungle","jungle");
		put("forestry.treeTeak","teak");
		put("forestry.treeIpe","ipe");
		put("forestry.treeKapok","kapok");
		put("forestry.treeEbony","ebony");
		put("forestry.treeZebrawood","zebrawood");
		put("forestry.treeMahogony","meranti");
		put("forestry.treeAcaciaVanilla","acacia");
		put("forestry.treeAcacia","desertacacia");
		put("forestry.treePadauk","padauk");
		put("forestry.treeBalsa","balsa");
		put("forestry.treeCocobolo","cocobolo");
		put("forestry.treeWenge","wenge");
		put("forestry.treeBaobab","baobab");
		put("forestry.treeMahoe","mahoe");
		put("forestry.treeWillow","willow");
		put("forestry.treeSipiri","sipiri");
		put("forestry.treePapaya","papaya");
		put("forestry.treeDate","palm");
		put("forestry.treePoplar","poplar");
	}};
	private static BiMap<String,String> treeGenomesBidir = HashBiMap.create(treeGenomes);

	public static String GenomeToSpecies (String key, boolean speciesToGenomes){
		if (speciesToGenomes){
			return treeGenomesBidir.inverse().get(key);
		} else {
			return treeGenomesBidir.get(key);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (TreeFamily tree : trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		LeavesPaging.getLeavesMapForModId(DynamicTreesForestry.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(TreeWillow.willowLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(ModContent.palmLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(ModContent.papayaLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());

		ModelLoader.setCustomStateMapper(appleLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(walnutLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(chestnutLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(cherryLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(lemonLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(plumLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
	}
}
