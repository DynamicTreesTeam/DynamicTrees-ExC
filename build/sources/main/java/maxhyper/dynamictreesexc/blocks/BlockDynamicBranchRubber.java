package maxhyper.dynamictreesexc.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.ModContent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import reborncore.common.util.WorldUtils;
import techreborn.init.ModItems;
import techreborn.init.ModSounds;

import java.util.Random;

public class BlockDynamicBranchRubber extends BlockBranchBasic {

    public BlockDynamicBranchRubber() {
        super(Material.WOOD, new ResourceLocation(DynamicTreesExC.MODID,"rubberbranch").toString());
        setTickRandomly(true);
    }
    public BlockDynamicBranchRubber(boolean filled) {
        super(Material.WOOD, new ResourceLocation(DynamicTreesExC.MODID,"rubberbranchfilled").toString());
        setTickRandomly(false);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    };

    @Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        performUpdate(worldIn, pos, state, rand);
    }
    private void performUpdate(World worldIn, BlockPos pos, IBlockState state, Random rand){
        if (worldIn.getBlockState(pos).getValue(RADIUS) > 5 && RANDOM.nextInt(50 * 8/worldIn.getBlockState(pos).getValue(RADIUS)) == 0){
            worldIn.setBlockState(pos, ModContent.rubberBranchFilled.getDefaultState().withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handStack = playerIn.getHeldItemMainhand();
        if (state.getBlock() == ModContent.rubberBranchFilled){
            ForgePowerItemManager capEnergy = null;
            if (handStack.getItem() == ModItems.ELECTRIC_TREE_TAP) {
                capEnergy = new ForgePowerItemManager(handStack);
            }
            if ((capEnergy != null && capEnergy.getEnergyStored() > 20) || handStack.getItem() == ModItems.TREE_TAP) {
                worldIn.setBlockState(pos, ModContent.rubberBranch.getDefaultState().withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));
                worldIn.playSound(playerIn, pos, ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS, 0.6F, 1F);
                WorldUtils.dropItem(new ItemStack(ModItems.PARTS, 1 + RANDOM.nextInt(2), 31), worldIn, pos);
                if (!worldIn.isRemote) {
                    if (capEnergy != null) {
                        capEnergy.extractEnergy(20, false);
                        ExternalPowerSystems.requestEnergyFromArmor(capEnergy, playerIn);
                    } else {
                        handStack.damageItem(1, playerIn);
                    }
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
