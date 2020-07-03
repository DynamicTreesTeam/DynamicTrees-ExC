package maxhyper.dynamictreesttf.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockTrunkShell;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.block.BlockTFFirefly;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class BlockDynamicTFFirefly extends BlockTFFirefly {

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(TFBlocks.firefly);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(TFBlocks.firefly));
    }

    @Override
    protected boolean canPlaceAt(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        return (state.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID || state.getBlock() instanceof BlockBranch || state.getBlock() instanceof BlockTrunkShell)
                && (!isExceptBlockForAttachWithPiston(state.getBlock())
                || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.CACTUS);
    }

}
