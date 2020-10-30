package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockBranchThickForestry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeBaobab extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.1"));
    public static int leavesMeta = 12;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.1"));
    public static int logMeta = 2;

    public class SpeciesBaobab extends Species {

        SpeciesBaobab(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.baobabLeavesProperties);

            setBasicGrowingParameters(1.5f, 30.0f, 20, 20, 0.4f);

            setSoilLongevity(40);

            generateSeed();

            setupStandardSeedDropping();

            addGenFeature(new FeatureGenMound(999));
            addGenFeature(new FeatureGenVine());
        }

        @Override
        public int maxBranchRadius() {
            return 24;
        }

        @Override
        public int getLowestBranchHeight(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return (int)(super.getLowestBranchHeight(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 4)); // Vary the height energy by a psuedorandom hash function
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 4); // Vary the height energy by a psuedorandom hash function
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
                probMap[EnumFacing.UP.getIndex()] = isBranchUp && !isBranchSide? 0:2;
                probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:3;
            }

            probMap[originDir.getIndex()] = 0;

            return probMap;
        }

        @Override
        public boolean isThick() {
            return true;
        }
    }

    public TreeBaobab() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.BAOBAB));

        //setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logMeta));

        ModContent.baobabLeavesProperties.setTree(this);

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }
    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, logMeta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public int getRootColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return 0x8e965a;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesBaobab(this));
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

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.BAOBAB);
    }
}