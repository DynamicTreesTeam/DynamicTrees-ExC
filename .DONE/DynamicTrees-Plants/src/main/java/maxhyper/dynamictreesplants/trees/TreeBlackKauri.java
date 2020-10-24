package maxhyper.dynamictreesplants.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import maxhyper.dynamictreesplants.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import shadows.plants2.data.enums.TheBigBookOfEnums;
import shadows.plants2.init.ModRegistry;

import java.util.List;
import java.util.Objects;

public class TreeBlackKauri extends TreeFamily {

    public static Block leavesBlock = ModRegistry.LEAF_0;
    public static Block logBlock = ModRegistry.LOG_0;
    public static Block saplingBlock = ModRegistry.SAP_0;
    public static int meta = 0;

    public class SpeciesBlackKauri extends Species {

        SpeciesBlackKauri(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.blackKauriLeavesProperties);

            setBasicGrowingParameters(0.8f, 8.0f, 5, 2, 1.2f);

            generateSeed();

            setupStandardSeedDropping();
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
                if (signal.energy > 1){ //Flaring at end points, higher min energy means more flaring
                    probMap[EnumFacing.DOWN.getIndex()] = 0;
                    for (EnumFacing dir: EnumFacing.HORIZONTALS){
                        probMap[dir.getIndex()] = 0;
                    }
                }
                boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot)).getBlock() instanceof BlockBranch;
                boolean isBranchSide = world.getBlockState(pos.up()).getBlock() instanceof BlockBranch;
                probMap[EnumFacing.UP.getIndex()] = isBranchUp && !isBranchSide? 0:1;
                probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:1;
            }

            probMap[originDir.getIndex()] = 0;

            return probMap;
        }

    }

    public TreeBlackKauri() {
        super(new ResourceLocation(DynamicTreesPlants.MODID, "blackKauri"));

        setPrimitiveLog(logBlock.getDefaultState().withProperty(PropertyEnum.create("type", TheBigBookOfEnums.Logs.class), TheBigBookOfEnums.Logs.BLACK_KAURI));

        ModContent.blackKauriLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesBlackKauri(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        return super.getRegisterableItems(itemList);
    }

}