package maxhyper.dynamictreesttf.blocks;

import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockDynamicTwilightRoots extends Block implements ITreePart {// ,MimicProperty.IMimic {

    public static final PropertyInteger RADIUS = PropertyInteger.create("radius", 1, 8);
    public static final PropertyBool GRASSY = PropertyBool.create("grassy");
    public static final PropertyInteger RADIUSNYBBLE = PropertyInteger.create("radius", 0, 15);

    public BlockDynamicTwilightRoots() {
        super(Material.GROUND);
        setDefaultState(this.getDefaultState().withProperty(RADIUS, 1).withProperty(GRASSY, false));
        setHardness(0.8f);
        needsRandomTick = true;
        setSoundType(SoundType.GROUND);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        if (state.getValue(GRASSY)){
            return SoundType.PLANT;
        } else {
            return SoundType.GROUND;
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (!worldIn.isAreaLoaded(pos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (state.getValue(GRASSY) && worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getLightOpacity(worldIn, pos.up()) > 2)
            {
                worldIn.setBlockState(pos, state.withProperty(GRASSY, false));
            }
            else
            {
                if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
                {
                    for (int i = 0; i < 4; ++i)
                    {
                        BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                        if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos))
                        {
                            return;
                        }

                        IBlockState iblockstate = worldIn.getBlockState(blockpos.up());
                        IBlockState iblockstate1 = worldIn.getBlockState(blockpos);

                        if (state.getValue(GRASSY) && iblockstate1.getBlock() == Blocks.DIRT && iblockstate1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && iblockstate.getLightOpacity(worldIn, pos.up()) <= 2)
                        {
                            worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState());
                        }
                        if (!state.getValue(GRASSY) && (iblockstate1.getBlock() == Blocks.GRASS || (iblockstate1.getProperties().containsKey(GRASSY) && iblockstate1.getValue(GRASSY))) && worldIn.getLightFromNeighbors(pos.up()) >= 9 && iblockstate.getLightOpacity(worldIn, blockpos.up()) <= 2)
                        {
                            worldIn.setBlockState(pos, state.withProperty(GRASSY, true));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        if (state.getValue(GRASSY)){
            return new ItemStack(Item.getItemFromBlock(Blocks.GRASS));
        } else {
            return new ItemStack(Item.getItemFromBlock(Blocks.DIRT));
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        NonNullList<ItemStack> ret = NonNullList.create();
        ret.add(new ItemStack(Item.getItemFromBlock(Blocks.DIRT)));
        return ret;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (state.getValue(GRASSY)){
            return new ItemStack(Item.getItemFromBlock(Blocks.GRASS));
        } else {
            return new ItemStack(Item.getItemFromBlock(Blocks.DIRT));
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        super.onBlockDestroyedByPlayer(world, pos, state);
        world.setBlockState(pos, ModContent.undergroundRootExposed.getDefaultState().withProperty(RADIUS, state.getValue(RADIUS)));
   }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = { RADIUS, GRASSY };
        return new BlockStateContainer(this, listedProperties);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta >= 8){
            return this.getDefaultState().withProperty(RADIUS, (meta & 7) + 1).withProperty(GRASSY, true);
        } else {
            return this.getDefaultState().withProperty(RADIUS, (meta & 7) + 1).withProperty(GRASSY, false);
        }
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(RADIUS) - 1) + (state.getValue(GRASSY)?8:0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public ICell getHydrationCell(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing, ILeavesProperties iLeavesProperties) {
        return CellNull.NULLCELL;
    }

    @Override
    public GrowSignal growSignal(World world, BlockPos blockPos, GrowSignal growSignal) {
        return growSignal;
    }

    @Override
    public int probabilityForBlock(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos, BlockBranch blockBranch) {
        return 0;
    }

    @Override
    public int getRadiusForConnection(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos, BlockBranch blockBranch, EnumFacing enumFacing, int i) {
        return 8;
    }

    @Override
    public int getRadius(IBlockState iBlockState) {
        return iBlockState.getBlock() == this ? iBlockState.getValue(RADIUS) : 0;
    }

    @Override
    public boolean shouldAnalyse() {
        return false;
    }

    @Override
    public MapSignal analyse(IBlockState iBlockState, World world, BlockPos blockPos, EnumFacing enumFacing, MapSignal mapSignal) {
        return mapSignal;
    }

    @Override
    public TreeFamily getFamily(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return TreeFamily.NULLFAMILY;
    }

    @Override
    public int branchSupport(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockBranch blockBranch, BlockPos blockPos, EnumFacing enumFacing, int i) {
        return BlockBranch.setSupport(1, 1);
    }

    @Override
    public TreePartType getTreePartType() {
        return TreePartType.ROOT;
    }
}
