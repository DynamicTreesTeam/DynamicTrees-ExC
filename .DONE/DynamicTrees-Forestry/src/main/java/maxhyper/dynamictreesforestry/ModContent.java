package maxhyper.dynamictreesforestry;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.DendroPotion;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesFruit;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesforestry.blocks.BlockFruitDate;
import maxhyper.dynamictreesforestry.crafting.RecipeFruitToSapling;
import maxhyper.dynamictreesforestry.crafting.RecipeSaplingToSeed;
import maxhyper.dynamictreesforestry.crafting.RecipeSeedToSapling;
import maxhyper.dynamictreesforestry.items.ItemDynamicSeedMaple;
import maxhyper.dynamictreesforestry.trees.*;
import maxhyper.dynamictreesforestry.trees.vanilla.*;
import maxhyper.dynamictreesforestry.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
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
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;

@Mod.EventBusSubscriber(modid = DynamicTreesForestry.MODID)
@ObjectHolder(DynamicTreesForestry.MODID)
public class ModContent {

	public static ILeavesProperties acaciaLeavesProperties, birchLeavesProperties, darkOakLeavesProperties,
			jungleLeavesProperties, oakLeavesProperties, spruceLeavesProperties,
			silverLimeLeavesProperties, walnutLeavesProperties, chestnutLeavesProperties, cherryLeavesProperties,
			lemonLeavesProperties, plumLeavesProperties, mapleLeavesProperties, larchLeavesProperties,
			bullPineLeavesProperties, coastSequoiaLeavesProperties, giantSequoiaLeavesProperties,
			teakLeavesProperties, ipeLeavesProperties, kapokLeavesProperties, ebonyLeavesProperties,
			zebrawoodLeavesProperties, merantiLeavesProperties, desertAcaciaLeavesProperties,
			padaukLeavesProperties, balsaLeavesProperties, cocoboloLeavesProperties, wengeLeavesProperties,
			baobabLeavesProperties, mahoeLeavesProperties, willowLeavesProperties, sipiriLeavesProperties,
			papayaLeavesProperties, palmLeavesProperties, poplarLeavesProperties;

	public static ILeavesProperties[] fruitAppleLeavesProperties, fruitWalnutLeavesProperties, fruitChestnutLeavesProperties,
			fruitCherryLeavesProperties, fruitLemonLeavesProperties, fruitPlumLeavesProperties;

	public static BlockDynamicLeavesFruit appleLeaves, walnutLeaves, chestnutLeaves, cherryLeaves, lemonLeaves, plumLeaves;
	public static BlockDynamicLeavesPalm palmFrondLeaves, papayaFrondLeaves;

	public static BlockFruitDate dateFruit, papayaFruit;
	public static BlockFruit chestnutFruit, walnutFruit, cherryFruit, lemonFruit, plumFruit;

	// trees added by this mod
	public static ArrayList<TreeFamily> trees = new ArrayList<>();

	// extra species added by this mod
	public static ArrayList<Species> vanillaSpecies = new ArrayList<>();

	@SubscribeEvent
	public static void registerDataBasePopulators(final WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent event) {
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

		chestnutFruit = new BlockFruit("fruitchestnut"){
			@Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
			{
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
		registry.register(chestnutFruit);
		walnutFruit = new BlockFruit("fruitwalnut"){
			@Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
			{
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
		registry.register(walnutFruit);
		cherryFruit = new BlockFruit("fruitcherry"){
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
		registry.register(cherryFruit);
		lemonFruit = new BlockFruit("fruitlemon"){
			@Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
			{
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
			public final AxisAlignedBB[] FRUIT_AABB = new AxisAlignedBB[] {
					createBox(1,1,0, 16),
					createBox(1,2,0, 16),
					createBox(2f,5,0, 20),
					createBox(2f,5,1.25f, 20)
			};
			@Override
			public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
				return FRUIT_AABB[state.getValue(AGE)];
			}
		};
		registry.register(lemonFruit);
		plumFruit = new BlockFruit("fruitplum"){
			@Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer()
			{
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
		registry.register(plumFruit);

		dateFruit = new BlockFruitDate("fruitdate", 5);
		registry.register(dateFruit);
		papayaFruit = new BlockFruitDate("fruitpapaya", 6);
		registry.register(papayaFruit);
		palmFrondLeaves = new BlockDynamicLeavesPalm("leaves_palm");
		registry.register(palmFrondLeaves);
		papayaFrondLeaves = new BlockDynamicLeavesPalm("leaves_papaya");
		registry.register(papayaFrondLeaves);

		appleLeaves = new BlockDynamicLeavesFruit("leaves_apple", BlockDynamicLeavesFruit.fruitTypes.APPLE);
		registry.register(appleLeaves);
		walnutLeaves = new BlockDynamicLeavesFruit("leaves_walnut", BlockDynamicLeavesFruit.fruitTypes.WALNUT);
		registry.register(walnutLeaves);
		chestnutLeaves = new BlockDynamicLeavesFruit("leaves_chestnut", BlockDynamicLeavesFruit.fruitTypes.CHESTNUT);
		registry.register(chestnutLeaves);
		cherryLeaves = new BlockDynamicLeavesFruit("leaves_cherry", BlockDynamicLeavesFruit.fruitTypes.CHERRY);
		registry.register(cherryLeaves);
		lemonLeaves = new BlockDynamicLeavesFruit("leaves_lemon", BlockDynamicLeavesFruit.fruitTypes.LEMON);
		registry.register(lemonLeaves);
		plumLeaves = new BlockDynamicLeavesFruit("leaves_plum", BlockDynamicLeavesFruit.fruitTypes.PLUM);
		registry.register(plumLeaves);

        oakLeavesProperties = setUpLeaves(SpeciesOak.leavesBlock, SpeciesOak.leavesMeta, "deciduous");
			fruitAppleLeavesProperties = new ILeavesProperties[] {
					setUpLeaves(SpeciesOak.leavesBlock, SpeciesOak.leavesMeta, "deciduous"),
					setUpLeaves(SpeciesOak.leavesBlock, SpeciesOak.leavesMeta, "deciduous"),
					setUpLeaves(SpeciesOak.leavesBlock, SpeciesOak.leavesMeta, "deciduous"),
					setUpLeaves(SpeciesOak.leavesBlock, SpeciesOak.leavesMeta, "deciduous")
			};
        spruceLeavesProperties = setUpLeaves(SpeciesSpruce.leavesBlock, SpeciesSpruce.leavesMeta, "conifer");
		birchLeavesProperties = setUpLeaves(SpeciesBirch.leavesBlock, SpeciesBirch.leavesMeta, "deciduous");
        jungleLeavesProperties = setUpLeaves(SpeciesJungle.leavesBlock, SpeciesJungle.leavesMeta, "acacia");
        darkOakLeavesProperties = setUpLeaves(SpeciesDarkOak.leavesBlock, SpeciesDarkOak.leavesMeta, "darkOak");
        acaciaLeavesProperties = setUpLeaves(SpeciesAcacia.leavesBlock, SpeciesAcacia.leavesMeta, "deciduous");

		silverLimeLeavesProperties = setUpLeaves(TreeSilverLime.leavesBlock, TreeSilverLime.leavesMeta, "deciduous");
		walnutLeavesProperties = setUpLeaves(TreeWalnut.leavesBlock, TreeWalnut.leavesMeta, "deciduous");
			fruitWalnutLeavesProperties = new ILeavesProperties[]{
					setUpLeaves(TreeWalnut.leavesBlock, TreeWalnut.leavesMeta, "deciduous"),
					setUpLeaves(TreeWalnut.leavesBlock, TreeWalnut.leavesMeta, "deciduous"),
					setUpLeaves(TreeWalnut.leavesBlock, TreeWalnut.leavesMeta, "deciduous"),
					setUpLeaves(TreeWalnut.leavesBlock, TreeWalnut.leavesMeta, "deciduous")
			};
		chestnutLeavesProperties = setUpLeaves(TreeChestnut.leavesBlock, TreeChestnut.leavesMeta, "deciduous");
			fruitChestnutLeavesProperties = new ILeavesProperties[]{
					setUpLeaves(TreeChestnut.leavesBlock, TreeChestnut.leavesMeta, "deciduous"),
					setUpLeaves(TreeChestnut.leavesBlock, TreeChestnut.leavesMeta, "deciduous"),
					setUpLeaves(TreeChestnut.leavesBlock, TreeChestnut.leavesMeta, "deciduous"),
					setUpLeaves(TreeChestnut.leavesBlock, TreeChestnut.leavesMeta, "deciduous")
			};
		cherryLeavesProperties = setUpLeaves(TreeCherry.leavesBlock, TreeCherry.leavesMeta, "acacia");
			fruitCherryLeavesProperties = new ILeavesProperties[]{
					setUpLeaves(TreeCherry.leavesBlock, TreeCherry.leavesMeta, "acacia"),
					setUpLeaves(TreeCherry.leavesBlock, TreeCherry.leavesMeta, "acacia"),
					setUpLeaves(TreeCherry.leavesBlock, TreeCherry.leavesMeta, "acacia"),
					setUpLeaves(TreeCherry.leavesBlock, TreeCherry.leavesMeta, "acacia")
			};
		lemonLeavesProperties = setUpLeaves(TreeLemon.leavesBlock, TreeLemon.leavesMeta, "deciduous");
			fruitLemonLeavesProperties = new ILeavesProperties[]{
					setUpLeaves(TreeLemon.leavesBlock, TreeLemon.leavesMeta, "deciduous"),
					setUpLeaves(TreeLemon.leavesBlock, TreeLemon.leavesMeta, "deciduous"),
					setUpLeaves(TreeLemon.leavesBlock, TreeLemon.leavesMeta, "deciduous"),
					setUpLeaves(TreeLemon.leavesBlock, TreeLemon.leavesMeta, "deciduous")
			};
		plumLeavesProperties = setUpLeaves(TreePlum.leavesBlock, TreePlum.leavesMeta, "deciduous");
			fruitPlumLeavesProperties = new ILeavesProperties[]{
					setUpLeaves(TreePlum.leavesBlock, TreePlum.leavesMeta, "deciduous"),
					setUpLeaves(TreePlum.leavesBlock, TreePlum.leavesMeta, "deciduous"),
					setUpLeaves(TreePlum.leavesBlock, TreePlum.leavesMeta, "deciduous"),
					setUpLeaves(TreePlum.leavesBlock, TreePlum.leavesMeta, "deciduous")
			};
		mapleLeavesProperties = setUpLeaves(TreeMaple.leavesBlock, TreeMaple.leavesMeta, "deciduous", 6, 13);
		larchLeavesProperties = setUpLeaves(TreeLarch.leavesBlock, TreeLarch.leavesMeta, "conifer", 6, 10);
		bullPineLeavesProperties = setUpLeaves(TreeBullPine.leavesBlock, TreeBullPine.leavesMeta, "conifer");
		coastSequoiaLeavesProperties = setUpLeaves(TreeCoastSequoia.leavesBlock, TreeCoastSequoia.leavesMeta, "acacia", 16, 6);
		//giantSequoiaLeavesProperties = setUpLeaves(TreeGiantSequoia.leavesBlock, TreeGiantSequoia.leavesMeta, "acacia", 16, 6);
		teakLeavesProperties = setUpLeaves(TreeTeak.leavesBlock, TreeTeak.leavesMeta, "deciduous");
		ipeLeavesProperties = setUpLeaves(TreeIpe.leavesBlock, TreeIpe.leavesMeta, "acacia", 8, 8);
		kapokLeavesProperties = setUpLeaves(TreeKapok.leavesBlock, TreeKapok.leavesMeta, "acacia", 4, 8);
		ebonyLeavesProperties = setUpLeaves(TreeEbony.leavesBlock, TreeEbony.leavesMeta, "deciduous");
		zebrawoodLeavesProperties = setUpLeaves(TreeZebrawood.leavesBlock, TreeZebrawood.leavesMeta, "deciduous");
		merantiLeavesProperties = setUpLeaves(TreeMeranti.leavesBlock, TreeMeranti.leavesMeta, "deciduous");
		desertAcaciaLeavesProperties = setUpLeaves(TreeDesertAcacia.leavesBlock, TreeDesertAcacia.leavesMeta, "acacia");
		padaukLeavesProperties = setUpLeaves(TreePadauk.leavesBlock, TreePadauk.leavesMeta, "deciduous");
		balsaLeavesProperties = setUpLeaves(TreeBalsa.leavesBlock, TreeBalsa.leavesMeta, "dynamictreesforestry:poplar", 6, 10);
		cocoboloLeavesProperties=setUpLeaves(TreeCocobolo.leavesBlock, TreeCocobolo.leavesMeta, "acacia", 8, 8);
		wengeLeavesProperties = setUpLeaves(TreeWenge.leavesBlock, TreeWenge.leavesMeta, "acacia", 8, 8);
		baobabLeavesProperties = setUpLeaves(TreeBaobab.leavesBlock, TreeBaobab.leavesMeta, "acacia");
		mahoeLeavesProperties = setUpLeaves(TreeMahoe.leavesBlock, TreeMahoe.leavesMeta, "deciduous");
		willowLeavesProperties = setUpLeaves(TreeWillow.leavesBlock, TreeWillow.leavesMeta, "deciduous");
		sipiriLeavesProperties = setUpLeaves( TreeSipiri.leavesBlock, TreeSipiri.leavesMeta, "deciduous", 4, 14);
		papayaLeavesProperties = new LeavesProperties(
				TreePapaya.leavesBlock.getStateFromMeta(TreePapaya.leavesMeta),
				new ItemStack(TreePapaya.leavesBlock,1, TreePapaya.leavesMeta),
				TreeRegistry.findCellKit("palm") ) {
			@Override
			public boolean appearanceChangesWithHydro() {
				return true;
			}
		};
		palmLeavesProperties = new LeavesProperties(
				TreePalm.leavesBlock.getStateFromMeta(TreePalm.leavesMeta),
				new ItemStack(TreePalm.leavesBlock, 1, TreePalm.leavesMeta),
				TreeRegistry.findCellKit("palm") ) {
			@Override
			public boolean appearanceChangesWithHydro() {
				return true;
			}
		};
		poplarLeavesProperties = setUpLeaves(TreePoplar.leavesBlock, TreePoplar.leavesMeta, "dynamictreesforestry:poplar", 12, 10);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 0, oakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 1, spruceLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 2, birchLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 3, jungleLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 4, darkOakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 5, acaciaLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 6, silverLimeLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 7, walnutLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 8, chestnutLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 9, cherryLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 10, lemonLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 11, plumLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 12, mapleLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 13, larchLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 14, bullPineLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 15, coastSequoiaLeavesProperties);

		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 16, giantSequoiaLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 17, teakLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 18, ipeLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 19, kapokLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 20, ebonyLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 21, zebrawoodLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 22, merantiLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 23, desertAcaciaLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 24, padaukLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 25, balsaLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 26, cocoboloLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 27, wengeLeavesProperties);

		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 28, baobabLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 29, mahoeLeavesProperties);
//		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 30, willowLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 31, sipiriLeavesProperties);

		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 32, papayaLeavesProperties);
		//LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 33, palmLeavesProperties);
		LeavesPaging.getLeavesBlockForSequence(DynamicTreesForestry.MODID, 34, poplarLeavesProperties);
		//

		palmLeavesProperties.setDynamicLeavesState(palmFrondLeaves.getDefaultState());
		palmFrondLeaves.setProperties(0, palmLeavesProperties);
		papayaLeavesProperties.setDynamicLeavesState(papayaFrondLeaves.getDefaultState());
		papayaFrondLeaves.setProperties(0, papayaLeavesProperties);

		for (int i=0;i<4;i++) {
			fruitAppleLeavesProperties[i].setDynamicLeavesState(appleLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
			appleLeaves.setProperties(i, fruitAppleLeavesProperties[i]);
			fruitWalnutLeavesProperties[i].setDynamicLeavesState(walnutLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
			walnutLeaves.setProperties(i, fruitWalnutLeavesProperties[i]);
			fruitChestnutLeavesProperties[i].setDynamicLeavesState(chestnutLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
			chestnutLeaves.setProperties(i, fruitChestnutLeavesProperties[i]);
			fruitCherryLeavesProperties[i].setDynamicLeavesState(cherryLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
			cherryLeaves.setProperties(i, fruitCherryLeavesProperties[i]);
			fruitLemonLeavesProperties[i].setDynamicLeavesState(lemonLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
			lemonLeaves.setProperties(i, fruitLemonLeavesProperties[i]);
			fruitPlumLeavesProperties[i].setDynamicLeavesState(plumLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
			plumLeaves.setProperties(i, fruitPlumLeavesProperties[i]);
		}

		TreeFamily vanillaOak = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "oak")).getFamily();
		TreeFamily vanillaBirch = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "birch")).getFamily();
		TreeFamily vanillaSpruce = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "spruce")).getFamily();
		TreeFamily vanillaJungle = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "jungle")).getFamily();
		TreeFamily vanillaDarkOak = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "darkoak")).getFamily();
		TreeFamily vanillaAcacia = TreeRegistry.findSpecies(new ResourceLocation(com.ferreusveritas.dynamictrees.ModConstants.MODID, "acacia")).getFamily();
		Species oak = new SpeciesOak(vanillaOak);
		vanillaSpecies.add(oak);
        Species birch = new SpeciesBirch(vanillaBirch);
		vanillaSpecies.add(birch);
		Species spruce = new SpeciesSpruce(vanillaSpruce);
		vanillaSpecies.add(spruce);
		Species jungle = new SpeciesJungle(vanillaJungle);
		vanillaSpecies.add(jungle);
		Species darkOak = new SpeciesDarkOak(vanillaDarkOak);
		vanillaSpecies.add(darkOak);
		Species acacia = new SpeciesAcacia(vanillaAcacia);
		vanillaSpecies.add(acacia);

		TreeFamily silverLime = new TreeSilverLime();
		TreeFamily walnut = new TreeWalnut();
		TreeFamily chestnut = new TreeChestnut();
		TreeFamily cherry = new TreeCherry();
		TreeFamily lemon = new TreeLemon();
		TreeFamily plum = new TreePlum();
		TreeFamily maple = new TreeMaple();
		TreeFamily larch = new TreeLarch();
		TreeFamily bullPine = new TreeBullPine();
		TreeFamily coastSequoia = new TreeCoastSequoia();
		//TreeFamily giantSequoia = new TreeGiantSequoia();
		TreeFamily teak = new TreeTeak();
		TreeFamily ipe = new TreeIpe();
		TreeFamily kapok = new TreeKapok();
		TreeFamily ebony = new TreeEbony();
		TreeFamily zebrawood = new TreeZebrawood();
		TreeFamily meranti = new TreeMeranti();
		TreeFamily desertAcacia = new TreeDesertAcacia();
		TreeFamily paduk = new TreePadauk();
		TreeFamily balsa = new TreeBalsa();
		TreeFamily cocobolo = new TreeCocobolo();
		TreeFamily wenge = new TreeWenge();
		TreeFamily baobab = new TreeBaobab();
		TreeFamily mahoe = new TreeMahoe();
		TreeFamily willow = new TreeWillow();
		TreeFamily sipiri = new TreeSipiri();
		TreeFamily papaya = new TreePapaya();
		TreeFamily palm = new TreePalm();
		TreeFamily poplar = new TreePoplar();

		Collections.addAll(trees,
				silverLime, walnut, chestnut, cherry, lemon, plum, maple, larch, bullPine, coastSequoia
				//, giantSequoia
				, teak, ipe, kapok, ebony, zebrawood, meranti, desertAcacia, paduk, balsa, cocobolo, wenge
				, baobab, mahoe, willow, sipiri, papaya, palm, poplar
		);

		Species.REGISTRY.registerAll(vanillaSpecies.toArray(new Species[0]));
		trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
		ArrayList<Block> treeBlocks = new ArrayList<>();
		trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
		treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(DynamicTreesForestry.MODID).values());
		registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
	}

	private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getStateFromMeta(leavesMeta),
				new ItemStack(leavesBlock, 1, leavesMeta),
				TreeRegistry.findCellKit(cellKit))
		{
			@Override public ItemStack getPrimitiveLeavesItemStack() {
				return new ItemStack(leavesBlock, 1, leavesMeta);
			}
		};
		return leavesProperties;
	}
	private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesMeta, String cellKit, int smother, int light){
		ILeavesProperties leavesProperties;
		leavesProperties = new LeavesProperties(
				leavesBlock.getStateFromMeta(leavesMeta),
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
		vanillaSpecies.forEach(species -> treeItems.add(species.getSeed()));
		trees.forEach(tree -> tree.getRegisterableItems(treeItems));
		registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		EntityRegistry.registerModEntity(new ResourceLocation(DynamicTreesForestry.MODID, "mapleseed"), ItemDynamicSeedMaple.EntityItemMapleSeed.class, "maple_seed", id++, DynamicTreesForestry.MODID, 32, 1, true);
	}


	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		setUpSeedRecipes(ModConstants.ACACIA);
		setUpSeedRecipes(ModConstants.BALSA);
		setUpSeedRecipes(ModConstants.BAOBAB);
		setUpSeedRecipes(ModConstants.BIRCH);
		setUpSeedRecipes(ModConstants.BULLPINE);
		setUpSeedRecipes(ModConstants.CHERRY);
		setUpSeedRecipes(ModConstants.CHESTNUT);
		setUpSeedRecipes(ModConstants.COASTSEQUOIA);
		setUpSeedRecipes(ModConstants.COCOBOLO);
		setUpSeedRecipes(ModConstants.DARKOAK);
		setUpSeedRecipes(ModConstants.DATEPALM);
		setUpSeedRecipes(ModConstants.DESERTACACIA);
		setUpSeedRecipes(ModConstants.EBONY);
		setUpSeedRecipes(ModConstants.GREENHEART);
		setUpSeedRecipes(ModConstants.IPE);
		setUpSeedRecipes(ModConstants.JUNGLE);
		setUpSeedRecipes(ModConstants.KAPOK);
		setUpSeedRecipes(ModConstants.LARCH);
		setUpSeedRecipes(ModConstants.MAHOE);
		setUpSeedRecipes(ModConstants.MAHOGANY);
		setUpSeedRecipes(ModConstants.MAPLE);
		setUpSeedRecipes(ModConstants.OAK);
		setUpSeedRecipes(ModConstants.PADAUK);
		setUpSeedRecipes(ModConstants.PAPAYA);
		setUpSeedRecipes(ModConstants.PLUM);
		setUpSeedRecipes(ModConstants.POPLAR);
		setUpSeedRecipes(ModConstants.SILVERLIME);
		setUpSeedRecipes(ModConstants.SPRUCE);
		setUpSeedRecipes(ModConstants.TEAK);
		setUpSeedRecipes(ModConstants.WALNUT);
		setUpSeedRecipes(ModConstants.WENGE);
		setUpSeedRecipes(ModConstants.WILLOW);
		setUpSeedRecipes(ModConstants.ZEBRAWOOD);

		IRecipe seedRecipe = new RecipeSaplingToSeed();
		seedRecipe.setRegistryName(new ResourceLocation(DynamicTreesForestry.MODID, "saplingToSeed"));
		event.getRegistry().register(seedRecipe);

		IRecipe saplingRecipe = new RecipeSeedToSapling();
		saplingRecipe.setRegistryName(new ResourceLocation(DynamicTreesForestry.MODID, "seedToSapling"));
		event.getRegistry().register(saplingRecipe);

		IRecipe fruitRecipe = new RecipeFruitToSapling();
		fruitRecipe.setRegistryName(new ResourceLocation(DynamicTreesForestry.MODID, "fruitToSapling"));
		event.getRegistry().register(fruitRecipe);

		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedApple"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "oak")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Items.APPLE,1,0))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedCherry"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "cherry")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,0))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedWalnut"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "walnut")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,1)),
				Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedChestnut"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "chestnut")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,2)),
				Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 15))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedLemon"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "lemon")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,3))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedPlum"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "plum")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,4))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedDate"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "palm")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,5))
		);
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(DynamicTreesForestry.MODID, "deseedPapaya"),
				null,
				TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "papaya")).getSeedStack(1),
				Ingredient.fromStacks(new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))),1,6))
		);
	}
	private static void setUpSeedRecipes(String name){
		Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, name));
		ItemStack treeSeed = treeSpecies.getSeedStack(1);
		ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotion.DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
		BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotion.DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);
		//createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);
	}

	private static Map<String,String> treeUIDs = new HashMap<String,String>(){{
		put("forestry.treeOak", ModConstants.OAK);
		put("forestry.treeDarkOak",ModConstants.DARKOAK);
		put("forestry.treeBirch",ModConstants.BIRCH);
		put("forestry.treeLime",ModConstants.SILVERLIME);
		put("forestry.treeWalnut",ModConstants.WALNUT);
		put("forestry.treeChestnut",ModConstants.CHESTNUT);
		put("forestry.treeCherry",ModConstants.CHERRY);
		put("forestry.treeLemon",ModConstants.LEMON);
		put("forestry.treePlum",ModConstants.PLUM);
		put("forestry.treeMaple",ModConstants.MAPLE);
		put("forestry.treeSpruce",ModConstants.SPRUCE);
		put("forestry.treeLarch",ModConstants.LARCH);
		put("forestry.treePine",ModConstants.BULLPINE);
		put("forestry.treeSequoia",ModConstants.COASTSEQUOIA);
		put("forestry.treeGigant",ModConstants.GIANTSEQUOIA);
		put("forestry.treeJungle",ModConstants.JUNGLE);
		put("forestry.treeTeak",ModConstants.TEAK);
		put("forestry.treeIpe",ModConstants.IPE);
		put("forestry.treeKapok",ModConstants.KAPOK);
		put("forestry.treeEbony",ModConstants.EBONY);
		put("forestry.treeZebrawood",ModConstants.ZEBRAWOOD);
		put("forestry.treeMahogony",ModConstants.MAHOGANY);
		put("forestry.treeAcaciaVanilla",ModConstants.ACACIA);
		put("forestry.treeAcacia",ModConstants.DESERTACACIA);
		put("forestry.treePadauk",ModConstants.PADAUK);
		put("forestry.treeBalsa",ModConstants.BALSA);
		put("forestry.treeCocobolo",ModConstants.COCOBOLO);
		put("forestry.treeWenge",ModConstants.WENGE);
		put("forestry.treeBaobab",ModConstants.BAOBAB);
		put("forestry.treeMahoe",ModConstants.MAHOE);
		put("forestry.treeWillow",ModConstants.WILLOW);
		put("forestry.treeSipiri",ModConstants.GREENHEART);
		put("forestry.treePapaya",ModConstants.PAPAYA);
		put("forestry.treeDate",ModConstants.DATEPALM);
		put("forestry.treePoplar",ModConstants.POPLAR);
	}};
	private static BiMap<String,String> treeUIDsBidir = HashBiMap.create(treeUIDs);

	public static String getTreeUIDfromID(String ID){
		return treeUIDsBidir.inverse().get(ID);
	}

	public static String getTreeIDfromUID(String UID){
		return treeUIDsBidir.get(UID);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (TreeFamily tree : trees) {
			ModelHelper.regModel(tree.getDynamicBranch());
			ModelHelper.regModel(tree.getCommonSpecies().getSeed());
			ModelHelper.regModel(tree);
		}
		LeavesPaging.getLeavesMapForModId(DynamicTreesForestry.MODID).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));

		vanillaSpecies.forEach(species -> ModelHelper.regModel(species.getSeed()));

		ModelLoader.setCustomStateMapper(TreeWillow.willowLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(ModContent.palmLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(ModContent.papayaLeavesProperties.getDynamicLeavesState().getBlock(), new StateMap.Builder().ignore(BlockDynamicLeaves.TREE).build());

		ModelLoader.setCustomStateMapper(appleLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(walnutLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(chestnutLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(cherryLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(lemonLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
		ModelLoader.setCustomStateMapper(plumLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).ignore(BlockDynamicLeaves.HYDRO).ignore(BlockDynamicLeaves.TREE).build());
	}
}
