package maxhyper.dttconstruct;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.dropcreators.FruitDropCreator;
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
public class DTTConstructRegistries {

    public static FruitBlock GREEN_SLIME_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock SKY_SLIME_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock ENDER_SLIME_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock BLOOD_SLIME_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        //event.registerType(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "blossom"), BlossomLeavesProperties.TYPE);
    }

    public static void setup() {
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "green_slime_fruit"), GREEN_SLIME_FRUIT);
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "sky_slime_fruit"), SKY_SLIME_FRUIT);
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "ender_slime_fruit"), ENDER_SLIME_FRUIT);
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "blood_slime_fruit"), BLOOD_SLIME_FRUIT);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
//        DirtHelper.createNewAdjective(SLIME_LIKE);
//
//        for (Block slimeDirt : TinkerWorld.slimeDirt.values()){
//            DirtHelper.registerSoil(slimeDirt, DirtHelper.DIRT_LIKE);
//            DirtHelper.registerSoil(slimeDirt, SLIME_LIKE);
//        }
//        for (EnumObject<SlimeType, SlimeGrassBlock> grassTypes : TinkerWorld.slimeGrass.values())
//            for (SlimeGrassBlock slimeGrass : grassTypes.values())
//                DirtHelper.registerSoil(slimeGrass, SLIME_LIKE);
//
//        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(true, DynamicTreesTConstruct.MOD_ID))
//            event.getRegistry().register(rooty);
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {

        setUpFruitSpecies(new ResourceLocation(DynamicTreesTConstruct.MOD_ID,"greenheart"),
                new ResourceLocation("slime_ball"), GREEN_SLIME_FRUIT);
        setUpFruitSpecies(new ResourceLocation(DynamicTreesTConstruct.MOD_ID,"skyroot"),
                new ResourceLocation("tconstruct","sky_slime_ball"), SKY_SLIME_FRUIT);
        setUpFruitSpecies(new ResourceLocation(DynamicTreesTConstruct.MOD_ID,"enderslime"),
                new ResourceLocation("tconstruct","ender_slime_ball"), ENDER_SLIME_FRUIT);
        setUpFruitSpecies(new ResourceLocation(DynamicTreesTConstruct.MOD_ID,"bloodshroom"),
                new ResourceLocation("tconstruct","blood_slime_ball"), BLOOD_SLIME_FRUIT);

    }

    private static void setUpFruitSpecies (ResourceLocation tree, ResourceLocation fruit, FruitBlock fruitBlock) {
//        Item fruitItem = ForgeRegistries.ITEMS.getValue(fruit);
//        fruitBlock.setDroppedItem(new ItemStack(fruitItem));
//        final Species treeSpecies = Species.REGISTRY.get(tree);
//        if (treeSpecies.isValid()){
//            treeSpecies.addDropCreator(new FruitDropCreator().setFruitItem(fruitItem));
//            fruitBlock.setSpecies(treeSpecies);
//        }
    }

}
