package maxhyper.dynamictreestechreborn.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreestechreborn.DynamicTreesTechReborn;
import maxhyper.dynamictreestechreborn.ModConfigs;
import maxhyper.dynamictreestechreborn.ModContent;
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
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.forge.ForgePowerItemManager;
import techreborn.init.ModItems;
import techreborn.init.ModSounds;

import java.util.Random;

public class BlockDynamicBranchRubber extends BlockBranchBasic {

    public BlockDynamicBranchRubber(boolean tick, String name) {
        super(new ResourceLocation(DynamicTreesTechReborn.MODID,"rubber" + name).toString());
        setTickRandomly(tick);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    };

    private static IBlockState stateFromAge(int age){
        switch (age){
            case 0:
                return ModContent.rubberBranch.getDefaultState();
            case 1:
                return ModContent.rubberBranchFilled.getDefaultState();
        }
        throw new IllegalArgumentException();
    }

    private static int ageFromState(IBlockState state){
        if (state.getBlock() == ModContent.rubberBranchFilled){
            return 1;
        } else if (state.getBlock() == ModContent.rubberBranch){
            return 0;
        } else return -1;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handStack = playerIn.getHeldItemMainhand();
        ItemStack resin = new ItemStack(ModItems.PARTS, 1, 31);
        int age = ageFromState(state);
        if (age > 0) {
            boolean doTap = false;
            if (handStack.getItem() == ModItems.ELECTRIC_TREE_TAP) {
                ForgePowerItemManager capEnergy = new ForgePowerItemManager(handStack);
                if (capEnergy.getEnergyStored() > 20){
                    capEnergy.extractEnergy(20, false);
                    ExternalPowerSystems.requestEnergyFromArmor(capEnergy, playerIn);
                    doTap = true;
                }
            } else if (handStack.getItem() == ModItems.TREE_TAP) {
                handStack.damageItem(1, playerIn);
                doTap = true;
            }
            if (doTap) {
                worldIn.playSound(playerIn, pos, ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS, 0.6F, 1F);
                if (!worldIn.isRemote){
                    if (age == 1){
                        worldIn.setBlockState(pos, stateFromAge(age - 1).withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)), 3);
                        resin.setCount(1 + worldIn.rand.nextInt(3));
                        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, resin));
                    }
                }
            }
            if (doTap) return true;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

}
