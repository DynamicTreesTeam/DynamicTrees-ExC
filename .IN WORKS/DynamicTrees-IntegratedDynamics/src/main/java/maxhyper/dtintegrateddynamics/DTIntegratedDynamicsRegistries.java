package maxhyper.dtintegrateddynamics;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTIntegratedDynamicsRegistries {

    public static FruitBlock MENRIL_BERRY_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static void setup() {
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesIntegratedDynamics.MOD_ID, "menril_berry_fruit"), MENRIL_BERRY_FRUIT);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        Item fruitItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("integrateddynamics","menril_berries"));
        MENRIL_BERRY_FRUIT.setDroppedItem(new ItemStack(fruitItem));
        final Species treeSpecies = Species.REGISTRY.get(new ResourceLocation(DynamicTreesIntegratedDynamics.MOD_ID, "menril"));
        if (treeSpecies.isValid())
            MENRIL_BERRY_FRUIT.setSpecies(treeSpecies);
    }

}
