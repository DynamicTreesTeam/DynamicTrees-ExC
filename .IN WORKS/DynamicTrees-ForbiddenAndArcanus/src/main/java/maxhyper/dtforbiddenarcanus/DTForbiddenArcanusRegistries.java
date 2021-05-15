package maxhyper.dtforbiddenarcanus;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.dropcreators.FruitDropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.stal111.forbidden_arcanus.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTForbiddenArcanusRegistries {

    public static FruitBlock GOLDEN_APPLE_FRUIT = new FruitBlock().setDroppedItem(new ItemStack(Items.GOLDEN_APPLE))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock CHERRY_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static void setup() {
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "golden_apple_fruit"), GOLDEN_APPLE_FRUIT);
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "cherry_fruit"), CHERRY_FRUIT);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        //event.registerType(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "blossom"), BlossomLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

        final Species cherry = Species.REGISTRY.get(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "cherrywood"));

        if (cherry.isValid()){
            //cherry.addDropCreator(new FruitDropCreator().setFruitItem());
            CHERRY_FRUIT.setSpecies(cherry);
        }
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
    }

}
