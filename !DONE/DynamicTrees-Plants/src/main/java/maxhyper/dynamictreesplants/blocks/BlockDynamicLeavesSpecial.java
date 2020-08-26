package maxhyper.dynamictreesplants.blocks;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import shadows.plants2.block.BlockEnumNetherLeaves;
import shadows.plants2.data.enums.TheBigBookOfEnums;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDynamicLeavesSpecial extends BlockDynamicLeaves {

    public BlockDynamicLeavesSpecial() {
        super();
        setRegistryName(DynamicTreesPlants.MODID, "leaves1");
        setUnlocalizedName("leaves1");
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.getBlockState(pos).getProperties().containsKey(TREE)) {
            int tree = world.getBlockState(pos).getValue(TREE);
            if (tree == 0) {
                if (rand.nextFloat() < 0.1F) world.spawnParticle(TheBigBookOfEnums.NetherLogs.ASH.getParticle(), pos.getX() + 0.5, pos.getY() - 0.8, pos.getZ() + 0.5, BlockEnumNetherLeaves.getDouble(rand), -0.1, BlockEnumNetherLeaves.getDouble(rand));
            } else if (tree == 1){
                if (rand.nextFloat() < 0.1F) world.spawnParticle(TheBigBookOfEnums.NetherLogs.BLAZE.getParticle(), pos.getX() + 0.5, pos.getY() - 0.8, pos.getZ() + 0.5, BlockEnumNetherLeaves.getDouble(rand), -0.1, BlockEnumNetherLeaves.getDouble(rand));
            }
        }
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        if (state.getProperties().containsKey(TREE)) {
            int tree = state.getValue(TREE);
            if (tree == 2 || tree == 3) {
                return SoundType.GLASS;
            }
        }
        return super.getSoundType(state, world, pos, entity);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {

        if(ModConfigs.canopyCrash && entity instanceof EntityLivingBase) { //We are only interested in Living things crashing through the canopy.
            entity.fallDistance--;

            AxisAlignedBB aabb = entity.getEntityBoundingBox();

            int minX = MathHelper.floor(aabb.minX + 0.001D);
            int minZ = MathHelper.floor(aabb.minZ + 0.001D);
            int maxX = MathHelper.floor(aabb.maxX - 0.001D);
            int maxZ = MathHelper.floor(aabb.maxZ - 0.001D);

            boolean crushing = true;
            boolean hasLeaves = true;

            SoundType stepSound = this.getSoundType(world.getBlockState(pos), world, pos, entity);
            float volume = MathHelper.clamp(stepSound.getVolume() / 16.0f * fallDistance, 0, 3.0f);
            world.playSound(entity.posX, entity.posY, entity.posZ, stepSound.getBreakSound(), SoundCategory.BLOCKS, volume, stepSound.getPitch(), false);

            for(int iy = 0; (entity.fallDistance > 3.0f) && crushing && ((pos.getY() - iy) > 0); iy++) {
                if(hasLeaves) {//This layer has leaves that can help break our fall
                    entity.fallDistance *= 0.66f;//For each layer we are crushing break the momentum
                    hasLeaves = false;
                }
                for(int ix = minX; ix <= maxX; ix++) {
                    for(int iz = minZ; iz <= maxZ; iz++) {
                        BlockPos iPos = new BlockPos(ix, pos.getY() - iy, iz);
                        IBlockState state = world.getBlockState(iPos);
                        if(TreeHelper.isLeaves(state)) {
                            hasLeaves = true;//This layer has leaves
                            DynamicTrees.proxy.crushLeavesBlock(world, iPos, state, entity);
                            world.setBlockToAir(iPos);
                        } else
                        if (!world.isAirBlock(iPos)) {
                            crushing = false;//We hit something solid thus no longer crushing leaves layers
                        }
                    }
                }
            }
        }
    }

}
