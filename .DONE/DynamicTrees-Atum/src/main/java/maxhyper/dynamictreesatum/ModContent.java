package maxhyper.dynamictreesatum;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.teammetallurgy.atum.init.AtumItems;
import maxhyper.dynamictreesatum.blocks.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesatum.trees.A2TreeDeadPalm;
import maxhyper.dynamictreesatum.trees.A2TreeDeadTree;
import maxhyper.dynamictreesatum.trees.A2TreePalm;
import maxhyper.dynamictreesatum.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;

@Mod.EventBusSubscriber(modid = DynamicTreesAtum.MODID)
@ObjectHolder(DynamicTreesAtum.MODID)
public class ModContent {

	public static BlockDynamicLeaves palmFrondLeaves;
	public static ILeavesProperties palmLeavesProperties, deadPalmLeavesProperties, nullLeavesProperties;
	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
		event.register(new BiomeDataBasePopulator());
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		palmFrondLeaves = new BlockDynamicLeavesPalm("leaves_palm");
		registry.register(palmFrondLeaves);

		palmLeavesProperties = new LeavesProperties(
				A2TreePalm.leavesBlock.getDefaultState(),
				new ItemStack(A2TreePalm.leavesBlock),
				TreeRegistry.findCellKit("palm") ) {
		};
		deadPalmLeavesProperties = new LeavesProperties(
				A2TreeDeadPalm.leavesBlock.getDefaultState(),
				new ItemStack(A2TreeDeadPalm.leavesBlock),
				TreeRegistry.findCellKit("palm") ) {
		};
		nullLeavesProperties = new LeavesProperties(null, ItemStack.EMPTY, TreeRegistry.findCellKit("bare") ) {};

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesAtum.MODID, 0, nullLeavesProperties);

		palmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		palmFrondLeaves.setProperties(0, palmLeavesProperties);
		deadPalmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
		palmFrondLeaves.setProperties(1, deadPalmLeavesProperties);

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

		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesAtum.MODID, "palm"));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		GameRegistry.addShapelessRecipe(new ResourceLocation(DynamicTreesAtum.MODID, "dateSeed"), null, treeSeed, Ingredient.fromItem(AtumItems.DATE));


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

		ModelLoader.setCustomStateMapper(palmFrondLeaves, new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(nullLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockLeaves.DECAYABLE).build());
	}

//	/** TODO: THIS IS THE WRONG WAY OF DOING THINGS BUT ITS THE ONLY IDEA THAT WORKED */
//	public static final AtumBiome OASIS = AtumRegistry.registerBiome(new BiomeOasis((new AtumBiome.AtumBiomeProperties("Dynamic Oasis", 0)).setHeightVariation(0.0F)){
//		@Override
//		public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
//			int x = random.nextInt(16) + 8;
//			int z = random.nextInt(16) + 8;
//			BlockPos height = world.getHeight(pos.add(x, 0, z));
//			ChunkPos chunkPos = new ChunkPos(pos);
//
//			new WorldGenOasisPond().generate(world, random, height);
//
////			if (random.nextFloat() <= 0.98F) {
////				new WorldGenPalm(true, random.nextInt(4) + 5, true).generate(world, random, height);
////			}
//
//			if (TerrainGen.decorate(world, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.REED)) {
//				int reedsPerChunk = 50;
//				for (int reeds = 0; reeds < reedsPerChunk; ++reeds) {
//					int y = height.getY() * 2;
//
//					if (y > 0) {
//						int randomY = random.nextInt(y);
//						new WorldGenPapyrus().generate(world, random, pos.add(x, randomY, z));
//					}
//				}
//			}
//			if (TerrainGen.decorate(world, random, chunkPos, DecorateBiomeEvent.Decorate.EventType.LILYPAD)) {
//				for (int amount = 0; amount < 2; ++amount) {
//					int y = world.getHeight(pos.add(x, 0, z)).getY() * 2;
//					if (y > 0) {
//						int randomY = random.nextInt(y);
//						BlockPos lilyPos;
//						BlockPos waterPos;
//						for (lilyPos = pos.add(x, randomY, z); lilyPos.getY() > 0; lilyPos = waterPos) {
//							waterPos = lilyPos.down();
//							if (!world.isAirBlock(waterPos)) {
//								break;
//							}
//						}
//						new WorldGenWaterlily().generate(world, random, lilyPos);
//					}
//				}
//			}
//			super.decorate(world, random, pos);
//		}
//	}, "oasis");
//	public static final AtumBiome DEAD_OASIS = AtumRegistry.registerBiome(new BiomeDeadOasis((new AtumBiome.AtumBiomeProperties("Dynamic Dead Oasis", 0)).setHeightVariation(0.0F)){
//		@Override
//		public void decorate(@Nonnull World world, @Nonnull Random random, @Nonnull BlockPos pos) {
//			int x = random.nextInt(4) + 4;
//			int z = random.nextInt(4) + 4;
//
//			int i1 = random.nextInt(16) + 8;
//			int j1 = random.nextInt(256);
//			int k1 = random.nextInt(16) + 8;
//			(new WorldGenLakes(Blocks.AIR)).generate(world, random, pos.add(i1, j1, k1));
//
////			if (random.nextFloat() <= 0.70F) {
////				new WorldGenPalm(true, 5, AtumBlocks.DEADWOOD_LOG.getDefaultState().withProperty(BlockDeadwood.HAS_SCARAB, true), BlockLeave.getLeave(BlockAtumPlank.WoodType.DEADWOOD).getDefaultState().withProperty(BlockLeave.CHECK_DECAY, false), false).generate(world, random, world.getHeight(pos.add(x, 0, z)));
////			}
//
//			super.decorate(world, random, pos);
//		}
//
//	}, "dead_oasis");
//
//	@SubscribeEvent
//	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
//		event.getRegistry().register(OASIS);
//		event.getRegistry().register(DEAD_OASIS);
//		BiomeDictionary.addTypes(OASIS, AtumBiomes.BiomeTags.OASIS, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.WET, BiomeDictionary.Type.RARE);
//		BiomeDictionary.addTypes(DEAD_OASIS, AtumBiomes.BiomeTags.ATUM, BiomeDictionary.Type.HOT, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.RARE);
//	}
}
