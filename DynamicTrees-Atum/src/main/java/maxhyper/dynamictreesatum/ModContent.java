package maxhyper.dynamictreesatum;

import java.util.ArrayList;
import java.util.Collections;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import maxhyper.dynamictreesatum.blocks.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesatum.trees.*;
import maxhyper.dynamictreesatum.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
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

@Mod.EventBusSubscriber(modid = DynamicTreesAtum.MODID)
@ObjectHolder(DynamicTreesAtum.MODID)
public class ModContent {

	public static BlockDynamicLeaves palmLeaves;
	public static ILeavesProperties palmLeavesProperties, deadPalmLeavesProperties, nullLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		palmLeaves = new BlockDynamicLeavesPalm();
		registry.register(palmLeaves);

		palmLeavesProperties = new LeavesProperties(
				A2TreePalm.leavesBlock.getDefaultState(),
				new ItemStack(A2TreePalm.leavesBlock),
				TreeRegistry.findCellKit("deciduous") ) {
		};
		deadPalmLeavesProperties = new LeavesProperties(
				A2TreeDeadPalm.leavesBlock.getDefaultState(),
				new ItemStack(A2TreeDeadPalm.leavesBlock),
				TreeRegistry.findCellKit("deciduous") ) {
		};
		nullLeavesProperties = new LeavesProperties(
				A2TreeDeadPalm.leavesBlock.getDefaultState(),
				ItemStack.EMPTY,
				TreeRegistry.findCellKit("bare") ) {
		};
		palmLeavesProperties.setDynamicLeavesState(palmLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		palmLeaves.setProperties(0, palmLeavesProperties);
		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesAtum.MODID, 0, palmLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesAtum.MODID, 0, deadPalmLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesAtum.MODID, 1, nullLeavesProperties);

		TreeFamily palmTree = new A2TreePalm();
		TreeFamily palmDeadTree = new A2TreeDeadPalm();
		TreeFamily deadTree = new A2TreeDeadTree();
		Collections.addAll(trees, palmTree, palmDeadTree, deadTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesAtum.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		setUpSeedRecipes("palm", new ItemStack(A2TreePalm.saplingBlock));

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesAtum.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesAtum.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(palmLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());

	}
}
