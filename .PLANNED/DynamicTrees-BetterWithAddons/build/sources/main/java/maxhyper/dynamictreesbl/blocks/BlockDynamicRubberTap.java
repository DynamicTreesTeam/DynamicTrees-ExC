package maxhyper.dynamictreesbl.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreesbl.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thebetweenlands.common.block.misc.BlockRubberTap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import thebetweenlands.common.block.terrain.BlockRubberLog;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.tile.TileEntityRubberTap;

import javax.annotation.Nullable;

public class BlockDynamicRubberTap extends BlockRubberTap {
    public BlockDynamicRubberTap(IBlockState material, int ticksPerStep) {
        super(material, ticksPerStep);
    }

    @Override protected ItemStack getBucket(boolean b) {
        return null;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRubberTap();
    }


    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (this.canPlaceAt(world, pos, facing)) {
            return this.getDefaultState().withProperty(FACING, facing);
        } else {
            for (EnumFacing enumfacing : FACING.getAllowedValues()) {
                if(this.canPlaceAt(world, pos, enumfacing))
                    return this.getDefaultState().withProperty(FACING, enumfacing);
            }
            return this.getDefaultState();
        }
    }

    @Override public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }
    @Override public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : FACING.getAllowedValues()) {
            if (this.canPlaceAt(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }
    @Override public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (this.checkForDrop(world, pos, world.getBlockState(pos))) {
            EnumFacing facing = (EnumFacing)state.getValue(FACING);
            EnumFacing.Axis axis = facing.getAxis();
            EnumFacing oppositeFacing = facing.getOpposite();
            if (axis.isVertical() || !this.canPlaceOn(world, pos.offset(oppositeFacing))) {
                this.dropBlockAsItem(world, pos, state, 0);
                world.setBlockToAir(pos);
            }
        }
    }
    @Override protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING))) {
            return true;
        } else {
            if (worldIn.getBlockState(pos).getBlock() == this) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            return false;
        }
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
        BlockPos blockPos = pos.offset(facing.getOpposite());
        boolean isHorizontal = facing.getAxis().isHorizontal();
        return isHorizontal && this.canPlaceOn(worldIn, blockPos);
    }
    private boolean canPlaceOn(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        return state.getBlock() == ModContent.rubberBranch;
    }

}
