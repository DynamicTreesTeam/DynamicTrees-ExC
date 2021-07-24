package maxhyper.dtneapolitan.blocks;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.minecraftabnormals.neapolitan.common.entity.PlantainSpiderEntity;
import com.minecraftabnormals.neapolitan.core.NeapolitanConfig;
import com.minecraftabnormals.neapolitan.core.registry.NeapolitanEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import static com.ferreusveritas.dynamictrees.util.ShapeUtils.createFruitShape;

public class BananaFruitBlock extends FruitBlock {

    public boolean shouldBlockDrop(IBlockReader world, BlockPos pos, BlockState state) {
        return !(world.getBlockState(pos.above(2)).getBlock() instanceof LeavesBlock);
    }

    @Override
    protected int fruitDropCount(BlockState state, World world, BlockPos pos) {
        return 2 + world.getRandom().nextInt(3);
    }

    protected AxisAlignedBB[] BANANA_AABB = new AxisAlignedBB[] {
            createFruitShape(1,1,0, 16),
            createFruitShape(2.5f,10,0,20),
            createFruitShape(7f,20,0,20),
            createFruitShape(7f,13,0,20)
    };

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.create(BANANA_AABB[state.getValue(AGE)]);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return VoxelShapes.empty();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (state.getValue(AGE) >= 3) {
            this.dropFruit(world, state, pos, player);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    private void dropFruit(World world, BlockState state, BlockPos pos, PlayerEntity player) {
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        if (state.getValue(AGE) >= 3) {
            world.addFreshEntity(new ItemEntity(world, pos.getX() + itemSpawnOffset.x, pos.getY() + itemSpawnOffset.y, pos.getZ() + itemSpawnOffset.z, this.getFruitDrop(fruitDropCount(state, world, pos))));
        }

        if (world.getRandom().nextFloat() <= 0.05F && NeapolitanConfig.COMMON.plantainSpidersFromBundles.get()) {
            PlantainSpiderEntity spider = NeapolitanEntities.PLANTAIN_SPIDER.get().create(world);
            if (spider != null){
                spider.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
                spider.setLastHurtByMob(player);
                world.addFreshEntity(spider);
                if (world.getRandom().nextFloat() <= 0.25F)
                    world.addFreshEntity(spider);
                if (world.getRandom().nextFloat() <= 0.45F)
                    world.addFreshEntity(spider);
            }
        }
    }
}
