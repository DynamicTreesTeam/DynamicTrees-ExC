package maxhyper.dynamictreestbl;

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
import maxhyper.dynamictreestbl.blocks.BlockDynamicBranchRubber;
import maxhyper.dynamictreestbl.blocks.BlockDynamicRubberTap;
import maxhyper.dynamictreestbl.trees.TreeRubber;
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
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = DynamicTreesTBL.MODID)
@ObjectHolder(DynamicTreesTBL.MODID)
public class ModContent {

	public static BlockBranch rubberBranch;
	public static ILeavesProperties rubberLeavesProperties;

	public static Block dynamicWeedwoodRubberTap, dynamicSyrmoriteRubberTap;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		rubberBranch = new BlockDynamicBranchRubber();
		registry.register(rubberBranch);

		dynamicWeedwoodRubberTap = new BlockDynamicRubberTap(BlockRegistry.WEEDWOOD_PLANKS.getDefaultState(), 540) {
			@Override protected ItemStack getBucket(boolean withRubber) {
				return new ItemStack(withRubber ? ItemRegistry.BL_BUCKET_RUBBER: ItemRegistry.BL_BUCKET, 1, 0);
			}
		};
		dynamicSyrmoriteRubberTap = new BlockDynamicRubberTap(BlockRegistry.SYRMORITE_BLOCK.getDefaultState(), 260) {
			@Override protected ItemStack getBucket(boolean withRubber) {
				return new ItemStack(withRubber ? ItemRegistry.BL_BUCKET_RUBBER: ItemRegistry.BL_BUCKET, 1, 1);
			}
		};
		registry.register(dynamicWeedwoodRubberTap.setRegistryName(DynamicTreesTBL.MODID, "dynamic_weedwood_rubber_tap"));
		registry.register(dynamicSyrmoriteRubberTap.setRegistryName(DynamicTreesTBL.MODID, "dynamic_syrmorite_rubber_tap"));

		rubberLeavesProperties = setUpLeaves(TreeRubber.leavesBlock, 0, "deciduous", 2, 13);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTBL.MODID, 0, rubberLeavesProperties);

		TreeFamily rubberTree = new TreeRubber();

		Collections.addAll(trees, rubberTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTBL.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getDefaultState(),
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
				leavesBlock.getDefaultState(),
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

		setUpSeedRecipes("rubber", new ItemStack(TreeRubber.saplingBlock, 1, 0));

	}
	private static void setUpSeedRecipes(String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTBL.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesTBL.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
	}
}
