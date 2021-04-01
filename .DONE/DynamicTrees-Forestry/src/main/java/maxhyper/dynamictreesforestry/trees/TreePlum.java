package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.models.ModelEntityFallingTree;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesFruit;
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

import javax.annotation.Nullable;
import java.util.Objects;

public class TreePlum extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.0"));
    public static int leavesMeta = 8;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.5"));
    public static int logMeta = 1;

    public static float fruitingOffset = 0f; //summer

    public class SpeciesPlum extends Species {

        SpeciesPlum(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.plumLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.6f, 6.0f, 1, 3, 1.2f);

            addValidLeavesBlocks(ModContent.fruitPlumLeavesProperties);

            generateSeed();
            //setupStandardSeedDropping();

            setFlowerSeasonHold(fruitingOffset - 0.5f, fruitingOffset + 0.5f);

            ModContent.plumLeaves.setSpecies(this);
            addGenFeature(new FeatureGenFruitLeaves(6, 8, ModContent.plumLeavesProperties.getDynamicLeavesState(), ModContent.fruitPlumLeavesProperties[0].getDynamicLeavesState(), 0.5f).setFruitingRadius(3));

            ModContent.plumFruit.setSpecies(this);
            addGenFeature(new FeatureGenFruit(ModContent.plumFruit).setRayDistance(2).setFruitingRadius(3));
        }

        @Override
        public int colorTreeQuads(int defaultColor, ModelEntityFallingTree.TreeQuadData treeQuad, @Nullable EntityFallingTree entity) {
            if (treeQuad.bakedQuad.getTintIndex() == 1){
                IBlockState state = treeQuad.state;
                if (state.getBlock() instanceof BlockDynamicLeavesFruit)
                    return ((BlockDynamicLeavesFruit) state.getBlock()).fruitColor(state);
            }
            return defaultColor;
        }

        @Override
        public float seasonalFruitProductionFactor(World world, BlockPos pos) {
            float offset = fruitingOffset;
            return SeasonHelper.globalSeasonalFruitProductionFactor(world, pos, offset);
        }

        @Override
        public boolean testFlowerSeasonHold(World world, BlockPos pos, float seasonValue) {
            return SeasonHelper.isSeasonBetween(seasonValue, flowerSeasonHoldMin, flowerSeasonHoldMax);
        }
    }

    public TreePlum() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PLUM));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.plumLeavesProperties.setTree(this);
        for (int i=0;i<4;i++) ModContent.fruitPlumLeavesProperties[i].setTree(this);

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
        return 0x985964;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesPlum(this));
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchBasic(this.getName() + "branch"){
            @Override
            public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
                int radius = getRadius(blockState);
                return EnumForestryWoodType.PLUM.getHardness() * (radius * radius) / 64.0f * 8.0f;
            }
        };
    }
}