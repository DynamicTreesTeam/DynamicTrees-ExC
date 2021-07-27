package maxhyper.dtneapolitan;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.PalmLeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.SolidLeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.WartProperties;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.families.NetherFungusFamily;
import com.ferreusveritas.dynamictrees.util.ShapeUtils;
import maxhyper.dtneapolitan.blocks.BananaFruitBlock;
import maxhyper.dtneapolitan.blocks.BananaLeavesProperties;
import maxhyper.dtneapolitan.genfeatures.DTNeapolitanGenFeatures;
import maxhyper.dtneapolitan.trees.BananaFamily;
import maxhyper.dtneapolitan.trees.BananaSpecies;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static com.ferreusveritas.dynamictrees.util.ShapeUtils.createFruitShape;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTNeapolitanRegistries {

    public static FruitBlock BANANA_FRUIT = new BananaFruitBlock()
            .setShape(0, createFruitShape(1,3,-1, 16))
            .setShape(1, ShapeUtils.createFruitShape(2.5f,10,0,20))
            .setShape(2, ShapeUtils.createFruitShape(5f,20,0,20))
            .setShape(3, ShapeUtils.createFruitShape(5f,24,0,20))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static void setup() {
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesNeapolitan.MOD_ID, "banana_fruit"), BANANA_FRUIT);
    }

    public static final ResourceLocation BANANA = new ResourceLocation(DynamicTreesNeapolitan.MOD_ID, "banana");

    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
        event.registerType(BANANA, BananaFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerSpeciesType(final TypeRegistryEvent<Species> event) {
        event.registerType(BANANA, BananaSpecies.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(BANANA, BananaLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        DTNeapolitanGenFeatures.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        Item fruitItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("neapolitan","banana_bunch"));
        BANANA_FRUIT.setDroppedItem(new ItemStack(fruitItem));
        final Species treeSpecies = Species.REGISTRY.get(BANANA);
        if (treeSpecies.isValid())
            BANANA_FRUIT.setSpecies(treeSpecies);
    }

}
