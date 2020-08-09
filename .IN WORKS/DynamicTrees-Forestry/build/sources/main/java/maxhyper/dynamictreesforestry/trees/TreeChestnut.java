package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockBranchThickForestry;
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

public class TreeChestnut extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.0"));
    public static int leavesMeta = 5;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.1"));
    public static int logMeta = 0;

    public class SpeciesChestnut extends Species {

        SpeciesChestnut(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.chestnutLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.6f, 12.0f, 1, 4, 0.9f);

            generateSeed();
            //setupStandardSeedDropping();

            addGenFeature(new FeatureGenFruitLeaves(10, 16, ModContent.chestnutLeavesProperties.getDynamicLeavesState(), ModContent.fruitChestnutLeavesProperties.getDynamicLeavesState(), 0.1f));
            addGenFeature(new FeatureGenFlareBottom());

            addGenFeature(new FeatureGenFruit(ModContent.chestnutFruit).setRayDistance(4));
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

    public TreeChestnut() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, "chestnut"));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.chestnutLeavesProperties.setTree(this);
        ModContent.fruitChestnutLeavesProperties.setTree(this);

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
        return 0xa09d56;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesChestnut(this));
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.CHESTNUT);
    }

}