package maxhyper.dynamictreesnatura.items;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.items.Seed;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import slimeknights.mantle.util.LocUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDynamicSeedFusewood extends Seed {

    public ItemDynamicSeedFusewood() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"fusewoodseed").toString());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(LocUtils.translateRecursive("tile.natura.nether_sapling.fusewood.tooltip"))));
        super.addInformation(stack, worldIn, tooltip, flagIn);
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