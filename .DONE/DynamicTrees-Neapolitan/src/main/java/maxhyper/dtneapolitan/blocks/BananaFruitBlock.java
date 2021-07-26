package maxhyper.dtneapolitan.blocks;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.minecraftabnormals.neapolitan.common.entity.PlantainSpiderEntity;
import com.minecraftabnormals.neapolitan.core.NeapolitanConfig;
import com.minecraftabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.minecraftabnormals.neapolitan.core.registry.NeapolitanEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BananaFruitBlock extends FruitBlock {

    public boolean shouldBlockDrop(IBlockReader world, BlockPos pos, BlockState state) {
        return !(world.getBlockState(pos.above(2)).getBlock() instanceof LeavesBlock);
    }

    @Override
    protected int fruitDropCount(BlockState state, World world, BlockPos pos) {
        return 2 + world.getRandom().nextInt(3);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return VoxelShapes.empty();
    }

    private boolean playerHasSilkTouch (Entity entity){
        final PlayerEntity player = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
        if (player != null){
            final ItemStack stack = player.getMainHandItem();
            return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
        }
        return false;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        final Entity entity = builder.getOptionalParameter(LootParameters.THIS_ENTITY);
        // If the tool has silk touch, drop the block
        if (playerHasSilkTouch(entity))
            return new ArrayList<>(Collections.singletonList(new ItemStack(NeapolitanBlocks.BANANA_BUNDLE.get().asItem())));
        return super.getDrops(state, builder);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ActionResultType result = super.use(state, worldIn, pos, player, handIn, hit);
        if (result == ActionResultType.SUCCESS)
            spawnSpiderEntity(worldIn, pos, player);
        return result;
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        spawnSpiderEntity(world, pos, player);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        if (this.shouldBlockDrop(world, pos, state)) {
            spawnSpiderEntity((World)world, pos, null);
        }
        super.onNeighborChange(state, world, pos, neighbor);
    }

    private void spawnSpiderEntity(World world, BlockPos pos, PlayerEntity player) {
        if (!playerHasSilkTouch(player) && world.getRandom().nextFloat() <= 0.05F && NeapolitanConfig.COMMON.plantainSpidersFromBundles.get()) {
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
