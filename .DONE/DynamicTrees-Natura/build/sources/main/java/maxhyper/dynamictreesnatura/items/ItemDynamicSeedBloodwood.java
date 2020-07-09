package maxhyper.dynamictreesnatura.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemDynamicSeedBloodwood extends Seed {
    public ItemDynamicSeedBloodwood() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"bloodwoodseed").toString());
    }
    @Override
    public boolean doPlanting(World world, BlockPos pos, EntityPlayer planter, ItemStack seedStack) {
        Species species = getSpecies(seedStack);
        if(species.plantSapling(world, pos)) {//Do the planting
            String joCode = getCode(seedStack);
            if(!joCode.isEmpty()) {
                world.setBlockToAir(pos);//Remove the newly created dynamic sapling
                species.getJoCode(joCode).setCareful(true).generate(world, species, pos.up(), world.getBiome(pos), planter != null ? planter.getHorizontalFacing() : EnumFacing.NORTH, 8, SafeChunkBounds.ANY);
            }
            return true;
        }
        return false;
    }

    @Override public EnumActionResult onItemUsePlantSeed(EntityPlayer player, World world, BlockPos pos, EnumHand hand, ItemStack seedStack, EnumFacing facing, float hitX, float hitY, float hitZ) {

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if(block.isReplaceable(world, pos)) {
            pos = pos.up();
            facing = EnumFacing.DOWN;
        }

        if (facing == EnumFacing.DOWN) {//Ensure this seed is only used on the bottom side of a block
            if (player.canPlayerEdit(pos, facing, seedStack) && player.canPlayerEdit(pos.down(), facing, seedStack)) {//Ensure permissions to edit block
                if(doPlanting(world, pos.down(), player, seedStack)) {
                    seedStack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        entityItem.motionY += 0.03f;
        return super.onEntityItemUpdate(entityItem);
    }

}
