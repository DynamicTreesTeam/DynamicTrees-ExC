package maxhyper.dynamictreesttf.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import maxhyper.dynamictreesttf.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;

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
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        IBlockState upState = world.getBlockState(pos.up());
        //if (upState.getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS) && upState.getValue(BlockDynamicTwilightRoots.RADIUS) > 6){
            world.setBlockState(pos, ModContent.mangroveBranch.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, 8));
        //} else {
        //    destroyTree(world, pos);
        //}
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        IBlockState upState = world.getBlockState(pos.up());
        if (upState.getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS) && upState.getValue(BlockDynamicTwilightRoots.RADIUS) > 6){
            world.setBlockState(pos, ModContent.mangroveBranch.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUS, 8));
        } else {
            destroyTree(world, pos);
        }
    }

}
