package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.genfeatures.FeatureGenFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class TreeLemon extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.0"));
    public static int leavesMeta = 7;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.5"));
    public static int logMeta = 3;

    public class SpeciesLemon extends Species {

        SpeciesLemon(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.lemonLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.6f, 6.0f, 1, 3, 1.2f);

            generateSeed();
            //setupStandardSeedDropping();

            addGenFeature(new FeatureGenFruitLeaves(6, 8, ModContent.lemonLeavesProperties.getDynamicLeavesState(), ModContent.fruitLemonLeavesProperties.getDynamicLeavesState(), 0.5f));

            addGenFeature(new FeatureGenFruit(ModContent.lemonFruit).setRayDistance(2).setFruitingRadius(3));
        }
    }

    public TreeLemon() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.LEMON));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.lemonLeavesProperties.setTree(this);
        ModContent.fruitLemonLeavesProperties.setTree(this);

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
        return 0x7d811f;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesLemon(this));
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchBasic(this.getName() + "branch"){
            @Override
            public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
                int radius = getRadius(blockState);
                return EnumForestryWoodType.CITRUS.getHardness() * (radius * radius) / 64.0f * 8.0f;
            }
        };
    }

}