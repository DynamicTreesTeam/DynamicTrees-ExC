package maxhyper.dynamictreesjurassicraft;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesjurassicraft.block.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesjurassicraft.trees.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
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

@Mod.EventBusSubscriber(modid = DynamicTreesJurassiCraft.MODID)
@ObjectHolder(DynamicTreesJurassiCraft.MODID)
public class ModContent {

	public static ILeavesProperties calamitesLeavesProperties, araucariaLeavesProperties,
			ginkgoLeavesProperties, phoenixLeavesProperties, psaroniusLeavesProperties;
	public static BlockDynamicLeavesPalm phoenixFrondLeaves, psaroniusFrondLeaves;

	public static ArrayList<TreeFamily> trees = new ArrayList<>();

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		phoenixFrondLeaves = new BlockDynamicLeavesPalm("leaves_phoenix");
		registry.register(phoenixFrondLeaves);
		psaroniusFrondLeaves = new BlockDynamicLeavesPalm("leaves_psaronius");
		registry.register(psaroniusFrondLeaves);

		calamitesLeavesProperties = setUpLeaves(TreeCalamites.leavesBlock, DynamicTreesJurassiCraft.MODID+":calamites", 6, 13);
		araucariaLeavesProperties = setUpLeaves(TreeAraucaria.leavesBlock, "conifer");
		ginkgoLeavesProperties = setUpLeaves(TreeGinkgo.leavesBlock, DynamicTreesJurassiCraft.MODID+":ginkgo", 8, 13);
		phoenixLeavesProperties = setUpLeaves(TreePhoenix.leavesBlock, "palm");
		psaroniusLeavesProperties = setUpLeaves(TreePsaronius.leavesBlock, "palm");
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesJurassiCraft.MODID, 0, calamitesLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesJurassiCraft.MODID, 1, araucariaLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesJurassiCraft.MODID, 2, ginkgoLeavesProperties);

		phoenixLeavesProperties.setDynamicLeavesState(phoenixFrondLeaves.getDefaultState());
		phoenixFrondLeaves.setProperties(0, phoenixLeavesProperties);
		psaroniusLeavesProperties.setDynamicLeavesState(psaroniusFrondLeaves.getDefaultState());
		psaroniusFrondLeaves.setProperties(0, psaroniusLeavesProperties);

		TreeFamily calaTree = new TreeCalamites();
		TreeFamily arcrTree = new TreeAraucaria();
		TreeFamily gnkoTree = new TreeGinkgo();
		TreeFamily phnxTree = new TreePhoenix();
		TreeFamily psrnTree = new TreePsaronius();
		Collections.addAll(trees, calaTree, arcrTree, gnkoTree, phnxTree, psrnTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesJurassiCraft.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKitString){
		return setUpLeaves(leavesBlock, cellKitString, 4, 13);
	}
	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKitString, int smother, int light){
		return new LeavesProperties(
				leavesBlock.getDefaultState(),
				new ItemStack(leavesBlock, 1, 0),
				TreeRegistry.findCellKit(cellKitString))
		{
			public boolean appearanceChangesWithHydro() {
				return cellKitString.equals("palm");
			}
			@Override public int getSmotherLeavesMax() { return smother; } //Default: 4
			@Override public int getLightRequirement() { return light; } //Default: 13
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, 0);
			}
		};
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		setUpSeedRecipes("araucaria", new ItemStack(TreeAraucaria.saplingBlock));
		setUpSeedRecipes("calamites", new ItemStack(TreeCalamites.saplingBlock));
		setUpSeedRecipes("ginkgo", new ItemStack(TreeGinkgo.saplingBlock));
		setUpSeedRecipes("phoenix", new ItemStack(TreePhoenix.saplingBlock));
		setUpSeedRecipes("psaronius", new ItemStack(TreePsaronius.saplingBlock));
	}

	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesJurassiCraft.MODID, name));
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
	}
}
