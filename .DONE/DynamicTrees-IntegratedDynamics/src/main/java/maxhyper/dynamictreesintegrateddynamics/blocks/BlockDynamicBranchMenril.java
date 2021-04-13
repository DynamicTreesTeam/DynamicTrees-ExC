package maxhyper.dynamictreesintegrateddynamics.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreesintegrateddynamics.DynamicTreesIntegratedDynamics;
import maxhyper.dynamictreesintegrateddynamics.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.integrateddynamics.item.ItemCrystalizedMenrilChunkConfig;

import java.util.Random;

//import reborncore.common.powerSystem.ExternalPowerSystems;
//import reborncore.common.powerSystem.forge.ForgePowerItemManager;
//import reborncore.common.util.WorldUtils;
//import techreborn.init.ModItems;
//import techreborn.init.ModSounds;
//
//import ic2.core.IC2;
//import ic2.core.audio.PositionSpec;
//import ic2.core.ref.ItemName;

public class BlockDynamicBranchMenril extends BlockBranchBasic {

    public BlockDynamicBranchMenril(boolean tick, String name) {
        super(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID,"menril" + name).toString());
        setTickRandomly(tick);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    }

//    @Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
//        super.updateTick(worldIn, pos, state, rand);
//        performUpdate(worldIn, pos, state, rand);
//    }
//    private void performUpdate(World worldIn, BlockPos pos, IBlockState state, Random rand){
//        if (worldIn.getBlockState(pos).getValue(RADIUS) > 6 &&
//                RANDOM.nextInt(50 * 8/worldIn.getBlockState(pos).getValue(RADIUS)) == 0 &&
//                worldIn.getBlockState(pos.up()).getBlock() != ModContent.menrilBranchFilled &&
//                worldIn.getBlockState(pos.down()).getBlock() != ModContent.menrilBranchFilled){
//            worldIn.setBlockState(pos, ModContent.menrilBranchFilled.getDefaultState().withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));
//        }
//    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handStack = playerIn.getHeldItemMainhand();
        ItemStack resin = new ItemStack(ItemCrystalizedMenrilChunkConfig._instance.getItemInstance());

        if (handStack.isEmpty() && state.getBlock() == ModContent.menrilBranchFilled){
            if (worldIn.isRemote){
                worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1f, worldIn.rand.nextFloat() + 0.5f, false);
            } else {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, resin));
                worldIn.setBlockState(pos, ModContent.menrilBranch.getDefaultState().withProperty(RADIUS, TreeHelper.getRadius(worldIn, pos)));
            }
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
