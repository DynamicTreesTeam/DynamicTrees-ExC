package maxhyper.dynamictreesforbiddenarcanus;

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

import maxhyper.dynamictreesforbiddenarcanus.trees.*;
import maxhyper.dynamictreesforbiddenarcanus.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

@Mod.EventBusSubscriber(modid = DynamicTreesForbiddenArcanus.MODID)
@ObjectHolder(DynamicTreesForbiddenArcanus.MODID)
public class ModContent {

	public static BlockFruit blockGoldenApple, blockCherry;
	public static ILeavesProperties cherrywoodLeavesProperties, mysterywoodLeavesProperties;
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

		blockGoldenApple = new BlockFruit("fruitgolden").setDroppedItem(new ItemStack(Items.GOLDEN_APPLE));
		registry.register(blockGoldenApple);
		blockCherry = new BlockFruit("fruitcherry"){
			@Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
			{
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
			protected final AxisAlignedBB[] FRUIT_AABB = new AxisAlignedBB[] {
					createBox(1,1,0, 16),
					createBox(2,3,0, 20),
					createBox(2.5f,4,2, 20),
					createBox(3.5f,4,3, 20)
			};
			@Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
				return FRUIT_AABB[state.getValue(AGE)];
			}
		};
		registry.register(blockCherry);

		cherrywoodLeavesProperties = setUpLeaves(TreeCherrywood.leavesBlock, "acacia");
		mysterywoodLeavesProperties = setUpLeaves(TreeMysterywood.leavesBlock, "deciduous");
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForbiddenArcanus.MODID, 0, cherrywoodLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForbiddenArcanus.MODID, 4, mysterywoodLeavesProperties);

		TreeFamily cherrywoodTree = new TreeCherrywood();
		TreeFamily mysterywoodTree = new TreeMysterywood();
		Collections.addAll(trees, cherrywoodTree, mysterywoodTree);

		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesForbiddenArcanus.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	public static ILeavesProperties setUpLeaves (Block leavesBlock, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getDefaultState(),
				new ItemStack(leavesBlock, 1, 0),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock);
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
			setUpSeedRecipes("cherrywood", new ItemStack(TreeCherrywood.saplingBlock), new ItemStack(com.stal111.forbidden_arcanus.init.ModItems.cherry_peach));
			setUpSeedRecipes("mysterywood", new ItemStack(TreeMysterywood.saplingBlock), null);

	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling, ItemStack treeFruit){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForbiddenArcanus.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		if (treeFruit == null)
			ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
		 else
			ModRecipes.createDirtBucketExchangeRecipesWithFruit(treeSapling, treeSeed, treeFruit,true, false);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (TreeFamily tree : trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		LeavesPaging.getLeavesMapForModId(DynamicTreesForbiddenArcanus.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

	}
}
