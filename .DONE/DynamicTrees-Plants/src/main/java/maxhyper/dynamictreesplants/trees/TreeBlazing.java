package maxhyper.dynamictreesplants.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import maxhyper.dynamictreesplants.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import shadows.plants2.data.enums.TheBigBookOfEnums;
import shadows.plants2.init.ModRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeBlazing extends TreeFamily {

    public static Block leavesBlock = ModRegistry.NETHER_LEAF;
    public static Block logBlock = ModRegistry.NETHER_LOG;
    public static Block saplingBlock = ModRegistry.NETHER_SAP;
    public static int meta = 1;

    public class SpeciesBlazing extends Species {

        SpeciesBlazing(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.blazingLeavesProperties);

            setBasicGrowingParameters(0.8f, 7, upProbability, 0, growthRate);

            generateSeed();

            setupStandardSeedDropping();
        }

        @Override
        protected void setStandardSoils() {
            addAcceptableSoils(DirtHelper.NETHERLIKE);
        }

        @Override
        public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
            return ModContent.rootyNetherDirt;
        }

        private EnumFacing getRelativeFace (BlockPos signalPos, BlockPos rootPos){
            if (signalPos.getZ() < rootPos.getZ()){
                return EnumFacing.NORTH;
            } else if (signalPos.getZ() > rootPos.getZ()){
                return EnumFacing.SOUTH;
            }else if (signalPos.getX() > rootPos.getX()){
                return EnumFacing.EAST;
            }else if (signalPos.getX() < rootPos.getX()){
                return EnumFacing.WEST;
            }else {
                return EnumFacing.UP;
            }
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
            EnumFacing originDir = signal.dir.getOpposite();

            if (signal.isInTrunk() && pos.getY() > signal.rootPos.getY() + getLowestBranchHeight() +1){
                probMap[EnumFacing.UP.getIndex()] = 0;
            }
            if (!signal.isInTrunk()){
                EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);
                //if (signal.energy > 1){ //Flaring at end points, higher min energy means more flaring
                    probMap[EnumFacing.DOWN.getIndex()] = 0;
                    for (EnumFacing dir: EnumFacing.HORIZONTALS){
                        probMap[dir.getIndex()] = 0;
                    }
                //}
                boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot)).getBlock() instanceof BlockBranch;
                boolean isBranchSide = world.getBlockState(pos.up()).getBlock() instanceof BlockBranch;
                probMap[EnumFacing.UP.getIndex()] = isBranchUp && !isBranchSide? 0:2;
                probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:1;
            }

            probMap[originDir.getIndex()] = 0;

            return probMap;
        }
    }

    public TreeBlazing() {
        super(new ResourceLocation(DynamicTreesPlants.MODID, "blazing"));

        setPrimitiveLog(logBlock.getDefaultState().withProperty(PropertyEnum.create("type", TheBigBookOfEnums.NetherLogs.class), TheBigBookOfEnums.NetherLogs.BLAZE));

        ModContent.blazingLeavesProperties.setTree(this);

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, meta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesBlazing(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        return super.getRegisterableItems(itemList);
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchBasic(this.getName()+"branch"){
            @Override
            public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
                return 0;
            }

            @Override
            public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
                return 0;
            }
        };
    }

}