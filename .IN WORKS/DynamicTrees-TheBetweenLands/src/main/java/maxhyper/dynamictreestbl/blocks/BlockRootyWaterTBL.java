package maxhyper.dynamictreestbl.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockRootyWater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockRootyWaterTBL extends BlockRootyWater {

    private final BlockFluidBase water;
    protected float quantaFraction = 8f / 9f;
    protected int densityDir = -1;

    public BlockRootyWaterTBL(String name, Block water) {
        super(false, name);
        if (water instanceof BlockFluidBase)
            this.water = (BlockFluidBase) water;
        else throw new IllegalArgumentException();
    }

    @Override
    public IBlockState getExtendedState(IBlockState oldState, IBlockAccess world, BlockPos pos) {
        if (oldState instanceof IExtendedBlockState) {
            IExtendedBlockState state = (IExtendedBlockState)oldState;
            for (EnumFacing dir : EnumFacing.VALUES) {
                state = state.withProperty(RENDER_SIDES[dir.ordinal()], shouldSideBeRendered(state, world, pos, dir));
            }
            IBlockState[][] upBlockState = new IBlockState[3][3];
            float[][] height = new float[3][3];
            float[][] corner = new float[2][2];
            upBlockState[1][1] = world.getBlockState(pos.down(densityDir));
            height[1][1] = water.getFluidHeightForRender(world, pos, upBlockState[1][1]);
            if (height[1][1] == 1)
            {
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        corner[i][j] = 1;
                    }
                }
            }
            else
            {
                for (int i = 0; i < 3; i++)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        if (i != 1 || j != 1)
                        {
                            BlockPos offsetPos = pos.add(i - 1, 0, j - 1);
                            Block offsetBlock = world.getBlockState(offsetPos).getBlock();
                            upBlockState[i][j] = world.getBlockState(offsetPos.down(densityDir));
                            if (offsetBlock instanceof BlockFluidBase){
                                height[i][j] = ((BlockFluidBase) offsetBlock).getFluidHeightForRender(world, offsetPos, upBlockState[i][j]);
                            } else {
                                height[i][j] = water.getFluidHeightForRender(world, offsetPos, upBlockState[i][j]);
                            }

                        }
                    }
                }
                for (int i = 0; i < 2; i++)
                {
                    for (int j = 0; j < 2; j++)
                    {
                        corner[i][j] = getFluidHeightAverage(height[i][j], height[i][j + 1], height[i + 1][j], height[i + 1][j + 1]);
                    }
                }
                //check for downflow above corners
                boolean n =  isFluid(upBlockState[0][1]);
                boolean s =  isFluid(upBlockState[2][1]);
                boolean w =  isFluid(upBlockState[1][0]);
                boolean e =  isFluid(upBlockState[1][2]);
                boolean nw = isFluid(upBlockState[0][0]);
                boolean ne = isFluid(upBlockState[0][2]);
                boolean sw = isFluid(upBlockState[2][0]);
                boolean se = isFluid(upBlockState[2][2]);
                if (nw || n || w)
                {
                    corner[0][0] = 1;
                }
                if (ne || n || e)
                {
                    corner[0][1] = 1;
                }
                if (sw || s || w)
                {
                    corner[1][0] = 1;
                }
                if (se || s || e)
                {
                    corner[1][1] = 1;
                }
            }

            state = state.withProperty(CORNER_HEIGHTS[0], corner[0][0]);
            state = state.withProperty(CORNER_HEIGHTS[1], corner[0][1]);
            state = state.withProperty(CORNER_HEIGHTS[2], corner[1][1]);
            state = state.withProperty(CORNER_HEIGHTS[3], corner[1][0]);
            return state;
        }
        return oldState;
    }

    private float getFluidHeight(IBlockAccess blockAccess, BlockPos blockPosIn, Material blockMaterial) {

        int i = 0;
        int w = 0;
        float f = 0.0F;

        for (int j = 0; j < 4; ++j) {
            BlockPos blockpos = blockPosIn.add(-(j & 1), 0, -(j >> 1 & 1));

            IBlockState posState = blockAccess.getBlockState(blockpos);
            IBlockState posUpState = blockAccess.getBlockState(blockpos.up());

            if (blockAccess.getBlockState(blockpos.up()).getMaterial() == blockMaterial) {
                return 1.0F;
            }

            if (posState.getBlock() instanceof BlockFluidBase){

                f += ((BlockFluidBase) posState.getBlock()).getFluidHeightForRender(blockAccess, blockpos, posUpState);
                ++i;

            } else {
                Material material = posState.getMaterial();

                if (material != blockMaterial) {
                    if (!material.isSolid()) {
                        ++f;
                        ++i;
                    }
                } else {
                    int k = posState.getValue(BlockLiquid.LEVEL);

                    if (k >= 8 || k == 0) {
                        f += BlockLiquid.getLiquidHeightPercent(k) * 10.0F;
                        i += 10;
                    }

                    f += BlockLiquid.getLiquidHeightPercent(k);
                    ++i;

                }
                if (posState.isFullCube() || posState.isOpaqueCube() || posState.isFullBlock()) {
                    w++;
                }
            }
        }
        if (w == 0 && i == 0) return 0;
        if (i == 0) return -1;
        return (1.0F - f / (float) i) + 0.0011f;
    }

    public float getFluidHeightAverage(float... flow) {
        float total = 0;
        int count = 0;

        for (int i = 0; i < flow.length; i++)
        {
            if (flow[i] >= quantaFraction)
            {
                total += flow[i] * 10;
                count += 10;
            }

            if (flow[i] >= 0)
            {
                total += flow[i];
                count++;
            }
        }

        return total / count;
    }

    private static boolean isFluid(@Nonnull IBlockState blockstate)
    {
        return blockstate.getMaterial().isLiquid() || blockstate.getBlock() instanceof IFluidBlock;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        int y = fromPos.getY() - pos.getY();
        if(y < 1) {
            IBlockState newState = worldIn.getBlockState(fromPos);
            if(canFlowInto(worldIn, fromPos, newState)) {
                newState.getBlock().dropBlockAsItem(worldIn, pos, newState, 0);
                worldIn.setBlockState(fromPos, water.getDefaultState().withProperty(BlockLiquid.LEVEL, 1), 2);
            }
        }
    }
    private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state) {
        Material material = state.getMaterial();
        return material != this.blockMaterial && material != Material.LAVA && !this.isBlocked(worldIn, pos, state);
    }
    private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
        Block block = state.getBlock(); //Forge: state must be valid for position
        Material mat = state.getMaterial();

        if (!(block instanceof BlockDoor) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER && block != Blocks.REEDS) {
            return mat == Material.PORTAL || mat == Material.STRUCTURE_VOID || mat.blocksMovement();
        }
        else {
            return true;
        }
    }
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockState(pos, water.getDefaultState());
    }

    @Override
    public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
        return  water.getDefaultState();
    }

    @Override
    public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
        return water.getDefaultState();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
        return water.getPackedLightmapCoords(state, source, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks) {
        return water.getFogColor(world, pos, water.getDefaultState(), entity, originalColor, partialTicks);
    }
}
