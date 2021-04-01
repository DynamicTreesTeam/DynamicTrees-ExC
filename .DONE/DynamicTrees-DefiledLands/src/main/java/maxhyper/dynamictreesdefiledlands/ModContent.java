package maxhyper.dynamictreesdefiledlands;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import lykrast.defiledlands.common.init.ModBlocks;
import maxhyper.dynamictreesdefiledlands.blocks.BlockRootyDefiledDirt;
import maxhyper.dynamictreesdefiledlands.trees.TreeTenebra;
import maxhyper.dynamictreesdefiledlands.worldgen.BiomeDataBasePopulator;
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

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = DynamicTreesDefiledLands.MODID)
@ObjectHolder(DynamicTreesDefiledLands.MODID)
public class ModContent {

	public static ILeavesProperties dyingLeavesProperties;
	public static BlockRooty rootyDefiledDirt;

	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	public static String CORRUPTDIRTLIKE = "corruptdirtlike";
	public static String CORRUPTSANDLIKE = "corruptsandlike";
	public static String CORRUPTGRAVELLIKE = "corruptgravellike";

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		DirtHelper.createNewAdjective(CORRUPTDIRTLIKE);
		DirtHelper.createNewAdjective(CORRUPTSANDLIKE);
		DirtHelper.createNewAdjective(CORRUPTGRAVELLIKE);

		IForgeRegistry<Block> registry = event.getRegistry();

		rootyDefiledDirt = new BlockRootyDefiledDirt(false);
		registry.register(rootyDefiledDirt);

		dyingLeavesProperties = new LeavesProperties(ModBlocks.tenebraLeaves.getDefaultState(), TreeRegistry.findCellKit("dynamictreesdefiledlands:sparse") ) {
			@Override public int getSmotherLeavesMax() { return 1; }
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(ModBlocks.tenebraLeaves);
			}
		};

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesDefiledLands.MODID, 0, dyingLeavesProperties);

		TreeFamily defiled = new TreeTenebra();

		Collections.addAll(trees, defiled);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesDefiledLands.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));

		DirtHelper.registerSoil(rootyDefiledDirt, CORRUPTDIRTLIKE);
		DirtHelper.registerSoil(ModBlocks.dirtDefiled, CORRUPTDIRTLIKE);
		DirtHelper.registerSoil(ModBlocks.grassDefiled, CORRUPTDIRTLIKE);
		DirtHelper.registerSoil(ModBlocks.sandDefiled, CORRUPTSANDLIKE);
		DirtHelper.registerSoil(ModBlocks.gravelDefiled, CORRUPTGRAVELLIKE);
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesDefiledLands.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesDefiledLands.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(rootyDefiledDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
	}
}
