package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockBranchThickForestry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class TreeZebrawood extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.1"));
    public static int leavesMeta = 4;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.7"));
    public static int logMeta = 0;

    public class SpeciesZebrawood extends Species {

        SpeciesZebrawood(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.zebrawoodLeavesProperties);

            setBasicGrowingParameters(0.6f, 16.0f, 30, 10, 0.9f);
            setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.DARKOAK));

            generateSeed();

            setupStandardSeedDropping();

            addGenFeature(new FeatureGenFlareBottom());
            addGenFeature(new FeatureGenMound(5));
        }

        @Override
        public int maxBranchRadius() {
            return 18;
        }

        @Override
        public boolean isThick() {
            return true;
        }
    }

    public TreeZebrawood() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.ZEBRAWOOD));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.zebrawoodLeavesProperties.setTree(this);

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
        return 0x98774d;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesZebrawood(this));
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.ZEBRAWOOD);
    }
}