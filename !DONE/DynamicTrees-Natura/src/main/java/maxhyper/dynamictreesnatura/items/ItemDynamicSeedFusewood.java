package maxhyper.dynamictreesnatura.items;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.EnumDifficulty;

public class ItemDynamicSeedFusewood extends Seed {

    public ItemDynamicSeedFusewood() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"fusewoodseed").toString());
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.ticksExisted >= ModConfigs.seedTimeToLive){
            if (entityItem.world.getDifficulty() == EnumDifficulty.HARD)
            {
                entityItem.world.createExplosion(null, entityItem.posX, entityItem.posY, entityItem.posZ, Math.max(entityItem.getItem().getCount()/4f, 2) , false);
            }
            else if (entityItem.world.getDifficulty() == EnumDifficulty.NORMAL || entityItem.world.getDifficulty() == EnumDifficulty.EASY)
            {
                entityItem.world.createExplosion(null, entityItem.posX, entityItem.posY, entityItem.posZ, Math.max(entityItem.getItem().getCount()/5f, 1.75f), false);
            }
        }
        return super.onEntityItemUpdate(entityItem);
    }

}