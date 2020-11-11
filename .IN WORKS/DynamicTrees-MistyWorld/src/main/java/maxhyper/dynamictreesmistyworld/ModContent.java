package maxhyper.dynamictreesmistyworld;

import java.util.ArrayList;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import maxhyper.dynamictreesmistyworld.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
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
import ru.liahim.mist.api.block.MistBlocks;
import ru.liahim.mist.init.ModBlocks;

@Mod.EventBusSubscriber(modid = DynamicTreesMistyWorld.MODID)
@ObjectHolder(DynamicTreesMistyWorld.MODID)
public class ModContent {

	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		///Collections.addAll(trees);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesMistyWorld.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));

		DirtHelper.registerSoil(MistBlocks.GRASS_C, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.GRASS_F, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.GRASS_R, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.GRASS_S, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.GRASS_T, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.DIRT_C, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.DIRT_F, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.DIRT_R, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.DIRT_S, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.DIRT_T, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.HUMUS_GRASS, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.HUMUS_DIRT, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(MistBlocks.HUMUS_GRASS, DirtHelper.MUDLIKE);
		DirtHelper.registerSoil(MistBlocks.HUMUS_DIRT, DirtHelper.MUDLIKE);
		DirtHelper.registerSoil(MistBlocks.SAND, DirtHelper.SANDLIKE);
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

			@Override
			public int getLightRequirement() {
				return 1;
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

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesMistyWorld.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesMistyWorld.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
	}
}
