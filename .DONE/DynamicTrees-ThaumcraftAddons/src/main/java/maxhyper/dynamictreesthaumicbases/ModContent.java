package maxhyper.dynamictreesthaumicbases;

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
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import com.rumaruka.thaumicbases.init.TBBlocks;
import maxhyper.dynamictreesthaumicbases.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Items;
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

@Mod.EventBusSubscriber(modid = DynamicTreesThaumcraftAddons.MODID)
@ObjectHolder(DynamicTreesThaumcraftAddons.MODID)
public class ModContent {

	public static BlockFruit blockGoldenApple, blockEnderPearl, blockMagmaCream;
	public static ILeavesProperties goldenOakLeavesProperties, enderOakLeavesProperties, hellishOakLeavesProperties, cactusLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	public static CactusRainbow rainbowCactus;

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		//
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		blockGoldenApple = (new BlockFruit("fruitgolden")).setDroppedItem(new ItemStack(Items.GOLDEN_APPLE));
		registry.register(blockGoldenApple);
		blockEnderPearl = (new BlockFruit("fruitender")).setDroppedItem(new ItemStack(Items.ENDER_PEARL));
		registry.register(blockEnderPearl);
		blockMagmaCream = (new BlockFruit("fruitmagma")).setDroppedItem(new ItemStack(Items.MAGMA_CREAM));
		registry.register(blockMagmaCream);

		goldenOakLeavesProperties = setUpLeaves(TBTreeGoldenOak.leavesBlock,  "deciduous");
		enderOakLeavesProperties = setUpLeaves(TBTreeEnderOak.leavesBlock,  "deciduous");
		hellishOakLeavesProperties = setUpLeaves(TBTreeHellishOak.leavesBlock,  "deciduous");

		cactusLeavesProperties = new LeavesProperties(null, ItemStack.EMPTY, TreeRegistry.findCellKit("bare"));

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesThaumcraftAddons.MODID, 0, goldenOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesThaumcraftAddons.MODID, 1, enderOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesThaumcraftAddons.MODID, 2, hellishOakLeavesProperties);

		TreeFamily goldenOakTree = new TBTreeGoldenOak();
		TreeFamily enderOakTree = new TBTreeEnderOak();
		TreeFamily hellishOakTree = new TBTreeHellishOak();
		Collections.addAll(trees, goldenOakTree, enderOakTree, hellishOakTree);

		rainbowCactus = new CactusRainbow();
		rainbowCactus.registerSpecies(Species.REGISTRY);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		rainbowCactus.getRegisterableBlocks(treeBlocks);
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesThaumcraftAddons.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));

		DirtHelper.registerSoil(TBBlocks.oldgravel, DirtHelper.GRAVELLIKE);
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getDefaultState(),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, 0);
			}
		};
		return leavesProperties;
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		rainbowCactus.getRegisterableItems(treeItems);

		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

			setUpSeedRecipes("goldenOak", new ItemStack(TBTreeGoldenOak.saplingBlock));
			setUpSeedRecipes("enderOak", new ItemStack(TBTreeEnderOak.saplingBlock));
			setUpSeedRecipes("hellishOak", new ItemStack(TBTreeHellishOak.saplingBlock));

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesThaumcraftAddons.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(rainbowCactus.getDynamicBranch(), new StateMap.Builder().ignore(BlockBranchCactus.TRUNK, BlockBranchCactus.ORIGIN).build());
		ModelHelper.regModel(rainbowCactus.getDynamicBranch());
		ModelHelper.regModel(rainbowCactus.getCommonSpecies().getSeed());

	}
}
