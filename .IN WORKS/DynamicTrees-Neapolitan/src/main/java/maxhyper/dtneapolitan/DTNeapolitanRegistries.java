package maxhyper.dtneapolitan;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dtneapolitan.trees.BananaSpecies;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTNeapolitanRegistries {

    public static FruitBlock BANANA_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static void setup() {
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesNeapolitan.MOD_ID, "banana_fruit"), BANANA_FRUIT);
    }

    @SubscribeEvent
    public static void registerSpeciesType(final TypeRegistryEvent<Species> event) {
        event.registerType(new ResourceLocation(DynamicTreesNeapolitan.MOD_ID, "banana"), BananaSpecies.TYPE);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        Item fruitItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("neapolitan","banana_bunch"));
        BANANA_FRUIT.setDroppedItem(new ItemStack(fruitItem));
        final Species treeSpecies = Species.REGISTRY.get(new ResourceLocation(DynamicTreesNeapolitan.MOD_ID, "banana_palm"));
        if (treeSpecies.isValid())
            BANANA_FRUIT.setSpecies(treeSpecies);
    }

}
