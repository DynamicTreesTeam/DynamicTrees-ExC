package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class TreeWenge extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.1"));
    public static int leavesMeta = 11;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.1"));
    public static int logMeta = 1;

    public class SpeciesWenge extends Species {

        SpeciesWenge(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.wengeLeavesProperties);

            setBasicGrowingParameters(0.4f, 12.0f, 5, 3, 1f);

            generateSeed();
            setupStandardSeedDropping();

            addGenFeature(new FeatureGenFlareBottom());
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {

            probMap[EnumFacing.DOWN.getIndex()] = 0;
            probMap[signal.dir.getOpposite().getIndex()] = 0;

            return probMap;
        }

        @Override
        public int maxBranchRadius() {
            return 16;
        }

        @Override
        public boolean isThick() {
            return true;
        }
    }

    public TreeWenge() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.WENGE));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.wengeLeavesProperties.setTree(this);

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
        return 0x565148;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesWenge(this));
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.WENGE);
    }
}