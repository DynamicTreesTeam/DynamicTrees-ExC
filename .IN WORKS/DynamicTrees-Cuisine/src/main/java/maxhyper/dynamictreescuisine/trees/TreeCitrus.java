package maxhyper.dynamictreescuisine.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreescuisine.DynamicTreesCuisine;
import maxhyper.dynamictreescuisine.blocks.BlockDynamicLeavesFruit;
import maxhyper.dynamictreescuisine.genfeatures.FeatureGenFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import snownee.cuisine.CuisineRegistry;
import snownee.cuisine.blocks.BlockModSapling;

import java.util.List;
import java.util.Random;

public class TreeCitrus extends TreeFamily {

    public static Block logBlock = CuisineRegistry.LOG;

    public enum citrusType {
        POMELO(
                2.5f,
                CuisineRegistry.LEAVES_POMELO,
                new int[]{0,7},
                11,
                0,
                0xF7F67E),
        CITRON(
                2.5f,
                CuisineRegistry.LEAVES_CITRON,
                new int[]{1,8},
                10,
                1,
                0xDDCC58),
        MANDARIN(
                2.5f,
                CuisineRegistry.LEAVES_MANDARIN,
                new int[]{2,9},
                9,
                2,
                15763993),
        GRAPEFRUIT(
                2.5f,
                CuisineRegistry.LEAVES_GRAPEFRUIT,
                new int[]{3,10},
                14,
                3,
                0xF08A19),
        ORANGE(
                2.5f, CuisineRegistry.LEAVES_ORANGE,
                new int[]{4,11},
                12,
                4,
                0xF08A19),
        LEMON(
                2.5f, CuisineRegistry.LEAVES_LEMON,
                new int[]{5,12},
                13,
                5,
                0xEBCA4B),
        LIME(
                2.5f, CuisineRegistry.LEAVES_LIME,
                new int[]{6,13},
                15,
                6,
                0xCADA76);

        public float fruitingOffset;
        public Block primitiveLeaves;
        public int fruitItemMeta;
        public int saplingMeta;
        public int fruitTintIndex;

        public ILeavesProperties[] leavesProperties;
        public BlockDynamicLeaves leavesBlock;
        public BlockFruit fruitBlock;
        citrusType (float frOff, Block primLeaves, int[] primLeavesItemMeta, int fruitItemMeta, int saplingMeta, int fruitTintIndex){
            this.fruitingOffset = frOff;
            this.primitiveLeaves = primLeaves;
            this.fruitItemMeta = fruitItemMeta;
            this.saplingMeta = saplingMeta;
            this.fruitTintIndex = fruitTintIndex;
            this.leavesProperties = new ILeavesProperties[]{
                    setUpLeaves(primLeaves, primLeavesItemMeta[0], "deciduous"),
                    setUpLeaves(primLeaves, primLeavesItemMeta[0], "deciduous"),
                    setUpLeaves(primLeaves, primLeavesItemMeta[1], "deciduous"),
                    setUpLeaves(primLeaves, primLeavesItemMeta[0], "deciduous")
            };
            this.leavesBlock = new BlockDynamicLeavesFruit("leaves_"+this.name().toLowerCase(), this);
            for(int i=0;i<4;i++){
                this.leavesProperties[i].setDynamicLeavesState(leavesBlock.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
                leavesBlock.setProperties(i, leavesProperties[i]);
            }
            this.fruitBlock = new BlockFruit("fruit_"+this.name().toLowerCase()){
                @Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT_MIPPED; }
            };
        }

        private static ILeavesProperties setUpLeaves (Block leavesBlock, int leavesItemMeta, String cellKit){
            ILeavesProperties leavesProperties;
            leavesProperties = new LeavesProperties(
                    leavesBlock.getDefaultState(),
                    new ItemStack(CuisineRegistry.SHEARED_LEAVES, 1, leavesItemMeta),
                    TreeRegistry.findCellKit(cellKit))
            {
                @Override public ItemStack getPrimitiveLeavesItemStack() {
                    return new ItemStack(CuisineRegistry.SHEARED_LEAVES, 1, leavesItemMeta);
                }
            };
            return leavesProperties;
        }

        public ResourceLocation getName(TreeFamily family){
            return new ResourceLocation(family.getName().getResourceDomain(), this.name().toLowerCase());
        }
        public ItemStack getPrimitiveFruitItem (){
            return new ItemStack(CuisineRegistry.BASIC_FOOD, 1, fruitItemMeta);
        }
        public IBlockState getPrimitiveSapling (){
            return CuisineRegistry.SAPLING.getStateFromMeta(saplingMeta);
        }
    }

    public static class SpeciesCitrus extends Species {

        public citrusType type;

        public SpeciesCitrus(TreeFamily treeFamily, citrusType type) {
            super(type.getName(treeFamily), treeFamily, type.leavesProperties[0]);

            this.type = type;
            for (int i=0;i<4;i++) type.leavesProperties[i].setTree(treeFamily);
            treeFamily.addConnectableVanillaLeaves((state) -> state.getBlock() == type.leavesBlock);

            setRequiresTileEntity(true);
            setSoilLongevity(2);

            // Set growing parameters.
            this.setBasicGrowingParameters(0.3F, 12.0F, this.upProbability, this.lowestBranchHeight, 0.9F);

            // Setup environment factors.
            this.envFactor(BiomeDictionary.Type.HOT, 0.9F);
            this.envFactor(BiomeDictionary.Type.DRY, 0.9F);

            // Setup seed.
            this.generateSeed();

            setFlowerSeasonHold(type.fruitingOffset - 0.5f, type.fruitingOffset + 0.5f);

            addDropCreator(new DropCreatorSeed() {
                @Override public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
                    float rarity = getHarvestRarity();
                    rarity *= (fortune + 1) / 128f; //Extra rare so players are incentivized to get fruits from growing instead of chopping
                    rarity *= Math.min(species.seasonalSeedDropFactor(world, leafPos) + 0.15f, 1.0);
                    if(rarity > random.nextFloat()) dropList.add(getFruit ()); //1 in 128 chance to drop a fruit on destruction..
                    return dropList;
                }

                private ItemStack getFruit (){
                    return type.getPrimitiveFruitItem();
                }

                @Override public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
                    int chance = 40;
                    //Hokey fortune stuff here to match Vanilla logic.
                    if (fortune > 0) {
                        chance -= 2 << fortune;
                        if (chance < 10) chance = 10;
                    }
                    float seasonFactor = 1.0f;
                    if(access instanceof World) {
                        World world = (World) access;
                        if(!world.isRemote) seasonFactor = species.seasonalSeedDropFactor(world, breakPos);
                    }
                    if(random.nextInt((int) (chance / getLeavesRarity())) == 0)
                        if(seasonFactor > random.nextFloat())
                            dropList.add(getFruit());
                    return dropList;
                }
            });

            addGenFeature(new FeatureGenFruitLeaves(6, 16, type.leavesProperties, 0.5f).setFruitingRadius(6));

            type.fruitBlock.setSpecies(this);
            addGenFeature(new FeatureGenFruit(type.fruitBlock).setFruitingRadius(6));
        }

        @Override
        public float seasonalFruitProductionFactor(World world, BlockPos pos) {
            float offset = type.fruitingOffset;
            return SeasonHelper.globalSeasonalFruitProductionFactor(world, pos, offset);
        }

        @Override
        public boolean testFlowerSeasonHold(World world, BlockPos pos, float seasonValue) {
            return SeasonHelper.isSeasonBetween(seasonValue, flowerSeasonHoldMin, flowerSeasonHoldMax);
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return isOneOfBiomes(biome, Biomes.PLAINS);
        }

    }

    static SpeciesCitrus pomelo, citron, mandarin, grapefruit, orange, lemon, lime;

    public static Item[] getSeeds (){
        return new Item[]{
                pomelo.getSeed(),
                citron.getSeed(),
                mandarin.getSeed(),
                grapefruit.getSeed(),
                orange.getSeed(),
                lemon.getSeed(),
                lime.getSeed()
        };
    }

    public TreeCitrus() {
        super(new ResourceLocation(DynamicTreesCuisine.MODID, "citrus"));
        setPrimitiveLog(logBlock.getDefaultState());
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(logBlock, qty);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        pomelo = new SpeciesCitrus(this, citrusType.POMELO);
        citron = new SpeciesCitrus(this, citrusType.CITRON);
        mandarin = new SpeciesCitrus(this, citrusType.MANDARIN);
        grapefruit = new SpeciesCitrus(this, citrusType.GRAPEFRUIT);
        orange = new SpeciesCitrus(this, citrusType.ORANGE);
        lemon = new SpeciesCitrus(this, citrusType.LEMON);
        lime = new SpeciesCitrus(this, citrusType.LIME);
        setCommonSpecies(lemon);
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        speciesRegistry.registerAll(pomelo, citron, mandarin, grapefruit, orange, lime);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        itemList.add(pomelo.getSeed());
        itemList.add(citron.getSeed());
        itemList.add(mandarin.getSeed());
        itemList.add(grapefruit.getSeed());
        itemList.add(orange.getSeed());
        itemList.add(lime.getSeed());
        return super.getRegisterableItems(itemList);
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList.add(pomelo.type.leavesBlock);
        blockList.add(citron.type.leavesBlock);
        blockList.add(mandarin.type.leavesBlock);
        blockList.add(grapefruit.type.leavesBlock);
        blockList.add(orange.type.leavesBlock);
        blockList.add(lemon.type.leavesBlock);
        blockList.add(lime.type.leavesBlock);

        blockList.add(pomelo.type.fruitBlock);
        blockList.add(citron.type.fruitBlock);
        blockList.add(mandarin.type.fruitBlock);
        blockList.add(grapefruit.type.fruitBlock);
        blockList.add(orange.type.fruitBlock);
        blockList.add(lemon.type.fruitBlock);
        blockList.add(lime.type.fruitBlock);

        return super.getRegisterableBlocks(blockList);
    }
}