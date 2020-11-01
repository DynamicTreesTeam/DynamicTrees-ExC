package maxhyper.dynamictreesnatura.items;

import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.mantle.util.LocUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDynamicSeedMaple extends Seed {

    public ItemDynamicSeedMaple() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"mapleseed").toString());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(LocUtils.translateRecursive("tile.natura.overworld_sapling.maple.tooltip"))));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    @Nullable
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        EntityItemMapleSeed mapleSeedEntity = new EntityItemMapleSeed(world, location.posX, location.posY, location.posZ, itemstack);

        //We need to also copy the motion of the replaced entity or it acts funny when the item spawns.
        mapleSeedEntity.motionX = location.motionX;
        mapleSeedEntity.motionY = location.motionY;
        mapleSeedEntity.motionZ = location.motionZ;

        return mapleSeedEntity;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {

        if (entityItem.motionY<=0){
            //Counteract the air friction that will be applied in super.onUpdate() this results in a 0.99 factor instead of 0.98
            entityItem.motionX *= 1.01f;
            entityItem.motionZ *= 1.01f;

            //Add lift to counteract the gravity that will be applied in super.onUpdate()
            entityItem.motionY += 0.03;
        }

        return super.onEntityItemUpdate(entityItem);
    }

    public static class EntityItemMapleSeed extends EntityItem {

        public EntityItemMapleSeed(World worldIn) {
            super(worldIn);
        }

        public EntityItemMapleSeed(World worldIn, double x, double y, double z, ItemStack stack) {
            super(worldIn, x, y, z, stack);
            this.setDefaultPickupDelay();
        }

        @Override
        public void setDefaultPickupDelay() {
            this.setPickupDelay(50);
        }

    }

}