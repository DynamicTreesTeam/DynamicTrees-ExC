package maxhyper.dynamictreesttf.blocks;

import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import twilightforest.util.WorldUtil;

import java.util.Random;

public class BlockBranchMagicCoreThick extends BlockBranchTwilightThick {

    private BlockBranchMagicCore.Types coreType;
    protected boolean isOn;
    public BlockBranchMagicCoreThick switchedBlock;

    protected BlockBranchMagicCoreThick(Material material, String name, boolean extended, BlockBranchMagicCore.Types type) {
        super(material, name, extended);
        coreType = type;
    }

    public BlockBranchMagicCoreThick(String name, BlockBranchMagicCore.Types type) {
        super(name);
        otherBlock = new BlockBranchMagicCoreThick(Material.WOOD, name+"x", true, type);
        otherBlock.otherBlock = this;
        cacheBranchThickStates();
        coreType = type;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 15;
    }

    @Override
    public int tickRate(World par1World) {
        return 20;
    }

    public BlockBranchMagicCoreThick setOffBlock(BlockBranchMagicCoreThick switchBlock){
        switchedBlock = switchBlock;
        ((BlockBranchMagicCoreThick)otherBlock).switchedBlock = (BlockBranchMagicCoreThick)switchedBlock.otherBlock;
        switchBlock.switchedBlock = this;
        ((BlockBranchMagicCoreThick)switchBlock.otherBlock).switchedBlock = (BlockBranchMagicCoreThick)otherBlock;
        isOn = ((BlockBranchMagicCoreThick) otherBlock).isOn = true;
        switchBlock.isOn = ((BlockBranchMagicCoreThick) switchBlock.otherBlock).isOn = false;
        setTickRandomly(true);
        otherBlock.setTickRandomly(true);
        return this;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

        if (world.isRemote || !isOn) return;

        if (coreType == BlockBranchMagicCore.Types.TIME) {
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.1F, 0.5F);
            doTreeOfTimeEffect(world, pos, rand);
        }

        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (isOn){
            world.scheduleUpdate(pos, this, this.tickRate(world));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        boolean placedCritter = super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        if (!placedCritter && facing == EnumFacing.WEST){
            worldIn.setBlockState(pos, switchedBlock.getDefaultState().withProperty(BlockDynamicTwilightRoots.RADIUSNYBBLE, worldIn.getBlockState(pos).getValue(BlockDynamicTwilightRoots.RADIUSNYBBLE)));
            return true;
        }
        return placedCritter;
    }

    /**
     * The tree of time adds extra ticks to blocks, so that they have twice the normal chance to get a random tick
     */
    public void doTreeOfTimeEffect(World world, BlockPos pos, Random rand) {

        int numticks = 8 * 3 * this.tickRate(world);

        for (int i = 0; i < numticks; i++) {

            BlockPos dPos = WorldUtil.randomOffset(rand, pos, 16);

            IBlockState state = world.getBlockState(dPos);
            Block block = state.getBlock();

            if (block != Blocks.AIR && block.getTickRandomly()) {
                block.updateTick(world, dPos, state, rand);
            }

            TileEntity te = world.getTileEntity(dPos);
            if (te instanceof ITickable && !te.isInvalid()) {
                ((ITickable) te).update();
            }
        }
    }
}
