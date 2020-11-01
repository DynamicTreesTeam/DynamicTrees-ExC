package maxhyper.dynamictreesnatura.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.mantle.util.LocUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDynamicSeedHopseed extends Seed {

    public ItemDynamicSeedHopseed() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"hopseedseed").toString());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(LocUtils.translateRecursive("tile.natura.overworld_sapling2.hopseed.tooltip"))));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    double prevSpeedY = 0;
    float bounciness = 0.9f;
    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (Math.abs(prevSpeedY) > 0.1 && entityItem.onGround){
            entityItem.motionY = Math.abs(prevSpeedY * bounciness);

            entityItem.world.spawnParticle(EnumParticleTypes.CLOUD, entityItem.posX, entityItem.posY, entityItem.posZ, 0,0,0);
            entityItem.world.playSound(entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_RABBIT_JUMP, SoundCategory.BLOCKS,5, 1, false);
        }
        prevSpeedY = entityItem.motionY;
        return super.onEntityItemUpdate(entityItem);
    }

}