package maxhyper.dynamictreesexc.compat;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockRooty;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.ModContent;
import maxhyper.dynamictreesexc.blocks.*;
import maxhyper.dynamictreesexc.featuregen.SlimeGrowthLogic;
import maxhyper.dynamictreesexc.items.ItemDynamicSeedBurntFeJuniper;
import maxhyper.dynamictreesexc.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;
import java.util.Objects;

import static maxhyper.dynamictreesexc.ModContent.*;

public class CompatEvents {

    //
    // MODEL BAKE
    //
    @Optional.Method(modid = "tconstruct")
    public static void ModelBakeTinkersConstructCompat(ModelBakeEvent event){
        Block[] rootyBlocks = new Block[] {rootySlimyDirt};

        for(Block block: rootyBlocks) {
            IBakedModel rootsObject = event.getModelRegistry().getObject(new ModelResourceLocation(block.getRegistryName(), "normal"));
            if (rootsObject != null) {
                BakedModelBlockRooty rootyModel = new BakedModelBlockRooty((IBakedModel) rootsObject);
                event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "normal"), rootyModel);
            }
        }
    }

    //
    // COLOR HANDLERS
    //
    @Optional.Method(modid = "extrautils2")
    public static void ColorHandlersExtraUtils2(){
        ModelHelper.regColorHandler(fejuniperLeaves, new IBlockColor() {
            @Override
            public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
                //boolean inWorld = worldIn != null && pos != null;

                Block block = state.getBlock();

                if (TreeHelper.isLeaves(block)) {
                    return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
                }
                return 0x00FF00FF; //Magenta
            }
        }); // Ferrous Juniper leaves
    }
    @Optional.Method(modid = "tconstruct")
    public static void ColorHandlersTinkersConstructCompat(){
        final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
                    switch(tintIndex) {
                        case 0: return blockColors.colorMultiplier(rootySlimyDirt.getMimic(world, pos), world, pos, tintIndex);
                        case 1: return state.getBlock() instanceof BlockRooty ? ((BlockRooty) state.getBlock()).rootColor(state, world, pos) : 0xFFFFFFFF;
                        default: return 0xFFFFFFFF;
                    }
                }, // Rooty Dirt
                rootySlimyDirt);
    }

    //
    // REGISTER BLOCKS
    //
    @Optional.Method(modid = "quark") //BLOSSOMING AND SWAMP OAK
    public static void RegisterBlocksQuark(IForgeRegistry<Block> registry){
        //Leaves properties
        swampOakLeavesProperties = setUpLeaves(TreeSwampOak.leavesBlock, 0, "deciduous", 3, 13);
        blossomingLeavesProperties = new LeavesProperties(
                TreeBlossoming.leavesBlock.getStateFromMeta(1),
                new ItemStack(TreeBlossoming.leavesBlock, 1, 1),
                TreeRegistry.findCellKit("deciduous"))
        {
            @Override public ItemStack getPrimitiveLeavesItemStack() {
                return new ItemStack(TreeBlossoming.leavesBlock, 1, 1);
            }
            @Override public int foliageColorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos) {
                return 0xffffff;
            }
        };

        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 4, blossomingLeavesProperties);
        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 5, swampOakLeavesProperties);

        TreeFamily blossomingTree = new TreeBlossoming();
        TreeFamily swampOakTree = new TreeSwampOak();
        Collections.addAll(trees, blossomingTree, swampOakTree);
    }
    @Optional.Method(modid = "integrateddynamics") //MENRIL
    public static void RegisterBlocksIntegratedDynamics(IForgeRegistry<Block> registry){
        //Branches
        menrilBranch = new BlockDynamicBranchMenril();
        registry.register(menrilBranch);
        menrilBranchFilled = new BlockDynamicBranchMenril(true);
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
    }
    @Optional.Method(modid = "extrautils2") //FERROUS JUNIPER
    public static void RegisterBlocksExtraUtils2(IForgeRegistry<Block> registry){

        fejuniperBranchRaw = new BlockDynamicBranchFeJuniper();
        registry.register(fejuniperBranchRaw);
        fejuniperBranchBurnt = new BlockDynamicBranchFeJuniper(true);
        registry.register(fejuniperBranchBurnt);

        fejuniperLeaves = new BlockDynamicLeavesFeJuniper();
        registry.register(fejuniperLeaves);

        fejuniperSaplingBurnt = new BlockDynamicSaplingBurntFeJuniper();
        registry.register(fejuniperSaplingBurnt);

        fejuniperLeavesRawProperties = setUpLeaves(TreeFeJuniper.leavesBlock, 0, "conifer");
        fejuniperLeavesBurntProperties = setUpLeaves(TreeFeJuniper.leavesBlock, 1, "conifer");

        fejuniperLeavesRawProperties.setDynamicLeavesState(fejuniperLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
        fejuniperLeavesBurntProperties.setDynamicLeavesState(fejuniperLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 1));
        fejuniperLeaves.setProperties(0, fejuniperLeavesRawProperties);
        fejuniperLeaves.setProperties(1, fejuniperLeavesBurntProperties);

        TreeFamily feJuniperTree = new TreeFeJuniper();
        Collections.addAll(trees, feJuniperTree);
    }
    @Optional.Method(modid = "tconstruct") //SLIME TREES
    public static void RegisterBlocksTinkersConstruct(IForgeRegistry<Block> registry){
        slimeBlueBranch = new BlockDynamicBranchSlime("slimebluebranch");
        registry.register(slimeBlueBranch);
        slimePurpleBranch = new BlockDynamicBranchSlime("slimepurplebranch");
        registry.register(slimePurpleBranch);
        slimeMagmaBranch = new BlockDynamicBranchSlime("slimemagmabranch");
        registry.register(slimeMagmaBranch);

        rootySlimyDirt = new BlockRootySlimyDirt(false);
        registry.register(rootySlimyDirt);

        blueSlimeLeavesProperties = new LeavesProperties(
                TreeSlimeBlue.leavesBlock.getStateFromMeta(0),
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
                TreeSlimeBlue.leavesBlock.getStateFromMeta(1),
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
                TreeSlimeMagma.leavesBlock.getStateFromMeta(2),
                new ItemStack(TreeSlimeMagma.leavesBlock, 1, 2),
                TreeRegistry.findCellKit("conifer"))
        {
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

        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 1, blueSlimeLeavesProperties);
        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 2, purpleSlimeLeavesProperties);
        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 3, magmaSlimeLeavesProperties);

        TreeFamily blueSlimeTree = new TreeSlimeBlue();
        TreeFamily purpleSlimeTree = new TreeSlimePurple();
        TreeFamily magmaSlimeTree = new TreeSlimeMagma();
        Collections.addAll(trees, blueSlimeTree, purpleSlimeTree, magmaSlimeTree);
    }
    @Optional.Method(modid = "techreborn") //RUBBER TREE
    public static void RegisterBlocksTechReborn(IForgeRegistry<Block> registry){
        rubberBranch = new BlockDynamicBranchRubber();
        registry.register(rubberBranch);
        rubberBranchFilled = new BlockDynamicBranchRubber(true);
        registry.register(rubberBranchFilled);

        rubberLeavesProperties = setUpLeaves(TreeRubber.leavesBlock, 0, "deciduous", 2, 13);

        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 0, rubberLeavesProperties);

        TreeFamily rubberTree = new TreeRubber();
        Collections.addAll(trees, rubberTree);
    }
    @Optional.Method(modid = "thaumicbases") //GOLDEN, ENDER, AND HELLISH OAK
    public static void RegisterBlocksThaumicBases(IForgeRegistry<Block> registry){
        blockGoldenApple = (new BlockFruit("fruitgolden")).setDroppedItem(new ItemStack(Items.GOLDEN_APPLE));
        registry.register(blockGoldenApple);
        blockEnderPearl = (new BlockFruit("fruitender")).setDroppedItem(new ItemStack(Items.ENDER_PEARL));
        registry.register(blockEnderPearl);
        blockMagmaCream = (new BlockFruit("fruitmagma")).setDroppedItem(new ItemStack(Items.MAGMA_CREAM));
        registry.register(blockMagmaCream);

        goldenOakLeavesProperties = setUpLeaves(TreeGoldenOak.leavesBlock, 0, "deciduous");
        enderOakLeavesProperties = setUpLeaves(TreeEnderOak.leavesBlock, 0, "deciduous");
        hellishOakLeavesProperties = setUpLeaves(TreeHellishOak.leavesBlock, 0, "deciduous");

        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 8, goldenOakLeavesProperties);
        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 9, enderOakLeavesProperties);
        LeavesPaging.getLeavesBlockForSequence(DynamicTreesExC.MODID, 10, hellishOakLeavesProperties);

        TreeFamily goldenOakTree = new TreeGoldenOak();
        TreeFamily enderOakTree = new TreeEnderOak();
        TreeFamily hellishOakTree = new TreeHellishOak();
        Collections.addAll(trees, goldenOakTree, enderOakTree, hellishOakTree);
    }

    //
    // REGISTER ITEMS
    //
    @Optional.Method(modid = "extrautils2") //FERROUS JUNIPER
    public static void RegisterItemsExtraUtils2(IForgeRegistry<Item> registry){
        fejuniperSeedBurnt = new ItemDynamicSeedBurntFeJuniper();
        registry.register(fejuniperSeedBurnt);
        registry.register(new ItemBlock(fejuniperBranchBurnt).setRegistryName(Objects.requireNonNull(fejuniperBranchBurnt.getRegistryName())));
    }
    @Optional.Method(modid = "integrateddynamics") //MENRIL
    public static void RegisterItemsIntegratedDynamics(IForgeRegistry<Item> registry){
        registry.register(new ItemBlock(menrilBranchFilled).setRegistryName(Objects.requireNonNull(menrilBranchFilled.getRegistryName())));
    }
    @Optional.Method(modid = "techreborn") //RUBBER TREE
    public static void RegisterItemsTechReborn(IForgeRegistry<Item> registry){
        registry.register(new ItemBlock(rubberBranchFilled).setRegistryName(Objects.requireNonNull(rubberBranchFilled.getRegistryName())));
    }

    //
    // REGISTER RECIPES
    //
    @Optional.Method(modid = "quark") //BLOSSOMING AND SWAMP OAK
    public static void RegisterRecipesQuark(){
        setUpSeedRecipes("blossoming", new ItemStack(TreeBlossoming.saplingBlock, 1, 1));
        setUpSeedRecipes("swampOak", new ItemStack(TreeSwampOak.saplingBlock, 1, 0));
    }
    @Optional.Method(modid = "integrateddynamics") //MENRIL
    public static void RegisterRecipesIntegratedDynamics(){
        setUpSeedRecipes("menril", new ItemStack(TreeMenril.saplingBlock));
    }
    @Optional.Method(modid = "extrautils2") //FERROUS JUNIPER
    public static void RegisterRecipesExtraUtils2(){
        setUpSeedRecipes("ferrousJuniper", new ItemStack(TreeFeJuniper.saplingBlock));
        setUpSeedRecipes("ferrousJuniperBurnt", new ItemStack(TreeFeJuniper.saplingBlock, 1, 1), new ItemStack(fejuniperSeedBurnt));
    }
    @Optional.Method(modid = "tconstruct") //SLIME TREES
    public static void RegisterRecipesTinkersConstruct(){
        setUpSeedRecipes("slimeBlue", new ItemStack(TreeSlimeBlue.saplingBlock, 1, 0));
        setUpSeedRecipes("slimePurple", new ItemStack(TreeSlimePurple.saplingBlock, 1, 1));
        setUpSeedRecipes("slimeMagma", new ItemStack(TreeSlimeMagma.saplingBlock, 1, 2));
    }
    @Optional.Method(modid = "techreborn") //RUBBER TREE
    public static void RegisterRecipesTechReborn(){
        setUpSeedRecipes("rubber", new ItemStack(TreeRubber.saplingBlock));
    }
    @Optional.Method(modid = "thaumicbases") //GOLDEN, ENDER, AND HELLISH OAK
    public static void RegisterRecipesThaumicBases(){
        setUpSeedRecipes("goldenOak", new ItemStack(TreeGoldenOak.saplingBlock));
        setUpSeedRecipes("enderOak", new ItemStack(TreeEnderOak.saplingBlock));
        setUpSeedRecipes("hellishOak", new ItemStack(TreeHellishOak.saplingBlock));
    }

    //
    // REGISTER MODELS
    //
    @Optional.Method(modid = "extrautils2") //FERROUS JUNIPER
    public static void RegisterModelsExtraUtils2(){
        ModelHelper.regModel(fejuniperBranchBurnt);
        ModelLoader.setCustomStateMapper(fejuniperLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
   }
    @Optional.Method(modid = "tconstruct") //SLIME TREES
    public static void RegisterModelsTinkersConstruct(){
        ModelLoader.setCustomStateMapper(rootySlimyDirt, new StateMap.Builder().ignore(BlockRooty.LIFE).build());
    }
    @Optional.Method(modid = "integrateddynamics") //MENRIL
    public static void RegisterModelsIntegratedDynamics(){
        ModelHelper.regModel(menrilBranchFilled);
        ModelLoader.setCustomStateMapper(menrilLeaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build());
   }
    @Optional.Method(modid = "techreborn") //RUBBER TREE
    public static void RegisterModelsTechReborn(){
        ModelHelper.regModel(rubberBranchFilled);
    }

}
