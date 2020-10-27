package maxhyper.dynamictreesttf.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

public class BlockBranchTwilight extends BlockBranchBasic {

    public BlockBranchTwilight(String name) {
        super(new ResourceLocation(DynamicTreesTTF.MODID,name).toString());
    }

    public static final int minCritterRadius = 5;

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handStack = playerIn.getHeldItemMainhand();
        if (handStack.getItem() == Item.getItemFromBlock(TFBlocks.cicada) && worldIn.getBlockState(pos).getValue(RADIUS) >= minCritterRadius && worldIn.getBlockState(pos.offset(facing)).getBlock().isReplaceable(worldIn, pos.offset(facing))){
            worldIn.setBlockState(pos.offset(facing), ModContent.dynamicCicada.getDefaultState().withProperty(BlockDirectional.FACING, facing));
            if (!playerIn.isCreative()) handStack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_SLIME_PLACE, SoundCategory.PLAYERS, 1, 1);
            playerIn.swingArm(EnumHand.MAIN_HAND);
            return true;
        } else if (handStack.getItem() == Item.getItemFromBlock(TFBlocks.firefly) && worldIn.getBlockState(pos).getValue(RADIUS) >= minCritterRadius && worldIn.getBlockState(pos.offset(facing)).getBlock().isReplaceable(worldIn, pos.offset(facing))){
            worldIn.setBlockState(pos.offset(facing), ModContent.dynamicFirefly.getDefaultState().withProperty(BlockDirectional.FACING, facing));
            if (!playerIn.isCreative()) handStack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_SLIME_PLACE, SoundCategory.PLAYERS, 1, 1);
            playerIn.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
