package maxhyper.dynamictreesic2.compat;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IC2Proxy {

    void IC2disableWorldGen();

    ItemStack getIC2ResinStack();

    Item getIC2TreeTap(boolean electric);

    boolean IC2CanUseElectricItem(ItemStack handStack, double operationEnergyCost);

    boolean IC2UseElectricItem(ItemStack handStack, double operationEnergyCost, EntityLivingBase entity);

    void IC2TapPlaySound(EntityPlayer player);

    Block IC2GetTreeBlocks(TreeBlock block);

    String IC2GetTreeID();

    enum TreeBlock {
        LOG,
        LEAVES,
        SAPLING
    }
}
