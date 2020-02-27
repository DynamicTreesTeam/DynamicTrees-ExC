package maxhyper.dynamictreesexc.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.ModContent;
import maxhyper.dynamictreesexc.blocks.BlockDynamicSaplingBurntFeJuniper;
import maxhyper.dynamictreesexc.trees.TreeFeJuniper;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDynamicSeedBurntFeJuniper extends Seed {
    public ItemDynamicSeedBurntFeJuniper() {
        super(new ResourceLocation(DynamicTreesExC.MODID,"ferrousjuniperburntseed").toString());
    }

    @Override
    public boolean doPlanting(World world, BlockPos pos, EntityPlayer planter, ItemStack seedStack) {
        Species species = getSpecies(seedStack);
        if(BlockDynamicSaplingBurntFeJuniper.canSaplingStay(world, species, pos)) {//Do the planting
            world.setBlockState(pos, ModContent.fejuniperSaplingBurnt.getDefaultState());
            return true;
        }
        return false;
    }

}
