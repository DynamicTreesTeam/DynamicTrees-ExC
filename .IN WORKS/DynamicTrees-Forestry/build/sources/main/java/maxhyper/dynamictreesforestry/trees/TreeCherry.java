package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
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

public class TreeCherry extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.0"));
    public static int leavesMeta = 6;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.3"));
    public static int logMeta = 3;

    public class SpeciesCherry extends Species {

        SpeciesCherry(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.cherryLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.4f, 10.0f, 1, 4, 1f);

            generateSeed();
            //setupStandardSeedDropping();

            addGenFeature(new FeatureGenFruitLeaves(8, 10, ModContent.cherryLeavesProperties.getDynamicLeavesState(), ModContent.fruitCherryLeavesProperties.getDynamicLeavesState(), 0.1f));

            addGenFeature(new FeatureGenFruit(ModContent.cherryFruit).setRayDistance(4));
        }
    }

    public TreeCherry() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, "cherry"));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.cherryLeavesProperties.setTree(this);
        ModContent.fruitCherryLeavesProperties.setTree(this);

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
        return 0xa97831;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesCherry(this));
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchBasic(this.getName() + "branch"){
            @Override
            public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
                int radius = getRadius(blockState);
                return EnumForestryWoodType.CHERRY.getHardness() * (radius * radius) / 64.0f * 8.0f;
            }
        };
    }
}