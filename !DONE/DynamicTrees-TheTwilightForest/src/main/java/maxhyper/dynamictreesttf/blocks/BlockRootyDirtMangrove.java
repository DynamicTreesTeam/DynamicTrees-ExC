package maxhyper.dynamictreesttf.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import maxhyper.dynamictreesttf.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockRootyDirtMangrove extends BlockRooty {

    static String name = "rootydirtmangrove";

    public BlockRootyDirtMangrove(boolean isTileEntity) {
        this(name + (isTileEntity ? "species" : ""), isTileEntity);
    }

    public BlockRootyDirtMangrove(String name, boolean isTileEntity) {
        super(name, Material.GROUND, isTileEntity);
    }

    @Override
    public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
        return MimicProperty.getDirtMimic(access, pos);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (world instanceof World && !(world.getBlockState(pos.up()).getBlock() instanceof BlockBranch)){
            ((World)world).setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public int getRadiusForConnection(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, BlockBranch from, EnumFacing side, int fromRadius) {
        if (side == EnumFacing.DOWN){
            return super.getRadiusForConnection(blockState, blockAccess, pos, from, side, fromRadius);
        } else {
            return 0;
        }

    }
}
