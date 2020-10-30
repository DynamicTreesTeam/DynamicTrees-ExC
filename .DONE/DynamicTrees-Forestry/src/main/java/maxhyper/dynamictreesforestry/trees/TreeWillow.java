package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeWillow extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.1"));
    public static int leavesMeta = 14;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.3"));
    public static int logMeta = 0;

    public class SpeciesWillow extends Species {

        SpeciesWillow(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.willowLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.6f, 10.0f, 1, 4, 1f);

            generateSeed();
            setupStandardSeedDropping();

            addGenFeature(new FeatureGenVine().setQuantity(16).setMaxLength(16));
        }
    }

    public static BlockDynamicLeaves willowLeaves;

    public static int leavesHeight = 5;
    public static int leavesHeightChance = 4;

    public TreeWillow() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.WILLOW));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        willowLeaves = new BlockDynamicLeaves(){
            @Override
            public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean unknown) {
            }

            @Override
            public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
                if (!(world.getBlockState(pos.up()).getBlock() == state.getBlock() && world.getBlockState(pos.up()).getValue(HYDRO) == 1)){
                    super.updateTick(world, pos, state, rand);
                }
                if (state.getValue(HYDRO) == 1 && world.getBlockState(pos.down()).getBlock() == Blocks.AIR &&
                rand.nextInt(leavesHeightChance) == 0 && world.getBlockState(pos.up(leavesHeight)).getBlock() != state.getBlock()){
                    world.setBlockState(pos.down(), state);
                }
            }

            @Override
            public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
                //super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

                if (entityIn.motionY > 0 && entityIn.motionY < 0.25D) {
                    entityIn.motionY += 0.025;//Allow a little climbing
                }
                entityIn.setSprinting(false);
                entityIn.motionX *= 0.4D;
                entityIn.motionZ *= 0.4D;
            }
        };
        willowLeaves.setRegistryName("leaves_willow");

        ModContent.willowLeavesProperties.setTree(this);
        ModContent.willowLeavesProperties.setDynamicLeavesState(willowLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
        willowLeaves.setProperties(0, ModContent.willowLeavesProperties);

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
        return 0x9a9b5b;
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList.add(willowLeaves);
        return super.getRegisterableBlocks( blockList);
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesWillow(this));
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchBasic(this.getName() + "branch"){
            @Override
            public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
                int radius = getRadius(blockState);
                return EnumForestryWoodType.WILLOW.getHardness() * (radius * radius) / 64.0f * 8.0f;
            }
        };
    }
}