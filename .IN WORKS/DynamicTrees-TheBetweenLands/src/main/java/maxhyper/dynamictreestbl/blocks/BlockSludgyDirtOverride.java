package maxhyper.dynamictreestbl.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import thebetweenlands.common.block.terrain.BlockSludgyDirt;
import thebetweenlands.common.block.terrain.BlockSpreadingSludgyDirt;

public class BlockSludgyDirtOverride extends BlockSludgyDirt {

    public BlockSludgyDirtOverride (ResourceLocation name){
        setRegistryName(name);
        setUnlocalizedName(name.getResourceDomain()+"."+name.getResourcePath());
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess iblockaccess, BlockPos pos, EnumFacing side) {
        Block block = iblockaccess.getBlockState(pos.offset(side)).getBlock();
        return !(block instanceof BlockSludgyDirt) && !(block instanceof BlockSpreadingSludgyDirt) && !(block instanceof BlockRootyMud) && !(block.isOpaqueCube(blockState));
    }
}