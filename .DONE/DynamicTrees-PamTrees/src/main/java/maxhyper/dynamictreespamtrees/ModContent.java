package maxhyper.dynamictreespamtrees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreespamtrees.trees.SpeciesRedbud;
import maxhyper.dynamictreespamtrees.trees.TreeSpooky;
import maxhyper.dynamictreespamtrees.worldgen.BiomeDataBasePopulator;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static maxhyper.dynamictreespamtrees.DynamicTreesPamTrees.REDBUDTREE_MOD;
import static maxhyper.dynamictreespamtrees.DynamicTreesPamTrees.SPOOKYTREE_MOD;

@Mod.EventBusSubscriber(modid = DynamicTreesPamTrees.MODID)
@ObjectHolder(DynamicTreesPamTrees.MODID)
public class ModContent {

	public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();

	public static LeavesProperties redbudLeavesProperties, spookyLeavesProperties;
	public static BlockDynamicLeaves leavesRedbud, leavesSpooky;
	public static TreeFamily treeSpooky;
	public static Species speciesRedbud;
	public static boolean spookySpawnNaturally, redbudSpawnNaturally;

	static boolean messageSent = false;

	@SubscribeEvent
	public static void onEvent(EntityJoinWorldEvent event)
	{
		if (!messageSent && (event.getEntity() instanceof EntityPlayer))
		{
			if (!Loader.isModLoaded(REDBUDTREE_MOD) && !Loader.isModLoaded(SPOOKYTREE_MOD)){
				event.getEntity().sendMessage(new TextComponentString("Dynamic Trees for Pam Trees is installed but neither Pam's Spooky Tree or Pam's Redbud Tree are installed."));
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

		//Blocks
		ArrayList<Block> treeBlocks = new ArrayList<>();

		if (Loader.isModLoaded(REDBUDTREE_MOD)){
			redbudLeavesProperties = new LeavesProperties(ModBlocks.blockStates.air){
				@Override
				public boolean updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
					if (!worldIn.isRemote){
						Float season = SeasonHelper.getSeasonValue(worldIn, pos);
						if (season == null){
							if (worldIn.getCurrentMoonPhaseFactor() >= 0.5F) { //If theres no seasons we use the moon phases
								worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE, 1));
							} else {
								worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE, 0));
							}
						} else {
							if (SeasonHelper.isSeasonBetween(season, SeasonHelper.SPRING, SeasonHelper.SUMMER)) {
								worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE, 1));
							} else {
								worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE, 0));
							}
						}
					}
					return super.updateTick(worldIn, pos, state, rand);
				}

				@Override
				public IBlockState getPrimitiveLeaves() {
					return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(REDBUDTREE_MOD, "redbudtree_leaves"))).getDefaultState();
				}
				@Override
				public ItemStack getPrimitiveLeavesItemStack() {
					return new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(REDBUDTREE_MOD, "redbudtree_leaves"))));
				}
			};
			leavesRedbud = new BlockDynamicLeaves();
			leavesRedbud.setDefaultNaming(DynamicTreesPamTrees.MODID, "leaves_redbud");
			redbudLeavesProperties.setDynamicLeavesState(leavesRedbud.getDefaultState());
			leavesRedbud.setProperties(0, redbudLeavesProperties);
			leavesRedbud.setProperties(1, redbudLeavesProperties);
		}
		if (Loader.isModLoaded(SPOOKYTREE_MOD)){
			spookyLeavesProperties = new LeavesProperties(Blocks.AIR.getDefaultState(), TreeRegistry.findCellKit("darkoak")){
				@Override
				public IBlockState getPrimitiveLeaves() {
					return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SPOOKYTREE_MOD, "spookytree_leaves"))).getDefaultState();
				}
				@Override
				public ItemStack getPrimitiveLeavesItemStack() {
					return new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SPOOKYTREE_MOD, "spookytree_leaves"))));
				}
			};
			leavesSpooky = new BlockDynamicLeaves();
			leavesSpooky.setDefaultNaming(DynamicTreesPamTrees.MODID, "leaves_spooky");
			spookyLeavesProperties.setDynamicLeavesState(leavesSpooky.getDefaultState());
			leavesSpooky.setProperties(0, spookyLeavesProperties);
		}

		//Trees

		if (Loader.isModLoaded(REDBUDTREE_MOD)){
			TreeFamily oak = TreeRegistry.findSpeciesSloppy("oak").getFamily();
			speciesRedbud = new SpeciesRedbud(oak);
			speciesRedbud.getLeavesProperties().setTree(oak);
			Species.REGISTRY.register(speciesRedbud);

			SpeciesRedbud.clearVanillaTreeSpawning();

			treeBlocks.add(leavesRedbud);
		}

		if (Loader.isModLoaded(SPOOKYTREE_MOD)){
			treeSpooky = new TreeSpooky();
			trees.add(treeSpooky);

			TreeSpooky.clearVanillaTreeSpawning();

			treeBlocks.add(leavesSpooky);
		}
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));

		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesPamTrees.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	@SubscribeEvent public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		ArrayList<Item> treeItems = new ArrayList<>();

		if (Loader.isModLoaded(REDBUDTREE_MOD)){
			treeItems.add(speciesRedbud.getSeed());
		}

		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		if (Loader.isModLoaded(REDBUDTREE_MOD)){
			Block redbudSapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(REDBUDTREE_MOD, "redbudtree_sapling"));
			setUpSeedRecipes("redbud", new ItemStack(redbudSapling));
		}
		if (Loader.isModLoaded(SPOOKYTREE_MOD)){
			Block spookySapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(SPOOKYTREE_MOD, "spookytree_sapling"));
			setUpSeedRecipes("spooky", new ItemStack(spookySapling));
		}
	}
	public static void setUpSeedRecipes (String name, ItemStack treeSapling){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPamTrees.MODID, name));
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
		
		LeavesPaging.getLeavesMapForModId(DynamicTreesPamTrees.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
		if (Loader.isModLoaded(REDBUDTREE_MOD)){
			ModelHelper.regModel(speciesRedbud.getSeed());
			ModelLoader.setCustomStateMapper(leavesRedbud, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).build());
		}
		if (Loader.isModLoaded(SPOOKYTREE_MOD)){
			ModelLoader.setCustomStateMapper(leavesSpooky, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		}
	}
}
