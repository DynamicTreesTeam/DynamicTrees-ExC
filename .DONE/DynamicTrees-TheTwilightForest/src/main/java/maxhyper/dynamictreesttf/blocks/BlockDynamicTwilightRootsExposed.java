package maxhyper.dynamictreesttf.blocks;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import maxhyper.dynamictreesttf.ModContent;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockDynamicTwilightRootsExposed extends BlockDynamicTwilightRoots {

    public static final IUnlistedProperty[] CONNECTIONS = {
            new Properties.PropertyAdapter<>(PropertyInteger.create("radiusd", 0, 8)),
            new Properties.PropertyAdapter<>(PropertyInteger.create("radiusu", 0, 8)),
            new Properties.PropertyAdapter<>(PropertyInteger.create("radiusn", 0, 8)),
            new Properties.PropertyAdapter<>(PropertyInteger.create("radiuss", 0, 8)),
            new Properties.PropertyAdapter<>(PropertyInteger.create("radiusw", 0, 8)),
            new Properties.PropertyAdapter<>(PropertyInteger.create("radiuse", 0, 8))
    };

    public BlockDynamicTwilightRootsExposed() {
        setDefaultState(this.getDefaultState().withProperty(RADIUS, 1).withProperty(GRASSY, false));
        setHardness(2.5f);
        setTickRandomly(false);
        setHarvestLevel("axe", 0);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) { return SoundType.WOOD;}

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) { }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {}

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) { }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        if (state.getValue(RADIUS) >= 6){
            if (state.getValue(GRASSY)){
                return new ItemStack(Item.getItemFromBlock(TFBlocks.root), 1, 1);
            } else {
                return new ItemStack(Item.getItemFromBlock(TFBlocks.root), 1, 0);
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        NonNullList<ItemStack> ret = NonNullList.create();
        if (state.getValue(GRASSY)){
            ret.add(new ItemStack(TFItems.liveroot, 1, 0));
        } else {
            ret.add(new ItemStack(Items.STICK, (int)Math.ceil(state.getValue(RADIUS)/2f), 0));
        }
        return ret;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(TFBlocks.root, 1, state.getValue(GRASSY)?1:0);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handStack = playerIn.getHeldItemMainhand();
        if (handStack.getItem() == Item.getItemFromBlock(Blocks.DIRT)){
            int thisRadius = state.getValue(RADIUS);
            worldIn.setBlockState(pos, ModContent.undergroundRoot.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, thisRadius ));
            if (!playerIn.isCreative()) handStack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
            playerIn.swingArm(EnumHand.MAIN_HAND);
            return true;
        } else if (handStack.getItem() == Item.getItemFromBlock(Blocks.GRASS)){
            int thisRadius = state.getValue(RADIUS);
            worldIn.setBlockState(pos, ModContent.undergroundRoot.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, thisRadius ).withProperty(GRASSY, true));
            if (!playerIn.isCreative()) handStack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
            playerIn.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    public int getRadius(IBlockState blockState) {
        return blockState.getBlock() == this ? blockState.getValue(RADIUS) : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = { RADIUS, GRASSY };
        return new ExtendedBlockState(this, listedProperties, CONNECTIONS);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState retval = (IExtendedBlockState) state;
            int thisRadius = getRadius(state);
            for (EnumFacing dir : EnumFacing.VALUES) {
                retval = retval.withProperty(CONNECTIONS[dir.getIndex()], getSideConnectionRadius(world, pos, thisRadius, dir));
            }
            return retval;
        }
        return state;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(RADIUS) == 8;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,	EnumFacing side) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;//This prevents fences and walls from attempting to connect to branches.
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return getRadius(state) == 8;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return getBlockLayer() == layer;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {

        if (state.getBlock() != this) {
            return NULL_AABB;
        }

        int thisRadius = getRadius(state);

        boolean connectionMade = false;
        double radius = thisRadius / 16.0;
        double gap = 0.5 - radius;
        AxisAlignedBB aabb = new AxisAlignedBB(0, 0, 0, 0, 0, 0).grow(radius);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (getSideConnectionRadius(blockAccess, pos, thisRadius, dir) > 0) {
                connectionMade = true;
                aabb = aabb.expand(dir.getFrontOffsetX() * gap, dir.getFrontOffsetY() * gap, dir.getFrontOffsetZ() * gap);
            }
        }
        if (connectionMade) {
            return aabb.offset(0.5, 0.5, 0.5);
        }

        return new AxisAlignedBB(0.5 - radius, 0.5 - radius, 0.5 - radius, 0.5 + radius, 0.5 + radius, 0.5 + radius);
    }

    public int getRadiusForConnection(IBlockState blockState, IBlockAccess world, BlockPos pos, BlockBranch from, EnumFacing side, int fromRadius) {
        return getRadius(blockState);
    }

    protected int getSideConnectionRadius(IBlockAccess blockAccess, BlockPos pos, int radius, EnumFacing side) {
        BlockPos deltaPos = pos.offset(side);
        IBlockState blockState = blockAccess.getBlockState(deltaPos);
        if (blockState.getProperties().containsKey(RADIUS)){
            return blockState.getValue(RADIUS);
        } else if (blockState.getBlock() == ModBlocks.blockRootyDirt && blockAccess.getBlockState(pos).getProperties().containsKey(RADIUS)){
            return blockAccess.getBlockState(pos).getValue(RADIUS);
        }
        else return 0;
    }

}
