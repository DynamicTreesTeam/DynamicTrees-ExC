package maxhyper.dynamictreestheaether2;

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

import maxhyper.dynamictreestheaether2.blocks.BlockDynamicLeavesAether;
import maxhyper.dynamictreestheaether2.trees.*;
import maxhyper.dynamictreestheaether2.worldgen.BiomeDataBasePopulator;
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

@Mod.EventBusSubscriber(modid = DynamicTreesTheAether2.MODID)
@ObjectHolder(DynamicTreesTheAether2.MODID)
public class ModContent {

	public static BlockDynamicLeaves specialLeaves, skyrootLeaves, wisprootLeaves, greatrootLeaves;
	public static ILeavesProperties greenSkyrootLeavesProperties, blueSkyrootLeavesProperties, darkblueSkyrootLeavesProperties,
			greenWisprootLeavesProperties, blueWisprootLeavesProperties, darkblueWisprootLeavesProperties,
			greenGreatrootLeavesProperties, blueGreatrootLeavesProperties, darkblueGreatrootLeavesProperties,
			amberootLeavesProperties, holidayLeavesProperties, holidayDecorLeavesProperties, therawoodLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		specialLeaves = new BlockDynamicLeavesAether("leaves_special");
		registry.register(specialLeaves);
		skyrootLeaves = new BlockDynamicLeavesAether("leaves_skyroot");
		registry.register(skyrootLeaves);
		wisprootLeaves = new BlockDynamicLeavesAether("leaves_wisproot");
		registry.register(wisprootLeaves);
		greatrootLeaves = new BlockDynamicLeavesAether("leaves_greatroot");
		registry.register(greatrootLeaves);

		greenSkyrootLeavesProperties = setUpLeaves(A2TreeGreenSkyroot.leavesBlock, "conifer", 4, 13);
		greenWisprootLeavesProperties = setUpLeaves(A2TreeGreenWisproot.leavesBlock, "conifer", 4, 13);
		greenGreatrootLeavesProperties = setUpLeaves(A2TreeGreenGreatroot.leavesBlock, "conifer", 4, 13);
		amberootLeavesProperties = setUpLeaves(A2TreeAmberoot.leavesBlock, "conifer", 2, 10);
		holidayLeavesProperties = setUpLeaves(A2TreeHoliday.leavesBlock, "conifer", 2, 4);
		holidayDecorLeavesProperties = setUpLeaves(A2TreeHoliday.leavesBlock2, "conifer", 2, 4);
		therawoodLeavesProperties = setUpLeaves(A2TreeTherawood.leavesBlock, "conifer", 4, 13);

		amberootLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		holidayLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		holidayDecorLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 2));
		therawoodLeavesProperties.setDynamicLeavesState(specialLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 3));
		specialLeaves.setProperties(0, amberootLeavesProperties);
		specialLeaves.setProperties(1, holidayLeavesProperties);
		specialLeaves.setProperties(2, holidayDecorLeavesProperties);
		specialLeaves.setProperties(3, therawoodLeavesProperties);

		greenSkyrootLeavesProperties.setDynamicLeavesState(skyrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		skyrootLeaves.setProperties(0, greenSkyrootLeavesProperties);

		greenWisprootLeavesProperties.setDynamicLeavesState(wisprootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		wisprootLeaves.setProperties(0, greenWisprootLeavesProperties);

		greenGreatrootLeavesProperties.setDynamicLeavesState(greatrootLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		greatrootLeaves.setProperties(0, greenGreatrootLeavesProperties);

		TreeFamily skyrootTree1 = new A2TreeGreenSkyroot();
		//TreeFamily skyrootTree2 = new A2TreeBlueSkyroot();
		//TreeFamily skyrootTree3 = new A2TreeDarkblueSkyroot();
		TreeFamily wisprootTree1 = new A2TreeGreenWisproot();

		TreeFamily greatrootTree1 = new A2TreeGreenGreatroot();

		TreeFamily goldenOakTree = new A2TreeAmberoot();
		TreeFamily holidayTree = new A2TreeHoliday();
		TreeFamily therawoodTree = new A2TreeTherawood();
		Collections.addAll(trees, skyrootTree1, wisprootTree1, greatrootTree1, goldenOakTree, holidayTree, therawoodTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTheAether2.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	private static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit, int smother, int light){
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

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		setUpSeedRecipes("greenskyroot", new ItemStack(A2TreeGreenSkyroot.saplingBlock, 1, 0));
		//setUpSeedRecipes("blueskyroot", new ItemStack(A2TreeBlueSkyroot.saplingBlock, 1, 1));
		//setUpSeedRecipes("darkblueskyroot", new ItemStack(A2TreeDarkBlueSkyroot.saplingBlock, 1, 2));
		setUpSeedRecipes("greenwisproot", new ItemStack(A2TreeGreenWisproot.saplingBlock, 1, 0));

		setUpSeedRecipes("greengreatroot", new ItemStack(A2TreeGreenGreatroot.saplingBlock, 1, 0));

		setUpSeedRecipes("amberoot", new ItemStack(A2TreeAmberoot.saplingBlock, 1, 0));
		setUpSeedRecipes("holiday", new ItemStack(A2TreeHoliday.saplingBlock, 1, 1));
	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether2.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesTheAether2.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(specialLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(skyrootLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(wisprootLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
		ModelLoader.setCustomStateMapper(greatrootLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());

	}
}
