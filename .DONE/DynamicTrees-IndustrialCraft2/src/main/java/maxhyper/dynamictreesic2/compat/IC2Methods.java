package maxhyper.dynamictreesic2.compat;

import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.init.MainConfig;
import ic2.core.item.type.MiscResourceType;
import ic2.core.ref.ItemName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraft.block.Block;

public class IC2Methods implements IC2Proxy {

    @Override
    public void IC2disableWorldGen(){
        MainConfig.get().set("worldgen/rubberTree", false);
    }

    @Override
    public ItemStack getIC2ResinStack(){
        return ItemName.misc_resource.getItemStack(MiscResourceType.resin);
    }

    @Override
    public Item getIC2TreeTap(boolean electric){
        if (electric)
            return ItemName.electric_treetap.getInstance();
        else
            return ItemName.treetap.getInstance();
    }

    @Override
    public boolean IC2CanUseElectricItem(ItemStack handStack, double operationEnergyCost){
        return ElectricItem.manager.canUse(handStack, operationEnergyCost);
    }

    @Override
    public boolean IC2UseElectricItem(ItemStack handStack, double operationEnergyCost, EntityLivingBase entity){
        return ElectricItem.manager.use(handStack, operationEnergyCost, entity);
    }

    @Override
    public void IC2TapPlaySound(EntityPlayer player){
        if (player != null)
            IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/Treetap.ogg", true, IC2.audioManager.getDefaultVolume());
    }

    @Override
    public Block IC2GetTreeBlocks(TreeBlock block) {
        switch (block){
            case LOG:
                return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ic2","rubber_wood"));
            case LEAVES:
                return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ic2","leaves"));
            case SAPLING:
                return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ic2","sapling"));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String IC2GetTreeID() {
        return "rubberIC";
    }
}

