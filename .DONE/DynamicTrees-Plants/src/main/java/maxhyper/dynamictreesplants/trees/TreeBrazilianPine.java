package maxhyper.dynamictreesplants.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
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

public class TreeBrazilianPine extends TreeFamily {

    public static Block leavesBlock = ModRegistry.LEAF_0;
    public static Block logBlock = ModRegistry.LOG_0;
    public static Block saplingBlock = ModRegistry.SAP_0;
    public static int meta = 1;

    public class SpeciesBrazilianPine extends Species {

        SpeciesBrazilianPine(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.brazilianPineLeavesProperties);

            setBasicGrowingParameters(0.3f, 26.0f, 20, 12, 1f);

            generateSeed();

            setupStandardSeedDropping();

            addGenFeature(new FeatureGenMound(999));
            addGenFeature(new FeatureGenVine());
        }

        @Override
        public int maxBranchRadius() {
            return 16;
        }

        @Override
        public int getLowestBranchHeight(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return (int)(super.getLowestBranchHeight(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3)); // Vary the height energy by a psuedorandom hash function
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3); // Vary the height energy by a psuedorandom hash function
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

            if (!signal.isInTrunk()){
                EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);
                if (signal.energy > 2){ //Flaring at end points, higher min energy means more flaring
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

        @Override
        public boolean isThick() {
            return true;
        }
    }

    public TreeBrazilianPine() {
        super(new ResourceLocation(DynamicTreesPlants.MODID, "brazilianPine"));

        setPrimitiveLog(logBlock.getDefaultState().withProperty(PropertyEnum.create("type", TheBigBookOfEnums.Logs.class), TheBigBookOfEnums.Logs.BRAZILLIAN_PINE));

        ModContent.brazilianPineLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesBrazilianPine(this));
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
    public boolean isThick() {
        return true;
    }

}