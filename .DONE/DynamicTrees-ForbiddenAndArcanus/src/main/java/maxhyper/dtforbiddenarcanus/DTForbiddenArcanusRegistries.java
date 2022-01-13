package maxhyper.dtforbiddenarcanus;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.ShapeUtils;
import com.stal111.forbidden_arcanus.config.WorldGenConfig;
import com.stal111.forbidden_arcanus.init.world.ModConfiguredFeatures;
import maxhyper.dtforbiddenarcanus.trees.MysterywoodFamily;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DynamicTreesForbiddenArcanus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DTForbiddenArcanusRegistries {

    public static FruitBlock GOLDEN_APPLE_FRUIT = new FruitBlock().setDroppedItem(new ItemStack(Items.GOLDEN_APPLE))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock CHERRY_FRUIT = new FruitBlock()
            .setShape(1, ShapeUtils.createFruitShape(2,3,0))
            .setShape(2, ShapeUtils.createFruitShape(2.5f,4,2))
            .setShape(3, ShapeUtils.createFruitShape(3.5f,4,3))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static void setup() {
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "golden_apple_fruit"), GOLDEN_APPLE_FRUIT);
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "cherry_fruit"), CHERRY_FRUIT);
    }

    @SubscribeEvent
    public static void registerFamilyTypes(TypeRegistryEvent<Family> event) {
        event.registerType(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "mysterywood"), MysterywoodFamily.TYPE);
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {

        Item cherryFruit = ForgeRegistries.ITEMS.getValue(new ResourceLocation("forbidden_arcanus","cherry_peach"));
        if (cherryFruit != null) CHERRY_FRUIT.setDroppedItem(new ItemStack(cherryFruit));
        final Species cherry = Species.REGISTRY.get(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "cherrywood"));
        if (cherry.isValid()) CHERRY_FRUIT.setSpecies(cherry);

        final Species mystery = Species.REGISTRY.get(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "mysterywood"));
        if (mystery.isValid()) GOLDEN_APPLE_FRUIT.setSpecies(mystery);


    }

}
