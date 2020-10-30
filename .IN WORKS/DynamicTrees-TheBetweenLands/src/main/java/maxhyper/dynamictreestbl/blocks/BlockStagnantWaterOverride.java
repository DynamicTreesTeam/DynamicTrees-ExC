package maxhyper.dynamictreestbl.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockRootyWater;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.capability.IDecayCapability;
import thebetweenlands.common.block.terrain.BlockStagnantWater;
import thebetweenlands.common.herblore.elixir.ElixirEffectRegistry;
import thebetweenlands.common.registries.CapabilityRegistry;
import thebetweenlands.util.AdvancedStateMap;

import javax.annotation.Nonnull;

public class BlockStagnantWaterOverride extends BlockStagnantWater {

    public BlockStagnantWaterOverride(ResourceLocation name) {
        setRegistryName(name);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn instanceof EntityPlayer && !worldIn.isRemote && !((EntityPlayer)entityIn).isPotionActive(ElixirEffectRegistry.EFFECT_DECAY.getPotionEffect())) {
            ((EntityPlayer)entityIn).addPotionEffect(ElixirEffectRegistry.EFFECT_DECAY.createEffect(60, 3));
        }
        if(!worldIn.isRemote ) {
            IDecayCapability cap = entityIn.getCapability(CapabilityRegistry.CAPABILITY_DECAY, null);
            if (cap != null) {
                cap.getDecayStats().addDecayAcceleration(0.1F);
            }
        }
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().isLiquid() && super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        return !world.getBlockState(pos).getMaterial().isLiquid() && super.displaceIfPossible(world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setStateMapper(AdvancedStateMap.Builder builder) {
        builder.ignore(BlockStagnantWaterOverride.LEVEL);
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState state, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
        if(materialIn == Material.WATER) {
            double liquidHeight = (float)(blockpos.getY() + 1) - BlockLiquid.getLiquidHeightPercent(state.getValue(BlockLiquid.LEVEL));
            if(testingHead) {
                double liquidHeightBelow = 0;
                if(world.getBlockState(blockpos.up()).getBlock() == state.getBlock()) {
                    liquidHeightBelow = (float)(blockpos.getY() + 2) - BlockLiquid.getLiquidHeightPercent(world.getBlockState(blockpos.up()).getValue(BlockLiquid.LEVEL));
                }
                return entity.posY + entity.getEyeHeight() < 0.1D + liquidHeight || entity.posY + entity.getEyeHeight() < 0.1D + liquidHeightBelow;
            } else {
                return entity.getEntityBoundingBox().maxY >= liquidHeight && entity.getEntityBoundingBox().minY < liquidHeight;
            }
        }
        return null;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return 100;
    }

    private static boolean isFluid(@Nonnull IBlockState blockstate)
    {
        return blockstate.getMaterial().isLiquid() || blockstate.getBlock() instanceof IFluidBlock || blockstate.getBlock() instanceof BlockRootyWater;
    }

    public float getFluidHeightForRender(IBlockAccess world, BlockPos pos, @Nonnull IBlockState up)
    {
        IBlockState here = world.getBlockState(pos);
        if (here.getBlock() == this)
        {
            if (isFluid(up))
            {
                return 1;
            }

            if (getMetaFromState(here) == getMaxRenderHeightMeta())
            {
                return quantaFraction;
            }
        }
        if (here.getBlock() instanceof BlockRootyWater)
        {
            if (isFluid(up)) {
                return 1;
            } else {
                return quantaFraction;
            }
        }
        if (here.getBlock() instanceof BlockLiquid)
        {
            return Math.min(1 - BlockLiquid.getLiquidHeightPercent(here.getValue(BlockLiquid.LEVEL)), quantaFraction);
        }
        return !here.getMaterial().isSolid() && up.getBlock() == this ? 1 : this.getQuantaPercentage(world, pos) * quantaFraction;
    }

    @Override
    public int getQuantaValue(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().isAir(state, world, pos))
        {
            return 0;
        }

        if (state.getBlock() instanceof BlockRootyWater){
            return quantaPerBlock;
        }

        if (state.getBlock() != this)
        {
            return -1;
        }

        return quantaPerBlock - state.getValue(LEVEL);
    }

    public boolean isSourceBlock(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return (state.getBlock() == this && state.getValue(LEVEL) == 0) || state.getBlock() instanceof BlockRootyWater;
    }

    @Override
    public ItemBlock getItemBlock() {
        return null;
    }
}
