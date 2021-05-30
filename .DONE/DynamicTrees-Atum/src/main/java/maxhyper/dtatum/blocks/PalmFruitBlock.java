package maxhyper.dtatum.blocks;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import static com.ferreusveritas.dynamictrees.util.ShapeUtils.createFruitShape;

public class PalmFruitBlock extends FruitBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    protected final float[] DATE_OFFSET = {6 /16f, 6 /16f, 6 /16f, 6 /16f};

    protected void createBlockStateDefinition(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    public BlockState getStateForAgeAndDir(int age, Direction direction) {
        return getStateForAge(age).setValue(FACING, direction);
    }

    @Override
    protected int fruitDropCount(BlockState state, World world, BlockPos pos) {
        return 2 + world.getRandom().nextInt(4);
    }

    @Override
    public boolean shouldBlockDrop(IBlockReader world, BlockPos pos, BlockState state) {
        Direction dir = state.getValue(FACING);
        BlockState offsetState = world.getBlockState(pos.offset(dir.getNormal()));
        BlockState offsetUpState = world.getBlockState(pos.offset(dir.getNormal()).above());
        return !(offsetState.getBlock() instanceof BranchBlock && offsetUpState.getBlock() instanceof DynamicLeavesBlock);
    }

    protected AxisAlignedBB[] DATE_AABB = new AxisAlignedBB[] {
            createFruitShape(1,1,1, 16),
            createFruitShape(3,8,7, 16),
            createFruitShape(3.5f,10,6, 16),
            createFruitShape(3.5f,10,6, 16)
    };

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.getValue(FACING);
        return VoxelShapes.create(offsetBoundingBox(DATE_AABB[state.getValue(AGE)], direction, DATE_OFFSET[state.getValue(AGE)]));
    }

    protected AxisAlignedBB offsetBoundingBox (AxisAlignedBB box, Direction dir, float offset){
        return box.move(dir.getStepX() * offset, dir.getStepY() * offset, dir.getStepZ() * offset);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return VoxelShapes.empty();
    }
}