package maxhyper.dynamictreescuisine.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.models.ModelEntityFallingTree;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreescuisine.DynamicTreesCuisine;
import maxhyper.dynamictreescuisine.ModConfigs;
import maxhyper.dynamictreescuisine.blocks.BlockDynamicLeavesFruit;
import maxhyper.dynamictreescuisine.genfeatures.FeatureGenFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
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
import snownee.cuisine.library.RarityManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TreeCitrus extends TreeFamily {

    public static Block logBlock = CuisineRegistry.LOG;

    public static ItemStack getFruitWithRarity (ItemStack fruit, Random rand){
        if (rand.nextInt(10) == 0)
        {
            RarityManager.setRarity(fruit, fruit.getRarity().ordinal() + 1);
        }
        return fruit;
    }
    private static AxisAlignedBB createBox (float radius, float height, float stemLength, float fraction){
        float topHeight = fraction - stemLength;
        float bottomHeight = topHeight - height;
        return new AxisAlignedBB(
                ((fraction/2) - radius)/fraction, topHeight/fraction, ((fraction/2) - radius)/fraction,
                ((fraction/2) + radius)/fraction, bottomHeight/fraction, ((fraction/2) + radius)/fraction);
    }

    static final AxisAlignedBB[] LEMON_AABB = new AxisAlignedBB[] {
            createBox(1,1,0, 16),
            createBox(1,2,0, 16),
            createBox(2f,5,0, 20),
            createBox(2f,5,1.25f, 20)
    };
    static final AxisAlignedBB[] MANDARIN_AABB = new AxisAlignedBB[] {
            createBox(1,1,0, 16),
            createBox(1,2,0, 16),
            createBox(2f,4,0, 20),
            createBox(2f,4,1.25f, 20)
    };
    public static final AxisAlignedBB[] GRAPEFRUIT_AABB = new AxisAlignedBB[] {
            createBox(1,1,0, 16),
            createBox(1,2,0, 16),
            createBox(2.5f,5,0, 20),
            createBox(3f,6,1.25f, 20)
    };

    public enum citrusType {
        POMELO(
                null, //all year-round
                3,
                0.3f,
                7.0f,
                2,
                1.2f,
                CuisineRegistry.LEAVES_POMELO,
                new int[]{0,7},
                11,
                0,
                0xF7F67E,
                GRAPEFRUIT_AABB),
        CITRON(
                1.5f, //autumn-winter
                2,
                0.6f,
                7.0f,
                4,
                1.2f,
                CuisineRegistry.LEAVES_CITRON,
                new int[]{1,8},
                10,
                1,
                0xDDCC58,
                LEMON_AABB),
        MANDARIN(
                0f, //summer
                4,
                0.3f,
                7.0f,
                3,
                1.2f,
                CuisineRegistry.LEAVES_MANDARIN,
                new int[]{2,9},
                9,
                2,
                15763993,
                MANDARIN_AABB),
        GRAPEFRUIT(
                3f, //spring
                4,
                0.2f,
                8.0f,
                3,
                1.2f,
                CuisineRegistry.LEAVES_GRAPEFRUIT,
                new int[]{3,10},
                14,
                3,
                0xF08A19,
                GRAPEFRUIT_AABB),
        ORANGE(
                2.5f, //winter-spring
                4,
                0.2f,
                8.0f,
                3,
                1.2f,
                CuisineRegistry.LEAVES_ORANGE,
                new int[]{4,11},
                12,
                4,
                0xF08A19,
                null),
        LEMON(
                2.5f, //winter-spring
                1,
                0.6f,
                6.0f,
                3,
                1.2f,
                CuisineRegistry.LEAVES_LEMON,
                new int[]{5,12},
                13,
                5,
                0xEBCA4B,
                LEMON_AABB),
        LIME(
                0f, //summer
                1,
                0.6f,
                6.0f,
                3,
                1.2f,
                CuisineRegistry.LEAVES_LIME,
                new int[]{6,13},
                15,
                6,
                0xCADA76,
                LEMON_AABB);

        public Float fruitingOffset;
        public int soilLongevity;
        public float tapering;
        public float energy;
        public int lowestBranchHeight;
        public float growthRate;

        public Block primitiveLeaves;
        public int fruitItemMeta;
        public int saplingMeta;
        public int fruitTint;

        public ILeavesProperties[] leavesProperties;
        public BlockDynamicLeaves leavesBlock;
        public BlockFruit fruitBlock;

        citrusType (Float frOff, int soilLongevity, float tapering, float energy, int lowestBranch, float growthRate, Block primLeaves, int[] primLeavesItemMeta, int fruitItemMeta, int saplingMeta, int fruitTint, AxisAlignedBB[] fruitBlockBox){
            this.fruitingOffset = frOff;
            this.soilLongevity = soilLongevity;
            this.tapering = tapering;
            this.energy = energy;
            this.lowestBranchHeight = lowestBranch;
            this.growthRate = growthRate;
            this.primitiveLeaves = primLeaves;
            this.fruitItemMeta = fruitItemMeta;
            this.saplingMeta = saplingMeta;
            this.fruitTint = fruitTint;
            this.leavesProperties = new ILeavesProperties[]{
                    setUpLeaves(primLeaves, primLeavesItemMeta[0], "deciduous"),
                    setUpLeaves(primLeaves, primLeavesItemMeta[0], "deciduous"),
                    setUpLeaves(primLeaves, primLeavesItemMeta[1], "deciduous"),
                    setUpLeaves(primLeaves, primLeavesItemMeta[0], "deciduous")
            };
            this.leavesBlock = new BlockDynamicLeavesFruit("leaves_" + this, this);
            for(int i=0;i<4;i++){
                this.leavesProperties[i].setDynamicLeavesState(leavesBlock.getDefaultState().withProperty(BlockDynamicLeaves.TREE, i));
                leavesBlock.setProperties(i, leavesProperties[i]);
            }
            this.fruitBlock = new BlockFruit("fruit_" + this){
                @Override @SideOnly(Side.CLIENT) public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT_MIPPED; }
                @Override public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
                    if (fruitBlockBox != null)
                        return fruitBlockBox[state.getValue(AGE)];
                    else
                        return super.getBoundingBox(state, source, pos);
                }
                @Override public ItemStack getFruitDrop() { return getFruitWithRarity(super.getFruitDrop(), new Random()); }
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
            return new ResourceLocation(family.getName().getResourceDomain(), this.toString());
        }
        public ItemStack getPrimitiveSapling (){ return new ItemStack(CuisineRegistry.SAPLING, 1, saplingMeta); }
        public ItemStack getPrimitiveFruit () { return new ItemStack(CuisineRegistry.BASIC_FOOD, 1, fruitItemMeta); }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public static class SpeciesCitrus extends Species {

        public citrusType type;
        private boolean isSeasonal = true;

        public SpeciesCitrus(TreeFamily treeFamily, citrusType type) {
            super(type.getName(treeFamily), treeFamily, type.leavesProperties[1]);

            this.type = type;
            for (int i=0;i<4;i++){
                type.leavesProperties[i].setTree(treeFamily);
                addValidLeavesBlocks(type.leavesProperties[i]);
            }
            treeFamily.addConnectableVanillaLeaves((state) -> state.getBlock() == type.leavesBlock);

            setRequiresTileEntity(true);
            setSoilLongevity(soilLongevity);

            // Set growing parameters.
            this.setBasicGrowingParameters(type.tapering, type.energy, 1, type.lowestBranchHeight, type.growthRate);

            // Setup environment factors.
            this.envFactor(BiomeDictionary.Type.HOT, 0.9F);
            this.envFactor(BiomeDictionary.Type.DRY, 0.9F);

            // Setup seed.
            this.generateSeed();

            if (type.fruitingOffset == null){
                isSeasonal = false;
            } else {
                setFlowerSeasonHold(type.fruitingOffset - 0.5f, type.fruitingOffset + 0.5f);
            }

            addDropCreator(new DropCreatorSeed() {
                @Override public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
                    float rarity = getHarvestRarity();
                    rarity *= (fortune + 1) / 128f; //Extra rare so players are incentivized to get fruits from growing instead of chopping
                    rarity *= Math.min(species.seasonalSeedDropFactor(world, leafPos) + 0.15f, 1.0);
                    if(rarity > random.nextFloat()) dropList.add(getFruit(world.rand)); //1 in 128 chance to drop a fruit on destruction..
                    return dropList;
                }

                private ItemStack getFruit (Random rand){
                    return getFruitWithRarity(new ItemStack(CuisineRegistry.BASIC_FOOD, 1, type.fruitItemMeta), rand);
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
                            dropList.add(getFruit(random));
                    return dropList;
                }
            });

            if (ModConfigs.fruityLeaves)
                addGenFeature(new FeatureGenFruitLeaves((int)type.energy, (int)type.energy + 5, type.leavesProperties, 0.5f).setFruitingRadius(5));

            type.fruitBlock.setSpecies(this);
            addGenFeature(new FeatureGenFruit(type.fruitBlock).setFruitingRadius(5));
            type.fruitBlock.setDroppedItem(new ItemStack(CuisineRegistry.BASIC_FOOD, 1, type.fruitItemMeta));
        }

        @Override
        public int colorTreeQuads(int defaultColor, ModelEntityFallingTree.TreeQuadData treeQuad, @Nullable EntityFallingTree entity) {
            if (treeQuad.bakedQuad.getTintIndex() == 1)
                return type.fruitTint;
             else
                return defaultColor;
        }

        @Override
        public float seasonalFruitProductionFactor(World world, BlockPos pos) {
            if (type.fruitingOffset == null) return 1;
            return SeasonHelper.globalSeasonalFruitProductionFactor(world, pos, type.fruitingOffset);
        }

        @Override
        public boolean testFlowerSeasonHold(World world, BlockPos pos, float seasonValue) {
            return isSeasonal && SeasonHelper.isSeasonBetween(seasonValue, flowerSeasonHoldMin, flowerSeasonHoldMax);
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return isOneOfBiomes(biome, Biomes.PLAINS);
        }

        @Override
        public boolean showSpeciesOnWaila() {
            return true;
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