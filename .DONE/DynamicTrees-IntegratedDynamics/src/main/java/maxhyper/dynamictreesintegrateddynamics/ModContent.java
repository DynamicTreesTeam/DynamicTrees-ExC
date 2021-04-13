package maxhyper.dynamictreesintegrateddynamics;

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
import maxhyper.dynamictreesintegrateddynamics.blocks.BlockDynamicBranchMenril;
import maxhyper.dynamictreesintegrateddynamics.blocks.BlockDynamicLeavesMenril;
import maxhyper.dynamictreesintegrateddynamics.trees.TreeMenril;
import maxhyper.dynamictreesintegrateddynamics.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
import java.util.Objects;

@Mod.EventBusSubscriber(modid = DynamicTreesIntegratedDynamics.MODID)
@ObjectHolder(DynamicTreesIntegratedDynamics.MODID)
public class ModContent {

	public static BlockDynamicLeaves menrilLeaves;
	public static BlockBranch menrilBranch, menrilBranchFilled;
	public static BlockFruit blockMenrilBerries;
	public static BlockSurfaceRoot menrilRoot;
	public static ILeavesProperties menrilLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	private static AxisAlignedBB createBox (float radius, float height, float stemLength, float fraction){
		float topHeight = fraction - stemLength;
		float bottomHeight = topHeight - height;
		return new AxisAlignedBB(
				((fraction/2) - radius)/fraction, topHeight/fraction, ((fraction/2) - radius)/fraction,
				((fraction/2) + radius)/fraction, bottomHeight/fraction, ((fraction/2) + radius)/fraction);
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		//fruit
		blockMenrilBerries = new BlockFruit("fruitmenrilberries"){
			@Override
			public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
				AxisAlignedBB[] MENRIL_BERRY_BOX = new AxisAlignedBB[] {
						createBox(1,1,0, 16),
						createBox(2,3,0, 20),
						createBox(3.3f,4,1, 20),
						createBox(3.3f,4,2, 20)
				};
				return MENRIL_BERRY_BOX[state.getValue(AGE)];
			}

			@Override
			public BlockRenderLayer getBlockLayer() {
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
		registry.register(blockMenrilBerries);

		//Branches
		menrilBranch = new BlockDynamicBranchMenril(false, "branch");
		registry.register(menrilBranch);
		menrilBranchFilled = new BlockDynamicBranchMenril(true, "branchfilled");
		registry.register(menrilBranchFilled);

		//Leaves
		menrilLeaves = new BlockDynamicLeavesMenril();
		registry.register(menrilLeaves);

		menrilLeavesProperties = new LeavesProperties(
				TreeMenril.leavesBlock.getDefaultState(),
				new ItemStack(TreeMenril.leavesBlock),
				TreeRegistry.findCellKit("deciduous"))
		{
			@Override public int getSmotherLeavesMax() {
				return 8;
			}
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(TreeMenril.leavesBlock);
			}
			@Override public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
				return 0xffffff;
			}
		};

		menrilLeavesProperties.setDynamicLeavesState(menrilLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		menrilLeaves.setProperties(0, menrilLeavesProperties);

		TreeFamily menrilTree = new TreeMenril();
		Collections.addAll(trees, menrilTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesIntegratedDynamics.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new ItemBlock(menrilBranchFilled).setRegistryName(Objects.requireNonNull(menrilBranchFilled.getRegistryName())));

		ArrayList<Item> treeItems = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		setUpSeedRecipes("menril", new ItemStack(TreeMenril.saplingBlock));

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, name));
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
		LeavesPaging.getLeavesMapForModId(DynamicTreesIntegratedDynamics.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		ModelHelper.regModel(menrilBranchFilled);
		ModelLoader.setCustomStateMapper(menrilLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());

	}
}
