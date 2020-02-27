package maxhyper.dynamictreesexc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

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
import maxhyper.dynamictreesexc.compat.CompatEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
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
			menrilBranch, menrilBranchFilled,
			rubberBranch, rubberBranchFilled, slimeBlueBranch, slimePurpleBranch, slimeMagmaBranch;
	public static Seed fejuniperSeedBurnt;
	public static BlockDynamicSapling fejuniperSaplingBurnt;
	public static BlockSurfaceRoot menrilRoot;
	public static BlockRooty rootySlimyDirt;
	public static BlockFruit blockGoldenApple, blockEnderPearl, blockMagmaCream;
	public static ILeavesProperties menrilLeavesProperties, rubberLeavesProperties,
			blueSlimeLeavesProperties, purpleSlimeLeavesProperties, magmaSlimeLeavesProperties,
			blossomingLeavesProperties, swampOakLeavesProperties,
			goldenOakLeavesProperties, enderOakLeavesProperties, hellishOakLeavesProperties,
			fejuniperLeavesRawProperties, fejuniperLeavesBurntProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {

	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		if (Loader.isModLoaded("integrateddynamics")) {
			CompatEvents.RegisterBlocksIntegratedDynamics(registry);
		}
		if (Loader.isModLoaded("tconstruct")) {
			CompatEvents.RegisterBlocksTinkersConstruct(registry);
		}
		if (Loader.isModLoaded("thaumicbases")) {
			CompatEvents.RegisterBlocksThaumicBases(registry);
		}
		if (Loader.isModLoaded("extrautils2")) {
			CompatEvents.RegisterBlocksExtraUtils2(registry);
		}
		if (Loader.isModLoaded("techreborn")) {
			CompatEvents.RegisterBlocksTechReborn(registry);
		}
		if (Loader.isModLoaded("quark")) {
			CompatEvents.RegisterBlocksQuark(registry);
		}

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesExC.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit){
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
	public static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit, int smother, int light){
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

		if (Loader.isModLoaded("extrautils2")) {
			CompatEvents.RegisterItemsExtraUtils2(registry);
		}
		if (Loader.isModLoaded("integrateddynamics")) {
			CompatEvents.RegisterItemsIntegratedDynamics(registry);
		}
		if (Loader.isModLoaded("techreborn")) {
			CompatEvents.RegisterItemsTechReborn(registry);
		}

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		if (Loader.isModLoaded("integrateddynamics")) {
			CompatEvents.RegisterRecipesIntegratedDynamics();
		}
		if (Loader.isModLoaded("tconstruct")) {
			CompatEvents.RegisterRecipesTinkersConstruct();
		}
		if (Loader.isModLoaded("thaumicbases")) {
			CompatEvents.RegisterRecipesThaumicBases();
		}
		if (Loader.isModLoaded("extrautils2")) {
			CompatEvents.RegisterRecipesExtraUtils2();
		}
		if (Loader.isModLoaded("techreborn")) {
			CompatEvents.RegisterRecipesTechReborn();
		}
		if (Loader.isModLoaded("quark")) {
			CompatEvents.RegisterRecipesQuark();
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
			CompatEvents.RegisterModelsIntegratedDynamics();
		}
		if (Loader.isModLoaded("tconstruct")) {
			CompatEvents.RegisterModelsTinkersConstruct();
		}
		if (Loader.isModLoaded("extrautils2")) {
			CompatEvents.RegisterModelsExtraUtils2();
		}
		if (Loader.isModLoaded("techreborn")) {
			CompatEvents.RegisterModelsTechReborn();
		}

	}
}
