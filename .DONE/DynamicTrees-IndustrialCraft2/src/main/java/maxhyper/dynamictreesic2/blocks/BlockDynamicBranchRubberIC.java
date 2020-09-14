package maxhyper.dynamictreesic2.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.tool.ItemTreetapElectric;
import ic2.core.item.type.MiscResourceType;
import ic2.core.ref.ItemName;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import maxhyper.dynamictreesic2.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDynamicBranchRubberIC extends BlockBranchBasic {

    public BlockDynamicBranchRubberIC() {
        super(new ResourceLocation(DynamicTreesIC2.MODID,"rubbericbranch").toString());
        setTickRandomly(true);
    }
    public BlockDynamicBranchRubberIC(boolean filled) {
        super(new ResourceLocation(DynamicTreesIC2.MODID,"rubbericbranchfilled").toString());
        setTickRandomly(false);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    };

    @Override
    public int getMaxRadius() {
        return 7;
    }

    @Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        performUpdate(worldIn, pos);
    }

    private void performUpdate(World worldIn, BlockPos pos){
        if (worldIn.getBlockState(pos).getValue(RADIUS) > 6 &&
                RANDOM.nextInt(50 * 8/worldIn.getBlockState(pos).getValue(RADIUS)) == 0 &&
                worldIn.getBlockState(pos.up()).getBlock() != ModContent.rubberICBranchFilled &&
                worldIn.getBlockState(pos.down()).getBlock() != ModContent.rubberICBranchFilled){
            worldIn.setBlockState(pos, ModContent.rubberICBranchFilled.getDefaultState().withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            ItemStack handStack = playerIn.getHeldItemMainhand();
            Block branch = ModContent.rubberICBranch;
            Block filledBranch = ModContent.rubberICBranchFilled;
            ItemStack resin = ItemName.misc_resource.getItemStack(MiscResourceType.resin);
            int operationEnergyCost = 50;
            if (state.getBlock() == filledBranch) {
                boolean doTap = false;
                if (handStack.getItem() == ItemName.electric_treetap.getInstance() && ElectricItem.manager.canUse(handStack, operationEnergyCost)) {
                    ElectricItem.manager.use(handStack, operationEnergyCost, playerIn);
                    doTap = true;
                } else if (handStack.getItem() == ItemName.treetap.getInstance()) {
                    handStack.damageItem(1, playerIn);
                    doTap = true;
                }
                if (doTap) {
                    worldIn.setBlockState(pos, branch.getDefaultState().withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));
                    IC2.audioManager.playOnce(playerIn, PositionSpec.Hand, "Tools/Treetap.ogg", true, IC2.audioManager.getDefaultVolume());
                    resin.setCount(1 + RANDOM.nextInt(3));
                    if (!worldIn.isRemote){
                        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, resin));
                    }
                }
            }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
