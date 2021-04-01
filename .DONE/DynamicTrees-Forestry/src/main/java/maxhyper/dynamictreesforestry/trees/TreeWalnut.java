package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.models.ModelEntityFallingTree;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockBranchThickForestry;
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

public class TreeWalnut extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.0"));
    public static int leavesMeta = 4;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.3"));
    public static int logMeta = 1;

    public static float fruitingOffset = 0.5f; //summer-autumn

    public class SpeciesWalnut extends Species {

        SpeciesWalnut(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.walnutLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.6f, 12.0f, 1, 4, 0.9f);

            addValidLeavesBlocks(ModContent.fruitWalnutLeavesProperties);

            generateSeed();
            //setupStandardSeedDropping();

            setFlowerSeasonHold(fruitingOffset - 0.5f, fruitingOffset + 0.5f);

            ModContent.walnutLeaves.setSpecies(this);
            addGenFeature(new FeatureGenFruitLeaves(10, 16, ModContent.walnutLeavesProperties.getDynamicLeavesState(), ModContent.fruitWalnutLeavesProperties[0].getDynamicLeavesState(), 0.5f).setfruitAttempts(20));
            addGenFeature(new FeatureGenFlareBottom());

            ModContent.walnutFruit.setSpecies(this);
            addGenFeature(new FeatureGenFruit(ModContent.walnutFruit).setRayDistance(4));
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

        @Override
        public int maxBranchRadius() {
            return 15;
        }

        @Override
        public boolean isThick() {
            return true;
        }

    }

    public TreeWalnut() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.WALNUT));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.walnutLeavesProperties.setTree(this);
        for (int i=0;i<4;i++) ModContent.fruitWalnutLeavesProperties[i].setTree(this);

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
        return 0x685952;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesWalnut(this));
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.WALNUT);
    }
}