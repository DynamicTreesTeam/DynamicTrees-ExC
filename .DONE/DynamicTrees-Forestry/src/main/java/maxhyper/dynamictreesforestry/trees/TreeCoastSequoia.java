package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockBranchThickForestry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeCoastSequoia extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.0"));
    public static int leavesMeta = 13;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.1"));
    public static int logMeta = 3;

    public class SpeciesPadauk extends Species {

        SpeciesPadauk(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.coastSequoiaLeavesProperties);

            setBasicGrowingParameters(0.3f, 45.0f, 20, 25, 2.1f);

            setSoilLongevity(80);

            generateSeed();

            setupStandardSeedDropping();

            addGenFeature(new FeatureGenClearVolume(50));//Clear a spot for the thick tree trunk
            addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
            addGenFeature(new FeatureGenMound(5));//Establish mounds
            addGenFeature(new FeatureGenRoots(7).setScaler(getRootScaler()).setLevelLimit(8));//Finally Generate Roots
        }

        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
            return (inRadius, trunkRadius) -> {
                float scale = MathHelper.clamp(trunkRadius >= 7 ? (trunkRadius / 24f) : 0, 0, 1);
                return (int) (inRadius * scale);
            };
        }

        @Override
        public int maxBranchRadius() {
            return 24;
        }

        @Override
        public boolean isThick() {
            return true;
        }

        @Override
        public int getWorldGenLeafMapHeight() {
            return 64;
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
//            if (!signal.isInTrunk()){
//                probMap[EnumFacing.UP.getIndex()] = 0;
//            }
//            probMap[EnumFacing.DOWN.getIndex()] = 0;
            probMap[signal.dir.getOpposite().getIndex()] = 0;

            return probMap;
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 4); // Vary the height energy by a psuedorandom hash function
        }

    }

    BlockSurfaceRoot surfaceRootBlock;

    public TreeCoastSequoia() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.COASTSEQUOIA));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");

        ModContent.coastSequoiaLeavesProperties.setTree(this);

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, logMeta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public int getRootColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return 0x7e3a24;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new TreeCoastSequoia.SpeciesPadauk(this));
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList.add(surfaceRootBlock);
        blockList.add(((BlockBranchThick)getDynamicBranch()).getPairSide(true));
        return super.getRegisterableBlocks(blockList);
    }

    @Override
    public BlockSurfaceRoot getSurfaceRoots() {
        return surfaceRootBlock;
    }

    @Override
    public BlockBranch createBranch() {
        String branchName = getName() + "branch";
        return new BlockBranchSequoia(branchName, EnumForestryWoodType.SEQUOIA);
    }

    // Code from DT BOP
    protected class BlockBranchSequoia extends BlockBranchThickForestry {

        public BlockBranchSequoia(String name, EnumForestryWoodType type) {
            this(Material.WOOD, name, type);
        }

        public BlockBranchSequoia(Material material, String name, EnumForestryWoodType type) {
            super(material, name, type);
            otherBlock = new BlockBranchSequoia(material, name + "x", true, type);
            otherBlock.otherBlock = this;
            woodType = type;
            cacheBranchThickStates();
        }

        protected BlockBranchSequoia(Material material, String name, boolean extended, EnumForestryWoodType type) {
            super(material, name, extended, type);
            woodType = type;
        }

        @Override
        public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
            int radius = getRadius(blockState);
            return EnumForestryWoodType.GIGANTEUM.getHardness() * (radius * radius) / 64.0f * 8.0f;
        }

        @Override
        protected int getMaxSignalDepth() {
            return 64;
        }

    }
}