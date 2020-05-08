package maxhyper.dynamictreesbl.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.ref.ItemName;
import maxhyper.dynamictreesbl.DynamicTreesBL;
import maxhyper.dynamictreesbl.ModContent;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.WorldUtils;
import techreborn.init.ModItems;
import techreborn.init.ModSounds;
import thebetweenlands.common.block.misc.BlockRubberTap;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.Random;

public class BlockDynamicBranchRubber extends BlockBranchBasic {

    public BlockDynamicBranchRubber() {
        super(new ResourceLocation(DynamicTreesBL.MODID,"rubberbranch").toString());
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    };

    @Override
    public int getMaxRadius() {
        return 4;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handStack = playerIn.getHeldItemMainhand();
        if (handStack.getItem() == ItemRegistry.BL_BUCKET && worldIn.getBlockState(pos).getValue(RADIUS) == 4 && worldIn.getBlockState(pos.offset(facing)).getBlock().isReplaceable(worldIn, pos.offset(facing))){
            if (handStack.getItem().getMetadata(handStack) == 0){
                //System.out.println("X: "+hitX + " | Y: "+ hitY+" | Z: "+ hitZ);
                worldIn.setBlockState(pos.offset(facing), ModContent.dynamicWeedwoodRubberTap.getDefaultState().withProperty(BlockRubberTap.FACING, facing));
                if (!playerIn.isCreative()) handStack.shrink(1);
                worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.PLAYERS, 1, 1);
            } else if (handStack.getItem().getMetadata(handStack) == 1){
                worldIn.setBlockState(pos.offset(facing), ModContent.dynamicSyrmoriteRubberTap.getDefaultState().withProperty(BlockRubberTap.FACING, facing));
                if (!playerIn.isCreative()) handStack.shrink(1);
                worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.PLAYERS, 1, 1);
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
