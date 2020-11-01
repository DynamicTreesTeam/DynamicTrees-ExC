package maxhyper.dynamictreesquark;

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

import maxhyper.dynamictreesquark.trees.*;
import maxhyper.dynamictreesquark.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.quark.world.block.BlockVariantLeaves;
import vazkii.quark.world.feature.TreeVariants;

@Mod.EventBusSubscriber(modid = DynamicTreesQuark.MODID)
@ObjectHolder(DynamicTreesQuark.MODID)
public class ModContent {

	public static ILeavesProperties blossomingLeavesProperties, swampOakLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	static boolean messageSent = false;
	static boolean failedToLoad = false;

	@SubscribeEvent
	public static void onEvent(EntityJoinWorldEvent event)
	{
		if (!messageSent && (event.getEntity() instanceof EntityPlayer))
		{
			if (failedToLoad){
				event.getEntity().sendMessage(new TextComponentString("Dynamic Trees for Quark: Failed to load. Maybe Tree Variants are disabled in Quark config?"));
				messageSent = true;
			} else if (TreeVariants.enableSakura || TreeVariants.enableSwamp){
				event.getEntity().sendMessage(new TextComponentString("Dynamic Trees for Quark: To prevent non-dynamic trees from spawning please disable Quark's Blossoming and Swamp trees (INDIVIDUALLY) in Quark world settings."));
				event.getEntity().sendMessage(new TextComponentString("Disabling Tree Variants as a whole will disable the Dynamic Trees for Quark Addon as well."));
				messageSent = true;
			}
		}

	}

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		try {
			swampOakLeavesProperties = setUpLeaves(TreeSwampOak.leavesBlock, TreeSwampOak.leavesBlock.getDefaultState().withProperty(BlockVariantLeaves.VARIANT, BlockVariantLeaves.Variant.SWAMP_LEAVES), 0, "deciduous", 3, 13);
			blossomingLeavesProperties = setUpLeaves(TreeBlossoming.leavesBlock, TreeSwampOak.leavesBlock.getDefaultState().withProperty(BlockVariantLeaves.VARIANT, BlockVariantLeaves.Variant.SAKURA_LEAVES), 1, "deciduous", 4, 13);

			LeavesPaging.getLeavesBlockForSequence(DynamicTreesQuark.MODID, 0, blossomingLeavesProperties);
			LeavesPaging.getLeavesBlockForSequence(DynamicTreesQuark.MODID, 1, swampOakLeavesProperties);


			TreeFamily blossomingTree = new TreeBlossoming();
			TreeFamily swampOakTree = new TreeSwampOak();
			Collections.addAll(trees, blossomingTree, swampOakTree);

			trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
			ArrayList<Block> treeBlocks = new ArrayList<>();
			trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
			treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesQuark.MODID).values());
			registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
		}catch (Exception e){
			failedToLoad = true;
		}
			Block glowcelium = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("quark","glowcelium"));
			if (glowcelium != Blocks.AIR)
				DirtHelper.registerSoil(glowcelium, DirtHelper.FUNGUSLIKE);
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

		if (!failedToLoad) {
			setUpSeedRecipes("blossoming", new ItemStack(TreeBlossoming.saplingBlock, 1, 1));
			setUpSeedRecipes("swampOak", new ItemStack(TreeSwampOak.saplingBlock, 1, 0));
		}
	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesQuark.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesQuark.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
	}
}
