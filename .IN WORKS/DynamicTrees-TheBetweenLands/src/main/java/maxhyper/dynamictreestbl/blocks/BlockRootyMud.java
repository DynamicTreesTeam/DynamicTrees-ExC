package maxhyper.dynamictreestbl.blocks;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
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

    public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
        return getMimic(access, pos);
    }

    public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
        return getMudMimic(access, pos);
    }
    @Override
    public boolean isOpaqueCube(IBlockState s) {
        return false;
    }
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.TRANSLUCENT;
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

    public static IBlockState getMudMimic(IBlockAccess access, BlockPos pos) {

        if(!ModConfigs.rootyTextureMimicry) {
            return BlockRegistry.MUD.getDefaultState();
        }

        final int[] dMap = {0, -1, 1};//Y-Axis depth map

        IBlockState mimic = BlockRegistry.MUD.getDefaultState();//Default to mud in case no dirt or grass is found
        IBlockState[] cache = new IBlockState[12];//A cache so we don't need to pull the blocks from the world twice
        int i = 0;

        //Prioritize Mud by searching for TBL's mud material first
        for (int depth : dMap) {
            for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                IBlockState ground = cache[i++] = access.getBlockState(pos.offset(dir).down(depth));
                if (ground.getMaterial() == BLMaterialRegistry.MUD && ground.isBlockNormalCube() && (!(ground.getBlock() instanceof MimicProperty.IMimic))) {
                    return ground;
                }
            }
        }

        //Settle for other kinds of dirt
        for (i = 0; i < 12; i++) {
            IBlockState ground = cache[i];
            if(ground != mimic && ground.getMaterial() == Material.GROUND && ground.isBlockNormalCube() && !(ground.getBlock() instanceof MimicProperty.IMimic)) {
                return ground;
            }
        }

        //Allow grass as well if theres no other options
        for (i = 0; i < 12; i++) {
            IBlockState ground = cache[i];
            if(ground != mimic && ground.getMaterial() == Material.GRASS && ground.isBlockNormalCube() && !(ground.getBlock() instanceof MimicProperty.IMimic)) {
                return ground;
            }
        }

        //If all else fails then just return plain ol' mud
        return mimic;
    }
}
