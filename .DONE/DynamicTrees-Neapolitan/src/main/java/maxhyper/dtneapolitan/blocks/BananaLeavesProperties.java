package maxhyper.dtneapolitan.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.PalmLeavesProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BananaLeavesProperties extends PalmLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(BananaLeavesProperties::new);

    public BananaLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public AbstractBlock.Properties getDefaultBlockProperties(Material material, MaterialColor materialColor) {
        return AbstractBlock.Properties.of(Material.PLANT, MaterialColor.COLOR_LIGHT_GREEN).strength(0.2F).sound(SoundType.WEEPING_VINES)
                .harvestTool(ToolType.HOE).noOcclusion().isSuffocating((s, r, p) -> false).isViewBlocking((s, r, p) -> false).randomTicks()
                .isValidSpawn((s, r, p, e) -> e == EntityType.OCELOT || e == EntityType.PARROT);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(AbstractBlock.Properties properties) {
        return new DynamicPalmLeavesBlock(this, properties){
            //When destroying, we update the fruit below if one is there.
            private void updateFruit (IWorld world, BlockPos pos, int offset){
                BlockState downState = world.getBlockState(pos.below(offset));
                if (downState.getBlock() instanceof BananaFruitBlock){
                    downState.onNeighborChange(world, pos.below(offset), pos.below(offset).above());
                }
            }
            @Override
            public void destroy(IWorld world, BlockPos pos, BlockState state) {
                updateFruit(world, pos, 2);
                super.destroy(world, pos, state);
            }
            @Override
            public void onPlace(BlockState thisState, World world, BlockPos pos, BlockState oldState, boolean bool) {
                updateFruit(world, pos, 2);
                updateFruit(world, pos, 3);
                super.onPlace(thisState, world, pos, oldState, bool);
            }

            @Deprecated
            public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader reader, BlockPos pos) {
                float f = state.getDestroySpeed(reader, pos);
                if (f == -1.0F) {
                    return 0.0F;
                } else {
                    int i = net.minecraftforge.common.ForgeHooks.canHarvestBlock(state, player, reader, pos) ? 30 : 100;
                    return player.getDigSpeed(state, pos) / f / (float)i;
                }
            }
        };
    }

}
