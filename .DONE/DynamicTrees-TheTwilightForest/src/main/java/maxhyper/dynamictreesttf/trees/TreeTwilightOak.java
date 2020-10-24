package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilightThick;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeTwilightOak extends TreeFamily {

    public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
    public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
    public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
    public static IBlockState leavesState = leavesBlock.getStateFromMeta(0);
    public static int logsMeta = 0;
    public static int saplingMeta = 0;
    public static int saplingMetaRobust = 4;

    public class SpeciesRobustTwilightOak extends Species {

        SpeciesRobustTwilightOak(TreeFamily treeFamily) {
            super(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakRobust"), treeFamily, ModContent.robustTwilightOakLeavesProperties);

            setBasicGrowingParameters(0.4f, 25.0f, 50, 15, 1.5f);

            setSoilLongevity(50);

            generateSeed();

            addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 20, 10));
            addGenFeature(new FeatureGenMound(6));//Establish mounds
            addGenFeature(new FeatureGenClearVolume(20));
            addGenFeature(new FeatureGenRoots(10).setScaler(getRootScaler()));//Finally Generate Roots
        }
        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
            return (inRadius, trunkRadius) -> {
                float scale = MathHelper.clamp(trunkRadius >= 13 ? (trunkRadius / 24f) : 0, 0, 1);
                return (int) (inRadius * scale);
            };
        }

        public Species generateSeed() {
            Seed seed = new Seed("robustTwilightOakSeed");
            setSeedStack(new ItemStack(seed));
            return this;
        }

        @Override
        public boolean isThick() {
            return true;
        }

        @Override
        public int maxBranchRadius() {
            return 18;
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
            EnumFacing originDir = signal.dir.getOpposite();

            probMap[0] = 1;
            probMap[1] = signal.isInTrunk() ? getUpProbability() : 0; //disable up when out of trunk
            probMap[2] = probMap[3] = probMap[4] = probMap[5] =  signal.isInTrunk() ? 1 : 20;
            probMap[originDir.ordinal()] = 0;
            probMap[signal.dir.getIndex()] *= 2;

            return probMap;
        }

        @Override
        protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
            if (signal.energy < 6f && !signal.isInTrunk()) {
                signal.energy = 6f;
            }
            return newDir;
        }

    }

    public class SpeciesSicklyTwilightOak extends Species {

        SpeciesSicklyTwilightOak(TreeFamily treeFamily) {
            super(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakSickly"), treeFamily, ModContent.sicklyTwilightOakLeavesProperties);

            setRequiresTileEntity(true);

            setBasicGrowingParameters(tapering, 9, upProbability, lowestBranchHeight, growthRate);

            generateSeed();
            setupStandardSeedDropping();
        }

        public Species generateSeed() {
            Seed seed = new Seed("sicklyTwilightOakSeed");
            setSeedStack(new ItemStack(seed));
            return this;
        }
    }

    public static BlockSurfaceRoot twilightRoots;
    public static Species sicklySpecies;
    public TreeTwilightOak() {
        super(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOak"));

        setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

        ModContent.sicklyTwilightOakLeavesProperties.setTree(this);
        ModContent.robustTwilightOakLeavesProperties.setTree(this);
        twilightRoots = new BlockSurfaceRoot(Material.WOOD, "twilight_roots");

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, logsMeta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesRobustTwilightOak(this));
        sicklySpecies = new SpeciesSicklyTwilightOak(this);
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        speciesRegistry.register(sicklySpecies);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        itemList.add(sicklySpecies.getSeed());
        return super.getRegisterableItems(itemList);
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList.add(twilightRoots);
        return super.getRegisterableBlocks(blockList);
    }

    @Override
    public BlockSurfaceRoot getSurfaceRoots() {
        return twilightRoots;
    }

    @Override
    public BlockBranch createBranch() {
        String branchName = "twilightOakbranch";
        return new BlockBranchTwilightThick(branchName);
    }

}
