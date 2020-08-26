package maxhyper.dynamictreesttf.blocks;

import maxhyper.dynamictreesttf.ModContent;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockBranchMangrove extends BlockBranchTwilight {

    protected static final PropertyBool WATERLOGGED = PropertyBool.create("waterlogged");

    public BlockBranchMangrove(String name) {
        super(name);
    }

    @Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return false;
    }
    @Override public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handStack = playerIn.getHeldItemMainhand();
        if (handStack.getItem() == Item.getItemFromBlock(Blocks.DIRT)){
            int thisRadius = state.getValue(RADIUS);
            worldIn.setBlockState(pos, ModContent.undergroundMangroveRoot.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, thisRadius ));
            if (!playerIn.isCreative()) handStack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
            playerIn.swingArm(EnumHand.MAIN_HAND);
            return true;
        } else if (handStack.getItem() == Item.getItemFromBlock(Blocks.GRASS)){
            int thisRadius = state.getValue(RADIUS);
            worldIn.setBlockState(pos, ModContent.undergroundMangroveRoot.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, thisRadius ).withProperty(BlockDynamicTwilightRoots.GRASSY, true));
            if (!playerIn.isCreative()) handStack.shrink(1);
            worldIn.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 1, 0.8f);
            playerIn.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = { RADIUS, WATERLOGGED };
        return new ExtendedBlockState(this, listedProperties, CONNECTIONS);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(RADIUS, (meta & 7) + 1).withProperty(WATERLOGGED, meta>7);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(RADIUS) - 1) + (state.getValue(WATERLOGGED)?8:0);
    }
}
