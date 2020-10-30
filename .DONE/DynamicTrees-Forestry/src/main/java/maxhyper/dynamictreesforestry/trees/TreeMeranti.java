package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeMeranti extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.1"));
    public static int leavesMeta = 5;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.2"));
    public static int logMeta = 2;

    public class SpeciesMeranti extends Species {

        SpeciesMeranti(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.merantiLeavesProperties);

            setBasicGrowingParameters(0.5f, 18.0f, 30, 12, 0.9f);
            setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.DARKOAK));

            generateSeed();

            setupStandardSeedDropping();

            addGenFeature(new FeatureGenFlareBottom());
            addGenFeature(new FeatureGenMound(5));
            addGenFeature(new FeatureGenVine().setQuantity(16).setMaxLength(16));
            addGenFeature(new FeatureGenRoots(14).setScaler(getRootScaler()));//Finally Generate Roots
        }

        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
            return (inRadius, trunkRadius) -> {
                float scale = MathHelper.clamp(trunkRadius >= 14 ? (trunkRadius / 20f) : 0, 0, 1);
                return (int) (inRadius * scale);
            };
        }

        @Override
        public int maxBranchRadius() {
            return 14;
        }

        @Override
        public boolean isThick() {
            return true;
        }
    }

    BlockSurfaceRoot surfaceRootBlock;

    public TreeMeranti() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.MAHOGANY));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");

        ModContent.merantiLeavesProperties.setTree(this);

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
        return 0x8d5e51;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new TreeMeranti.SpeciesMeranti(this));
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
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.MAHOGANY);
    }
}