package maxhyper.dynamictreesextrautils2.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesextrautils2.DynamicTreesExtraUtils2;
import maxhyper.dynamictreesextrautils2.ModContent;
import maxhyper.dynamictreesextrautils2.blocks.BlockDynamicSaplingBurntFeJuniper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDynamicSeedBurntFeJuniper extends Seed {
    public ItemDynamicSeedBurntFeJuniper() {
        super(new ResourceLocation(DynamicTreesExtraUtils2.MODID,"ferrousjuniperburntseed").toString());
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("extrautils2.text.its.dead"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
