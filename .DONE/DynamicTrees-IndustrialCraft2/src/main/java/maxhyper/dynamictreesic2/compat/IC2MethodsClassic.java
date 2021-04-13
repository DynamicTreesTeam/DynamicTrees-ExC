//package maxhyper.dynamictreesic2.compat;
//
//import ic2.api.classic.audio.PositionSpec;
//import ic2.api.item.ElectricItem;
//import ic2.core.IC2;
//import ic2.core.platform.registry.Ic2Sounds;
//import ic2.core.util.events.IC2WorldGenerator;
//import net.minecraft.block.Block;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.common.registry.ForgeRegistries;
//
//public class IC2MethodsClassic implements IC2Proxy {
//
//    @Override
//    public void IC2disableWorldGen() {
//        IC2.config.setValue("WorldGenTreeRubber", false);
//        IC2WorldGenerator.instance.updateConfig();
//    }
//
//    @Override
//    public ItemStack getIC2ResinStack() {
//        Item resin = ForgeRegistries.ITEMS.getValue(new ResourceLocation("ic2","itemharz"));
//        assert resin != null;
//        return new ItemStack(resin);
//    }
//
//    @Override
//    public Item getIC2TreeTap(boolean electric) {
//        if (electric)
//            return ForgeRegistries.ITEMS.getValue(new ResourceLocation("ic2","itemtreetapelectric"));
//        else
//            return ForgeRegistries.ITEMS.getValue(new ResourceLocation("ic2","itemtreetap"));
//    }
//
//    @Override
//    public boolean IC2CanUseElectricItem(ItemStack handStack, double operationEnergyCost) {
//        return ElectricItem.manager.canUse(handStack, operationEnergyCost);
//    }
//
//    @Override
//    public boolean IC2UseElectricItem(ItemStack handStack, double operationEnergyCost, EntityLivingBase entity) {
//        return ElectricItem.manager.use(handStack, operationEnergyCost, entity);
//    }
//
//    @Override
//    public void IC2TapPlaySound(EntityPlayer player) {
//        if (player != null)
//            IC2.audioManager.playOnce(player, PositionSpec.Hand, Ic2Sounds.treeTapUse, true, IC2.audioManager.defaultVolume);
//    }
//
//    @Override
//    public Block IC2GetTreeBlocks(TreeBlock block) {
//        switch (block){
//            case LOG:
//                return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ic2","blockrubwood"));
//            case LEAVES:
//                return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ic2","leaves"));
//            case SAPLING:
//                return ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ic2","blockrubsapling"));
//        }
//        throw new IllegalArgumentException();
//    }
//
//        @Override
//    public String IC2GetTreeID() {
//        return "rubberICC";
//    }
//}