package maxhyper.dynamictreestbl.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockRootyWater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.render.particle.BLParticles;
import thebetweenlands.common.block.terrain.BlockSwampWater;
import thebetweenlands.common.item.armor.ItemMarshRunnerBoots;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.FluidRegistry;
import thebetweenlands.common.world.WorldProviderBetweenlands;
import thebetweenlands.util.AdvancedStateMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSwampWaterOverride extends BlockSwampWater {
    private boolean isUnderwaterBlock = false;

    public BlockSwampWaterOverride(ResourceLocation name) {
        super(FluidRegistry.SWAMP_WATER, Material.WATER);
        setRegistryName(name);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        if (world.isAirBlock(pos)) return true;

        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockSwampWater || state.getBlock() instanceof BlockRootyWater) {
            return false;
        }

        if (displacements.containsKey(state.getBlock())) {
            return displacements.get(state.getBlock());
        }

        Material material = state.getMaterial();
        if (material.blocksMovement() || material == Material.PORTAL) {
            return false;
        }

        int density = getDensity(world, pos);
        if (density == Integer.MAX_VALUE) {
            return true;
        }

        return this.density > density;
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        if (world == null || !world.isBlockLoaded(pos)) return false;

        if (world.isAirBlock(pos)) {
            return true;
        }

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BlockSwampWater || state.getBlock() instanceof BlockRootyWater) {
            return false;
        }

        if (displacements.containsKey(block)) {
            if (displacements.get(block)) {
                block.dropBlockAsItem(world, pos, state, 0);
                return true;
            }
            return false;
        }

        Material material = state.getMaterial();
        if (material.blocksMovement() || material == Material.PORTAL) {
            return false;
        }

        int density = getDensity(world, pos);
        if (density == Integer.MAX_VALUE) {
            block.dropBlockAsItem(world, pos, state, 0);
            return true;
        }

        return this.density > density;
    }

    private static boolean isFluid(@Nonnull IBlockState blockstate)
    {
        return (blockstate.getMaterial().isLiquid() || blockstate.getBlock() instanceof IFluidBlock) && (!(blockstate.getBlock() instanceof BlockSwampWaterOverride) || !((BlockSwampWaterOverride)blockstate.getBlock()).isUnderwaterBlock);
    }

    @Override
    public float getFluidHeightForRender(IBlockAccess world, BlockPos pos, @Nonnull IBlockState up) {
        IBlockState here = world.getBlockState(pos);
        if (here.getBlock() instanceof BlockSwampWater) {
            if (isFluid(up)) {
                return 1;
            }

            if (here.getValue(LEVEL) == getMaxRenderHeightMeta()) {
                return 0.875F;
            }
        }
        if (here.getBlock() instanceof BlockRootyWater)
        {
            if (isFluid(up)) {
                return 1;
            } else {
                return 0.875F;
            }
        }
        return !here.getMaterial().isSolid() && (up.getBlock() instanceof BlockSwampWater || up.getBlock() instanceof BlockRootyWater) ? 1 : this.getQuantaPercentage(world, pos) * 0.875F;
    }

    @Override
    public Vec3d getFlowVector(IBlockAccess world, BlockPos pos) {
        Vec3d vec = new Vec3d(0.0D, 0.0D, 0.0D);
        int decay = quantaPerBlock - getQuantaValue(world, pos);

        for (int side = 0; side < 4; ++side)
        {
            int x2 = pos.getX();
            int z2 = pos.getZ();

            switch (side)
            {
                case 0: --x2; break;
                case 1: --z2; break;
                case 2: ++x2; break;
                case 3: ++z2; break;
            }

            BlockPos pos2 = new BlockPos(x2, pos.getY(), z2);
            int otherDecay = quantaPerBlock - getQuantaValue(world, pos2);
            if (otherDecay >= quantaPerBlock)
            {
                if (!world.getBlockState(pos2).getMaterial().blocksMovement())
                {
                    otherDecay = quantaPerBlock - getQuantaValue(world, pos2.down());
                    if (otherDecay >= 0)
                    {
                        int power = otherDecay - (decay - quantaPerBlock);
                        vec = vec.add(new Vec3d((pos2.getX() - pos.getX()) * power, 0, (pos2.getZ() - pos.getZ()) * power));
                    }
                }
            }
            else if (otherDecay >= 0)
            {
                int power = otherDecay - decay;
                vec = vec.add(new Vec3d((pos2.getX() - pos.getX()) * power, 0, (pos2.getZ() - pos.getZ()) * power));
            }
        }

        if (!this.isSourceBlock(world, pos) && (world.getBlockState(pos.up()).getBlock() instanceof BlockSwampWater || world.getBlockState(pos.up()).getBlock() instanceof BlockRootyWater)) {
            for (EnumFacing dir : EnumFacing.Plane.HORIZONTAL) {
                if (this.causesDownwardCurrent(world, pos.offset(dir), dir) || this.causesDownwardCurrent(world, pos.offset(dir).up(), dir)) {
                    vec = vec.normalize().add(new Vec3d(0.0D, -6.0D, 0.0D));
                    break;
                }
            }
        }

        vec = vec.normalize();
        return vec;
    }

    @Override
    public int getQuantaValue(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BlockSwampWaterOverride && ((BlockSwampWaterOverride) state.getBlock()).isUnderwaterBlock) {
            return this.quantaPerBlock;
        }

        if (state.getBlock() == Blocks.AIR) {
            return 0;
        }

        if (state.getBlock() instanceof BlockRootyWater){
            return this.quantaPerBlock;
        }

        if (!(state.getBlock() instanceof BlockSwampWater)) {
            return -1;
        }

        return this.quantaPerBlock - state.getValue(LEVEL);
    }

    @Override
    public boolean isSourceBlock(IBlockAccess world, BlockPos pos) {
        return super.isSourceBlock(world, pos);
    }

    @Override
    protected boolean canFlowInto(IBlockAccess world, BlockPos pos) {
        if (world instanceof World && !((World) world).isBlockLoaded(pos)) return false;

        if (world.isAirBlock(pos)) return true;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockSwampWater || state.getBlock() instanceof BlockRootyWater) {
            return true;
        }

        if (displacements.containsKey(state.getBlock())) {
            return displacements.get(state.getBlock());
        }

        Material material = state.getMaterial();
        if (material.blocksMovement() ||
                material == Material.WATER ||
                material == Material.LAVA ||
                material == Material.PORTAL) {
            return false;
        }

        int density = getDensity(world, pos);
        if (density == Integer.MAX_VALUE) {
            return true;
        }

        return this.density > density;
    }

    @Override
    protected void flowIntoBlock(World world, BlockPos pos, int meta) {
        if (meta < 0) return;
        if (displaceIfPossible(world, pos)) {
            world.setBlockState(pos, BlockRegistry.SWAMP_WATER.getBlockState().getBaseState().withProperty(LEVEL, meta), 3);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int quantaRemaining = quantaPerBlock - state.getValue(LEVEL);

        //Replenishing source
        if (quantaRemaining < quantaPerBlock && !world.isAirBlock(pos.down())) {
            int adjacentSources = 0;
            if (this.isSourceBlock(world, pos.east())) adjacentSources++;
            if (this.isSourceBlock(world, pos.north())) adjacentSources++;
            if (this.isSourceBlock(world, pos.south())) adjacentSources++;
            if (this.isSourceBlock(world, pos.west())) adjacentSources++;
            if (adjacentSources >= 2) {
                world.setBlockState(pos, state.withProperty(LEVEL, 0), 2);
                quantaRemaining = quantaPerBlock;
            }
        }

        int expQuanta = -101;

        if (!(state.getBlock() instanceof BlockSwampWaterOverride && ((BlockSwampWaterOverride) state.getBlock()).isUnderwaterBlock)) {
            // check adjacent block levels if non-source
            if (quantaRemaining < quantaPerBlock) {
                if (world.getBlockState(pos.add(0, -densityDir, 0)).getBlock() == this ||
                        world.getBlockState(pos.add(-1, -densityDir, 0)).getBlock() == this ||
                        world.getBlockState(pos.add(1, -densityDir, 0)).getBlock() == this ||
                        world.getBlockState(pos.add(0, -densityDir, -1)).getBlock() == this ||
                        world.getBlockState(pos.add(0, -densityDir, 1)).getBlock() == this) {
                    expQuanta = quantaPerBlock - 1;
                } else {
                    int maxQuanta = -100;
                    maxQuanta = getLargerQuanta(world, pos.add(-1, 0, 0), maxQuanta);
                    maxQuanta = getLargerQuanta(world, pos.add(1, 0, 0), maxQuanta);
                    maxQuanta = getLargerQuanta(world, pos.add(0, 0, -1), maxQuanta);
                    maxQuanta = getLargerQuanta(world, pos.add(0, 0, 1), maxQuanta);

                    expQuanta = maxQuanta - 1;
                }

                // decay calculation
                if (expQuanta != quantaRemaining) {
                    quantaRemaining = expQuanta;

                    if (expQuanta <= 0) {
                        world.setBlockToAir(pos);
                    } else {
                        world.setBlockState(pos, state.withProperty(LEVEL, quantaPerBlock - expQuanta), 2);
                        world.scheduleUpdate(pos, this, tickRate);
                        world.notifyNeighborsOfStateChange(pos, this, true);
                    }
                }
            }
            // This is a "source" block, set meta to zero, and send a server only update
            else {
                world.setBlockState(pos, this.getDefaultState(), 2);
            }
        }

        // Flow vertically if possible
        if (canDisplace(world, pos.up(densityDir))) {
            flowIntoBlock(world, pos.up(densityDir), 1);
            return;
        }

        // Flow outward if possible
        int flowMeta = quantaPerBlock - quantaRemaining + 1;
        if (flowMeta >= quantaPerBlock) {
            return;
        }

        if (isSourceBlock(world, pos) || !isFlowingVertically(world, pos)) {
            if (world.getBlockState(pos.down(densityDir)).getBlock() instanceof BlockSwampWater || world.getBlockState(pos.down(densityDir)).getBlock() instanceof BlockRootyWater) {
                flowMeta = 1;
            }
            boolean[] flowTo = getOptimalFlowDirections(world, pos);

            if (flowTo[0]) flowIntoBlock(world, pos.add(-1, 0, 0), flowMeta);
            if (flowTo[1]) flowIntoBlock(world, pos.add(1, 0, 0), flowMeta);
            if (flowTo[2]) flowIntoBlock(world, pos.add(0, 0, -1), flowMeta);
            if (flowTo[3]) flowIntoBlock(world, pos.add(0, 0, 1), flowMeta);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && (!this.isUnderwaterBlock || worldIn.getBlockState(pos).getMaterial() == Material.WATER);
    }

}