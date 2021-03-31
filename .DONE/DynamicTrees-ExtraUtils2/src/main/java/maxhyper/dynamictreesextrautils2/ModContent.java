package maxhyper.dynamictreesextrautils2;

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
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import maxhyper.dynamictreesextrautils2.blocks.*;
import maxhyper.dynamictreesextrautils2.items.ItemDynamicSeedBurntFeJuniper;
import maxhyper.dynamictreesextrautils2.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DynamicTreesExtraUtils2.MODID)
@ObjectHolder(DynamicTreesExtraUtils2.MODID)
public class ModContent {

	public static BlockDynamicLeaves fejuniperLeaves;
	public static BlockBranch fejuniperBranchRaw, fejuniperBranchBurnt;
	public static Seed fejuniperSeedBurnt;
	public static BlockDynamicSapling fejuniperSaplingBurnt;
	public static ILeavesProperties fejuniperLeavesRawProperties, fejuniperLeavesBurntProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		fejuniperBranchRaw = new BlockBranchFeJuniper();
		fejuniperBranchBurnt = new BlockBranchFeJuniper(true);
		registry.register(fejuniperBranchBurnt);

		fejuniperLeaves = new BlockDynamicLeavesFeJuniper();
		registry.register(fejuniperLeaves);

		fejuniperSaplingBurnt = new BlockDynamicSaplingBurntFeJuniper();
		registry.register(fejuniperSaplingBurnt);

		fejuniperLeavesRawProperties = setUpLeaves(TreeFeJuniper.leavesBlock, TreeFeJuniper.leavesStateRaw, 0, "conifer", 4, 13);
		fejuniperLeavesBurntProperties = setUpLeaves(TreeFeJuniper.leavesBlock, TreeFeJuniper.leavesStateBurnt, 1,"conifer", 4, 13);

		fejuniperLeavesRawProperties.setDynamicLeavesState(fejuniperLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		fejuniperLeavesBurntProperties.setDynamicLeavesState(fejuniperLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		fejuniperLeaves.setProperties(0, fejuniperLeavesRawProperties);
		fejuniperLeaves.setProperties(1, fejuniperLeavesBurntProperties);

		TreeFamily feJuniperTree = new TreeFeJuniper();
		Collections.addAll(trees, feJuniperTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesExtraUtils2.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));
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

			fejuniperSeedBurnt = new ItemDynamicSeedBurntFeJuniper();
			registry.register(fejuniperSeedBurnt);

			registry.register(new ItemBlock(fejuniperBranchBurnt).setRegistryName(Objects.requireNonNull(fejuniperBranchBurnt.getRegistryName())));

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		setUpSeedRecipes("ferrousJuniper", new ItemStack(TreeFeJuniper.saplingBlock));
		setUpSeedRecipes("ferrousJuniperBurnt", new ItemStack(TreeFeJuniper.saplingBlock, 1, 1), new ItemStack(fejuniperSeedBurnt));
	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExtraUtils2.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}
	public static void setUpSeedRecipes (String name, ItemStack sapling, ItemStack seed){
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesExtraUtils2.MODID, name+"seed"), (ResourceLocation)null, seed, Ingredient.fromStacks(sapling), Ingredient.fromItem(ModItems.dirtBucket));
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesExtraUtils2.MODID, name+"sapling"), (ResourceLocation)null, sapling, Ingredient.fromStacks(seed), Ingredient.fromItem(ModItems.dirtBucket));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesExtraUtils2.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelHelper.regModel(fejuniperSeedBurnt);
		ModelHelper.regModel(fejuniperBranchBurnt);
		ModelLoader.setCustomStateMapper(fejuniperLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());

	}
}
