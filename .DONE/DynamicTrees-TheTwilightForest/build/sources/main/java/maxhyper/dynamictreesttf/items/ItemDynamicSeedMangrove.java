package maxhyper.dynamictreesttf.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class ItemDynamicSeedMangrove extends Seed {

    public ItemDynamicSeedMangrove() {
        super(new ResourceLocation(DynamicTreesTTF.MODID,"mangroveseed").toString());
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.isInWater()){
            entityItem.motionY += 0.05;
        }
        return super.onEntityItemUpdate(entityItem);
    }

}