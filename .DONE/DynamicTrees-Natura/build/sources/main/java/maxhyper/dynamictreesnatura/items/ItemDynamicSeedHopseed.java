package maxhyper.dynamictreesnatura.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class ItemDynamicSeedHopseed extends Seed {

    public ItemDynamicSeedHopseed() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"hopseedseed").toString());
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