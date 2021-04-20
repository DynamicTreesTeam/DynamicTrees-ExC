package maxhyper.dynamictreestbl.blocks;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import thebetweenlands.common.block.terrain.BlockSludgyDirt;
import thebetweenlands.common.block.terrain.BlockSpreadingSludgyDirt;
import thebetweenlands.common.item.BLMaterialRegistry;
import thebetweenlands.common.registries.BlockRegistry;

public class BlockRootyMud extends BlockRooty {
    static String name = "rootymud";

    public BlockRootyMud(boolean isTileEntity) {
        this(name + (isTileEntity ? "species" : ""), isTileEntity);
    }

    public BlockRootyMud(String name, boolean isTileEntity) {
        super(name, Material.GROUND, isTileEntity);
    }

    ///////////////////////////////////////////
    // BLOCKSTATES
    ///////////////////////////////////////////

    public static final Material[] materialOrder = {BLMaterialRegistry.MUD, Material.GROUND, Material.CLAY, Material.GRASS};
    @Override
    public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
        return MimicProperty.getGenericMimic(access, pos, materialOrder, DirtHelper.getSoilFlags(DirtHelper.MUDLIKE), BlockRegistry.MUD.getDefaultState());
    }

    ///////////////////////////////////////////
    // RENDERING
    ///////////////////////////////////////////

    @Override
    public boolean isOpaqueCube(IBlockState s) {
        return false;
    }
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.CUTOUT_MIPPED;
    }
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess access, BlockPos pos, EnumFacing side) {
        Block mimic = getMimic(access, pos).getBlock();
        if (!(mimic instanceof BlockSludgyDirt) && !(mimic instanceof BlockSpreadingSludgyDirt)){
            return super.shouldSideBeRendered(blockState, access, pos, side);
        }
        Block block = access.getBlockState(pos.offset(side)).getBlock();
        return !(block instanceof BlockSludgyDirt) && !(block instanceof BlockSpreadingSludgyDirt) && super.shouldSideBeRendered(blockState, access, pos, side);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, 1 - 0.125F, 1);
    }
}
