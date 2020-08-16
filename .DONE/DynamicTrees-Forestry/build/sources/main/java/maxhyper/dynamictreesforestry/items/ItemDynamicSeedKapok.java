package maxhyper.dynamictreesforestry.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;

public class ItemDynamicSeedKapok extends Seed {

    public ItemDynamicSeedKapok() {
        super(new ResourceLocation(DynamicTreesForestry.MODID,"kapokseed").toString());
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        //Add lift to counteract the gravity that will be applied in super.onUpdate()
        entityItem.motionY += 0.03;
        return super.onEntityItemUpdate(entityItem);
    }

}