package maxhyper.dynamictreesmysticalworld.trees;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import epicsquid.mysticalworld.init.ModBlocks;
import maxhyper.dynamictreesmysticalworld.DynamicTreesMysticalWorld;
import maxhyper.dynamictreesmysticalworld.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class TreeCharred extends TreeFamily {

    public static Block logBlock = ModBlocks.charred_log;

    public class SpeciesCharred extends Species {

        SpeciesCharred(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.nullLeavesProperties);

            setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.5f);

        }

        @Override
        public boolean grow(World world, BlockRooty rootyDirt, BlockPos rootPos, int soilLife, ITreePart treeBase, BlockPos treePos, Random random, boolean natural) {
            if (ModConfigs.worldGenDebug) {
                return super.grow(world,rootyDirt,rootPos,soilLife,treeBase,treePos,random,natural);
            } else return false;
        }

        @Override
        public boolean canBoneMeal() {
            return ModConfigs.worldGenDebug;
        }

        @Override
        public boolean canGrowWithBoneMeal(World world, BlockPos pos) {
            return ModConfigs.worldGenDebug;
        }

        @Override
        public boolean canUseBoneMealNow(World world, Random rand, BlockPos pos) {
            return ModConfigs.worldGenDebug;
        }

        @Override
        public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
            return false;
        }

    }
    public TreeCharred() {
        super(new ResourceLocation(DynamicTreesMysticalWorld.MODID, "charred"));

        setPrimitiveLog(logBlock.getDefaultState());

        ModContent.nullLeavesProperties.setTree(this);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(logBlock, 1, 0);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesCharred(this));
    }
}
