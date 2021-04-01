package maxhyper.dynamictreesdefiledlands.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import maxhyper.dynamictreesdefiledlands.DynamicTreesDefiledLands;
import maxhyper.dynamictreesdefiledlands.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.Random;

public class TreeTenebra extends TreeFamily {

    public static Block logBlock = lykrast.defiledlands.common.init.ModBlocks.tenebraLog;

    public class SpeciesTenebra extends Species {

        SpeciesTenebra(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.dyingLeavesProperties);

            setSoilLongevity(3);

            setBasicGrowingParameters(0.3f, 16.0f, 8, lowestBranchHeight, 0.65f);

            generateSeed();

            setupStandardSeedDropping();
            setRequiresTileEntity(true);

            ModContent.dyingLeavesProperties.setTree(treeFamily);
        }

        @Override
        protected void setStandardSoils() {
            addAcceptableSoils(DirtHelper.DIRTLIKE, ModContent.CORRUPTDIRTLIKE);
        }

        @Override
        public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
            if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(ModContent.CORRUPTDIRTLIKE))){
                return ModContent.rootyDefiledDirt;
            } else
                return super.getRootyBlock(world,rootPos);
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
            for (EnumFacing dir : EnumFacing.HORIZONTALS){
                probMap[dir.ordinal()] *= 3;
            }
            //Branching is prevented all together, only allowing radius 1 branches to expand
            if ((radius > 1)){
                for (EnumFacing dir : EnumFacing.values()){
                    if (!TreeHelper.isBranch(world.getBlockState(pos.offset(dir))) || (TreeHelper.getRadius(world, pos.offset(dir)) <= 1 && TreeHelper.getRadius(world, pos) > 2)){
                        //minuscule chance that it branches out anyways, but if it does its short lived
                        if (world.rand.nextFloat() < 0.9f){
                            probMap[dir.ordinal()] = 0;
                        } else {
                            signal.energy = 4;
                        }
                    }
                }
            }

            probMap[signal.dir.getOpposite().ordinal()] = 0;
            probMap[EnumFacing.DOWN.ordinal()] = 0;

            return probMap;
        }
    }

    public TreeTenebra() {
        super(new ResourceLocation(DynamicTreesDefiledLands.MODID, "tenebra"));

        setPrimitiveLog(logBlock.getDefaultState());

        ModContent.dyingLeavesProperties.setTree(this);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(logBlock, 1, 0);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesTenebra(this));
    }
}
