package maxhyper.dynamictreestbl;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestbl.blocks.BlockDynamicHearthgroveRoots;
import maxhyper.dynamictreestbl.blocks.BlockDynamicBranchRubber;
import maxhyper.dynamictreestbl.blocks.BlockDynamicRubberTap;
import maxhyper.dynamictreestbl.trees.TreeHearthgrove;
import maxhyper.dynamictreestbl.trees.TreeNibbletwig;
import maxhyper.dynamictreestbl.trees.TreeRubber;
import maxhyper.dynamictreestbl.trees.TreeSap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
import java.util.List;

@Mod.EventBusSubscriber(modid = DynamicTreesTBL.MODID)
@ObjectHolder(DynamicTreesTBL.MODID)
public class ModContent {

	public static BlockBranch rubberBranch, hearthgroveBranch;
	public static ILeavesProperties rubberLeavesProperties, sapLeavesProperties, hearthgroveLeavesProperties, nibbletwigLeavesProperties;

	public static Block dynamicWeedwoodRubberTap, dynamicSyrmoriteRubberTap;
	public static BlockDynamicHearthgroveRoots undergroundHearthgroveRoot, undergroundHearthgroveRootSwamp;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		rubberBranch = new BlockDynamicBranchRubber();
		hearthgroveBranch = new BlockBranchBasic("hearthgrovebranch"){
			@Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
				return false;
			}
			@Override public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
				ItemStack handStack = playerIn.getHeldItemMainhand();
				if (handStack.getItem() == Item.getItemFromBlock(Blocks.DIRT)){
					int thisRadius = state.getValue(RADIUS);
					worldIn.setBlockState(pos, ModContent.undergroundHearthgroveRoot.getDefaultState().withProperty(BlockDynamicHearthgroveRoots.RADIUS, thisRadius ));
					if (!playerIn.isCreative()) handStack.shrink(1);
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
					playerIn.swingArm(EnumHand.MAIN_HAND);
					return true;
				} else if (handStack.getItem() == Item.getItemFromBlock(Blocks.GRASS)){
					int thisRadius = state.getValue(RADIUS);
					worldIn.setBlockState(pos, ModContent.undergroundHearthgroveRoot.getDefaultState().withProperty(BlockDynamicHearthgroveRoots.RADIUS, thisRadius ).withProperty(BlockDynamicHearthgroveRoots.GRASSY, true));
					if (!playerIn.isCreative()) handStack.shrink(1);
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
					playerIn.swingArm(EnumHand.MAIN_HAND);
					return true;
				} else if (handStack.getItem() == Item.getItemFromBlock(BlockRegistry.SWAMP_DIRT)){
					int thisRadius = state.getValue(RADIUS);
					worldIn.setBlockState(pos, ModContent.undergroundHearthgroveRootSwamp.getDefaultState().withProperty(BlockDynamicHearthgroveRoots.RADIUS, thisRadius ).withProperty(BlockDynamicHearthgroveRoots.GRASSY, false));
					if (!playerIn.isCreative()) handStack.shrink(1);
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
					playerIn.swingArm(EnumHand.MAIN_HAND);
					return true;
				} else if (handStack.getItem() == Item.getItemFromBlock(BlockRegistry.SWAMP_GRASS)){
					int thisRadius = state.getValue(RADIUS);
					worldIn.setBlockState(pos, ModContent.undergroundHearthgroveRootSwamp.getDefaultState().withProperty(BlockDynamicHearthgroveRoots.RADIUS, thisRadius ).withProperty(BlockDynamicHearthgroveRoots.GRASSY, true));
					if (!playerIn.isCreative()) handStack.shrink(1);
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
					playerIn.swingArm(EnumHand.MAIN_HAND);
					return true;
				}
				return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
			}
		};

		undergroundHearthgroveRoot = new BlockDynamicHearthgroveRoots();
		registry.register(undergroundHearthgroveRoot.setRegistryName(DynamicTreesTBL.MODID, "underground_roots_hearthgrove"));
		undergroundHearthgroveRootSwamp = new BlockDynamicHearthgroveRoots(){
			@Override
			protected ItemStack getSilkTouchDrop(IBlockState state) {
				if (state.getValue(GRASSY)){
					return new ItemStack(Item.getItemFromBlock(BlockRegistry.SWAMP_GRASS));
				} else {
					return new ItemStack(Item.getItemFromBlock(BlockRegistry.SWAMP_DIRT));
				}
			}

			@Override
			public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
			{
				NonNullList<ItemStack> ret = NonNullList.create();
				ret.add(new ItemStack(Item.getItemFromBlock(BlockRegistry.SWAMP_DIRT)));
				return ret;
			}

			@Override
			public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
				if (state.getValue(GRASSY)){
					return new ItemStack(Item.getItemFromBlock(BlockRegistry.SWAMP_GRASS));
				} else {
					return new ItemStack(Item.getItemFromBlock(BlockRegistry.SWAMP_DIRT));
				}
			}
		};
		registry.register(undergroundHearthgroveRootSwamp.setRegistryName(DynamicTreesTBL.MODID, "underground_roots_hearthgrove_2"));

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
		sapLeavesProperties = setUpLeaves(TreeSap.leavesBlock, 0, "deciduous");
		hearthgroveLeavesProperties = setUpLeaves(TreeHearthgrove.leavesBlock, 0, "acacia");
		nibbletwigLeavesProperties = setUpLeaves(TreeNibbletwig.leavesBlock, 0, "deciduous");

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTBL.MODID, 0, rubberLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTBL.MODID, 1, sapLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTBL.MODID, 4, hearthgroveLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesTBL.MODID, 3, nibbletwigLeavesProperties);

		TreeFamily rubberTree = new TreeRubber();
		TreeFamily sapTree = new TreeSap();
		TreeFamily hearthgroveTree = new TreeHearthgrove();
		TreeFamily nibbletwigTree = new TreeNibbletwig();

		Collections.addAll(trees, rubberTree, sapTree, hearthgroveTree, nibbletwigTree);

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
		setUpSeedRecipes("sap", new ItemStack(TreeSap.saplingBlock, 1, 0));
		setUpSeedRecipes("hearthgrove", new ItemStack(TreeHearthgrove.saplingBlock, 1, 0));
		setUpSeedRecipes("nibbletwig", new ItemStack(TreeNibbletwig.saplingBlock, 1, 0));

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

		ModelLoader.setCustomStateMapper(undergroundHearthgroveRoot, new StateMap.Builder().ignore(BlockDynamicHearthgroveRoots.RADIUS).build());
		ModelLoader.setCustomStateMapper(undergroundHearthgroveRootSwamp, new StateMap.Builder().ignore(BlockDynamicHearthgroveRoots.RADIUS).build());
	}
}
