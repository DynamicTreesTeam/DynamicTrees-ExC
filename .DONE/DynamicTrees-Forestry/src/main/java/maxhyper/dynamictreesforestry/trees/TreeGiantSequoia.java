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
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

//public class TreeGiantSequoia extends TreeFamily {
//
//    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
//            "leaves.decorative.0"));
//    public static int leavesMeta = 14;
//    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
//            "logs.6"));
//    public static int logMeta = 0;
//
//    public class SpeciesPadauk extends Species {
//
//        SpeciesPadauk(TreeFamily treeFamily) {
//            super(treeFamily.getName(), treeFamily, ModContent.giantSequoiaLeavesProperties);
//
//            setBasicGrowingParameters(0.3f, 80.0f, 16, 25, 0.7f);
//            //setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.DARKOAK));
//
//            setSoilLongevity(80);
//
//            generateSeed();
//
//            setupStandardSeedDropping();
//
//            addGenFeature(new FeatureGenClearVolume(70));//Clear a spot for the thick tree trunk
//            addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
//            addGenFeature(new FeatureGenMound(5));//Establish mounds
//            addGenFeature(new FeatureGenRoots(16).setScaler(getRootScaler()));//Finally Generate Roots
//        }
//
//        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
//            return (inRadius, trunkRadius) -> {
//                float scale = MathHelper.clamp(trunkRadius >= 7 ? (trunkRadius / 12f) : 0, 0, 1);
//                return (int) (inRadius * scale);
//            };
//        }
//
//        @Override
//        public int maxBranchRadius() {
//            return 24;
//        }
//
//        @Override
//        public boolean isThick() {
//            return true;
//        }
//
//        @Override
//        public int getWorldGenLeafMapHeight() {
//            return 86;
//        }
//
//        @Override
//        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
////            if (!signal.isInTrunk()){
////                probMap[EnumFacing.UP.getIndex()] = 0;
////            }
////            probMap[EnumFacing.DOWN.getIndex()] = 0;
//            probMap[signal.dir.getOpposite().getIndex()] = 0;
//
//            return probMap;
//        }
//
//        @Override
//        public float getEnergy(World world, BlockPos pos) {
//            long day = world.getWorldTime() / 24000L;
//            int month = (int) day / 30; // Change the hashs every in-game month
//
//            return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 4); // Vary the height energy by a psuedorandom hash function
//        }
//
//    }
//
//    BlockSurfaceRoot surfaceRootBlock;
//
//    public TreeGiantSequoia() {
//        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.GIANTSEQUOIA));
//
//        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));
//
//        surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");
//
//        ModContent.giantSequoiaLeavesProperties.setTree(this);
//
//        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
//    }
//
//    @Override
//    public ItemStack getPrimitiveLogItemStack(int qty) {
//        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, logMeta);
//        stack.setCount(MathHelper.clamp(qty, 0, 64));
//        return stack;
//    }
//
//    @Override
//    public int getRootColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
//        return 0x82442c;
//    }
//
//    @Override
//    public void createSpecies() {
//        setCommonSpecies(new TreeGiantSequoia.SpeciesPadauk(this));
//    }
//
//    @Override
//    public List<Block> getRegisterableBlocks(List<Block> blockList) {
//        blockList.add(surfaceRootBlock);
//        blockList.add(((BlockBranchThick)getDynamicBranch()).getPairSide(true));
//        return super.getRegisterableBlocks(blockList);
//    }
//
//    @Override
//    public BlockSurfaceRoot getSurfaceRoots() {
//        return surfaceRootBlock;
//    }
//
//    @Override
//    public BlockBranch createBranch() {
//        String branchName = getName() + "branch";
//        return new BlockBranchSequoia(branchName);
//    }
//
//    // Code from DT BOP
//    protected class BlockBranchSequoia extends BlockBranchThick {
//
//        public BlockBranchSequoia(String name) {
//            this(Material.WOOD, name);
//        }
//
//        public BlockBranchSequoia(Material material, String name) {
//            super(material, name);
//            otherBlock = new BlockBranchSequoia(material, name + "x", true);
//            otherBlock.otherBlock = this;
//
//            cacheBranchThickStates();
//        }
//
//        protected BlockBranchSequoia(Material material, String name, boolean extended) {
//            super(material, name, extended);
//        }
//
//        @Override
//        protected int getMaxSignalDepth() {
//            return 86;
//        }
//
//    }
//}