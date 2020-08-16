package maxhyper.dynamictreesforestry.items;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;

public class ItemDynamicSeedBalsa extends Seed {

    public ItemDynamicSeedBalsa() {
        super(new ResourceLocation(DynamicTreesForestry.MODID,"balsaseed").toString());
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        //Add lift to counteract the gravity that will be applied in super.onUpdate()
        entityItem.motionY += 0.035;
        return super.onEntityItemUpdate(entityItem);
    }

}