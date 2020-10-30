package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeSipiri extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.1"));
    public static int leavesMeta = 15;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.3"));
    public static int logMeta = 2;

    public class SpeciesSipiri extends Species {

        SpeciesSipiri(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.sipiriLeavesProperties);

            setBasicGrowingParameters(0.25f, 20.0f, 30, 10, 0.9f);

            generateSeed();

            setupStandardSeedDropping();

            addGenFeature(new FeatureGenVine());
            addGenFeature(new FeatureGenFlareBottom());
            addGenFeature(new FeatureGenRoots(6).setScaler(getRootScaler()));//Finally Generate Roots
        }

        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
            return (inRadius, trunkRadius) -> {
                float scale = MathHelper.clamp(trunkRadius >= 6 ? (trunkRadius / 24f) : 0, 0, 1);
                return (int) (inRadius * scale);
            };
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
            if (signal.numTurns > 0){
                for (EnumFacing dir : EnumFacing.HORIZONTALS){
                    probMap[dir.getIndex()] = 0;
                }
                probMap[EnumFacing.UP.getIndex()] = 0;
            }

            probMap[signal.dir.getOpposite().getIndex()] = 0;

            return probMap;
        }

        @Override
        public int maxBranchRadius() {
            return 8;
        }

        @Override
        public boolean isThick() {
            return true;
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 6); // Vary the height energy by a psuedorandom hash function
        }
    }

    BlockSurfaceRoot surfaceRootBlock;

    public TreeSipiri() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.GREENHEART));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");

        ModContent.sipiriLeavesProperties.setTree(this);

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
        return 0x557a5e;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesSipiri(this));
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList.add(surfaceRootBlock);
        return super.getRegisterableBlocks(blockList);
    }

    @Override
    public BlockSurfaceRoot getSurfaceRoots() {
        return surfaceRootBlock;
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.GREENHEART);
    }
}