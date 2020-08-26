package maxhyper.dynamictreesfossil;

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

import maxhyper.dynamictreesfossil.trees.*;
import maxhyper.dynamictreesfossil.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
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

@Mod.EventBusSubscriber(modid = DynamicTreesFossil.MODID)
@ObjectHolder(DynamicTreesFossil.MODID)
public class ModContent {

	public static BlockDynamicLeaves fejuniperLeaves;
	public static Seed fejuniperSeedBurnt;
	public static BlockRooty rootySlimyDirt;
	public static ILeavesProperties calamitesLeavesProperties, cordaitesLeavesProperties,
			palaeorapheLeavesProperties, sigillariaLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

			calamitesLeavesProperties = setUpLeaves(FsArTreeCalamites.leavesBlock, "deciduous");
			cordaitesLeavesProperties = setUpLeaves(FsArTreeCordaites.leavesBlock, "deciduous");
			palaeorapheLeavesProperties = setUpLeaves(FsArTreePalaeoraphe.leavesBlock, "deciduous");
			sigillariaLeavesProperties = setUpLeaves(FsArTreeSigillaria.leavesBlock, "deciduous");
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesFossil.MODID, 0, calamitesLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesFossil.MODID, 1, cordaitesLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesFossil.MODID, 2, palaeorapheLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesFossil.MODID, 3, sigillariaLeavesProperties);

			TreeFamily calaTree = new FsArTreeCalamites();
			TreeFamily cordTree = new FsArTreeCordaites();
			TreeFamily palaTree = new FsArTreePalaeoraphe();
			TreeFamily sigiTree = new FsArTreeSigillaria();
			Collections.addAll(trees, calaTree, cordTree, palaTree, sigiTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesFossil.MODID).values());
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

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

			setUpSeedRecipes("calamites", new ItemStack(FsArTreeCalamites.saplingBlock));
			setUpSeedRecipes("cordaites", new ItemStack(FsArTreeCordaites.saplingBlock));
			setUpSeedRecipes("palaeoraphe", new ItemStack(FsArTreePalaeoraphe.saplingBlock));
			setUpSeedRecipes("sigillaria", new ItemStack(FsArTreeSigillaria.saplingBlock));

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesFossil.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}
	public static void setUpSeedRecipes (String name, ItemStack sapling, ItemStack seed){
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesFossil.MODID, name+"seed"), (ResourceLocation)null, seed, Ingredient.fromStacks(sapling), Ingredient.fromItem(ModItems.dirtBucket));
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesFossil.MODID, name+"sapling"), (ResourceLocation)null, sapling, Ingredient.fromStacks(seed), Ingredient.fromItem(ModItems.dirtBucket));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesFossil.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

	}
}
