package maxhyper.dynamictreesttf.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemMangroveSeed extends Seed {

    public ItemMangroveSeed(String name) {
        super(name);
    }

    public EnumActionResult onItemUsePlantSeed(EntityPlayer player, World world, BlockPos pos, EnumHand hand, ItemStack seedStack, EnumFacing facing, float hitX, float hitY, float hitZ) {

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (world.getBlockState(pos.up()).getMaterial() == Material.WATER){
            return EnumActionResult.PASS;
        }

        if(block.isReplaceable(world, pos)) {
            pos = pos.down();
            facing = EnumFacing.UP;
        }

        if (facing == EnumFacing.UP) {//Ensure this seed is only used on the top side of a block
            if (player.canPlayerEdit(pos, facing, seedStack) && player.canPlayerEdit(pos.up(), facing, seedStack)) {//Ensure permissions to edit block
                if(doPlanting(world, pos.up(), player, seedStack)) {
                    seedStack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

        if (raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        }
        else {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                    return new ActionResult<>(EnumActionResult.FAIL, itemstack);
                }

                BlockPos abovePos = blockpos.up();
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if ((iblockstate.getMaterial() == Material.WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0) && worldIn.isAirBlock(abovePos)) {
                    if(doPlanting(worldIn, abovePos, playerIn, itemstack)) {
                        if (!playerIn.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
                    }
                }
            }

            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.isInWater()){
            entityItem.motionY += 0.05;
        }
        return super.onEntityItemUpdate(entityItem);
    }

}
