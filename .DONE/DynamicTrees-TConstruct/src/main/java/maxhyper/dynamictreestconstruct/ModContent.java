package maxhyper.dynamictreestconstruct;

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

import maxhyper.dynamictreestconstruct.blocks.*;
import maxhyper.dynamictreestconstruct.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.BlockSlimeGrass;

@Mod.EventBusSubscriber(modid = DynamicTreesTConstruct.MODID)
@ObjectHolder(DynamicTreesTConstruct.MODID)
public class ModContent {

	public static BlockBranch slimeBlueBranch, slimePurpleBranch, slimeMagmaBranch;
	public static BlockRooty rootySlimyDirt;
	public static BlockFruit blockGreenSlime, blockBlueSlime, blockPurpleSlime, blockMagmaSlime;
	public static ILeavesProperties blueSlimeLeavesProperties, purpleSlimeLeavesProperties, magmaSlimeLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	static boolean messageSent = false;
	public static boolean genSlimeIslands;

	@SubscribeEvent
	public static void onEvent(EntityJoinWorldEvent event)
	{
		if (!messageSent && (event.getEntity() instanceof EntityPlayer))
		{
			if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld")){
				event.getEntity().sendMessage(new TextComponentString("Dynamic Trees for Tinkers' Construct: Failed to load. Tinker World module is disabled in Tinkers' Construct Config."));
				messageSent = true;
			}
		}

	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld"))
			return;
		IForgeRegistry<Block> registry = event.getRegistry();

		blockGreenSlime = (new BlockFruit("fruitgreenslime"));
		registry.register(blockGreenSlime);
		blockBlueSlime = (new BlockFruit("fruitblueslime"));
		registry.register(blockBlueSlime);
		blockPurpleSlime = (new BlockFruit("fruitpurpleslime"));
		registry.register(blockPurpleSlime);
		blockMagmaSlime = (new BlockFruit("fruitmagmaslime"));
		registry.register(blockMagmaSlime);

		slimeBlueBranch = new BlockDynamicBranchSlime("slimebluebranch");
		slimePurpleBranch = new BlockDynamicBranchSlime("slimepurplebranch");
		slimeMagmaBranch = new BlockDynamicBranchSlime("slimemagmabranch");

		rootySlimyDirt = new BlockRootySlimyDirt(false);
		registry.register(rootySlimyDirt);

		blueSlimeLeavesProperties = new LeavesProperties(
				TreeSlimeBlue.leavesBlock.getDefaultState().withProperty(BlockSlimeGrass.FOLIAGE, BlockSlimeGrass.FoliageType.BLUE),
				new ItemStack(TreeSlimeBlue.leavesBlock, 1, 0),
				TreeRegistry.findCellKit("conifer"))
		{
			@Override public int getSmotherLeavesMax() {
				return 8;
			}
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(TreeSlimeBlue.leavesBlock, 1, 0);
			}
			@Override public int getFlammability() {
				return 0;
			}
			@Override public int getFireSpreadSpeed() { return 0; }
		};
		purpleSlimeLeavesProperties = new LeavesProperties(
				TreeSlimeBlue.leavesBlock.getDefaultState().withProperty(BlockSlimeGrass.FOLIAGE, BlockSlimeGrass.FoliageType.PURPLE),
				new ItemStack(TreeSlimeBlue.leavesBlock, 1, 1),
				TreeRegistry.findCellKit("conifer"))
		{
			@Override public int getSmotherLeavesMax() {
				return 8;
			}
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(TreeSlimePurple.leavesBlock, 1, 1);
			}
			@Override public int getFlammability() {
				return 0;
			}
			@Override public int getFireSpreadSpeed() { return 0; }
		};
		magmaSlimeLeavesProperties = new LeavesProperties(
				TreeSlimeMagma.leavesBlock.getDefaultState().withProperty(BlockSlimeGrass.FOLIAGE, BlockSlimeGrass.FoliageType.ORANGE),
				new ItemStack(TreeSlimeMagma.leavesBlock, 1, 2),
				TreeRegistry.findCellKit("conifer"))
		{
			@Override public int getLightRequirement() { return 0; }
			@Override public int getSmotherLeavesMax() {
				return 8;
			}
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(TreeSlimeMagma.leavesBlock, 1, 2);
			}
			@Override public int getFlammability() {
				return 0;
			}
			@Override public int getFireSpreadSpeed() { return 0; }
		};

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTConstruct.MODID, 0, blueSlimeLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTConstruct.MODID, 1, purpleSlimeLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTConstruct.MODID, 2, magmaSlimeLeavesProperties);

		TreeFamily blueSlimeTree = new TreeSlimeBlue();
		TreeFamily purpleSlimeTree = new TreeSlimePurple();
		TreeFamily magmaSlimeTree = new TreeSlimeMagma();
		Collections.addAll(trees, blueSlimeTree, purpleSlimeTree, magmaSlimeTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesTConstruct.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[0]));

		DirtHelper.registerSoil(TinkerWorld.slimeGrass, DirtHelper.SLIMELIKE);
		DirtHelper.registerSoil(TinkerWorld.slimeGrass, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(TinkerWorld.slimeDirt, DirtHelper.SLIMELIKE);
		DirtHelper.registerSoil(TinkerWorld.slimeDirt, DirtHelper.DIRTLIKE);
		DirtHelper.registerSoil(rootySlimyDirt, DirtHelper.SLIMELIKE);
		DirtHelper.registerSoil(rootySlimyDirt, DirtHelper.DIRTLIKE);
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld"))
			return;
		IForgeRegistry<Item> registry = event.getRegistry();

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld"))
			return;
		setUpSeedRecipes("slimeBlue", new ItemStack(TreeSlimeBlue.saplingBlock, 1, 0));
		setUpSeedRecipes("slimePurple", new ItemStack(TreeSlimePurple.saplingBlock, 1, 1));
		setUpSeedRecipes("slimeMagma", new ItemStack(TreeSlimeMagma.saplingBlock, 1, 2));

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		if (!TConstruct.pulseManager.isPulseLoaded("TinkerWorld"))
			return;
		for (TreeFamily tree : trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		LeavesPaging.getLeavesMapForModId(DynamicTreesTConstruct.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelLoader.setCustomStateMapper(rootySlimyDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
	}
}
