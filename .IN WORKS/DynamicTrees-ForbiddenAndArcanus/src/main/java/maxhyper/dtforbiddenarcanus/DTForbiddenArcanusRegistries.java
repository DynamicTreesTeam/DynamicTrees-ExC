package maxhyper.dtforbiddenarcanus;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.systems.dropcreators.FruitDropCreator;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.families.NetherFungusFamily;
import com.ferreusveritas.dynamictrees.util.ShapeUtils;
import com.ferreusveritas.dynamictrees.util.json.JsonObjectGetters;
import com.stal111.forbidden_arcanus.init.ModBlocks;
import com.stal111.forbidden_arcanus.init.ModItems;
import maxhyper.dtforbiddenarcanus.trees.MysterywoodFamily;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
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
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

        DirtHelper.registerSoil(ModBlocks.SOULLESS_SAND.getBlock(), DirtHelper.END_LIKE);

        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(true, DynamicTreesForbiddenArcanus.MOD_ID))
            event.getRegistry().register(rooty);
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {

        Item cherryFruit = ForgeRegistries.ITEMS.getValue(new ResourceLocation("forbidden_arcanus","cherry_peach"));
        CHERRY_FRUIT.setDroppedItem(new ItemStack(cherryFruit));
        final Species cherry = Species.REGISTRY.get(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "cherrywood"));
        if (cherry.isValid()){
            cherry.addDropCreator(new FruitDropCreator().setFruitItem(cherryFruit));
            CHERRY_FRUIT.setSpecies(cherry);
        }
        final Species mystery = Species.REGISTRY.get(new ResourceLocation(DynamicTreesForbiddenArcanus.MOD_ID, "mysterywood"));
        if (mystery.isValid()){
            mystery.addDropCreator(new FruitDropCreator(0.005f).setFruitItem(Items.GOLDEN_APPLE));
            GOLDEN_APPLE_FRUIT.setSpecies(mystery);
        }

    }

}
