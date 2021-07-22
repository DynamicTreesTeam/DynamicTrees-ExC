package maxhyper.dtintegrateddynamics;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dtintegrateddynamics.blocks.FilledMenrilBranchBlock;
import maxhyper.dtintegrateddynamics.growthlogic.DTIDGrowthLogicKits;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTIDRegistries {

    public static final ResourceLocation MENRIL = new ResourceLocation(DynamicTreesIntegratedDynamics.MOD_ID, "menril");

    public static FruitBlock MENRIL_BERRY_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static BranchBlock FILLED_MENRIL_BRANCH;

    public static void setup() {
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesIntegratedDynamics.MOD_ID, "menril_berry_fruit"), MENRIL_BERRY_FRUIT);
    }

    @SubscribeEvent
    public static void onGrowthLogicKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        DTIDGrowthLogicKits.register(event.getRegistry());
    }

//    @SubscribeEvent
//    public static void registerFamilyType(final TypeRegistryEvent<Family> event) {
//
//    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        Family menrilFamily = Family.REGISTRY.get(MENRIL);
        FILLED_MENRIL_BRANCH = new FilledMenrilBranchBlock(menrilFamily.getProperties());
        FILLED_MENRIL_BRANCH.setRegistryName(new ResourceLocation(DynamicTreesIntegratedDynamics.MOD_ID, "filled_menril_branch"));
        FILLED_MENRIL_BRANCH.setFamily(menrilFamily);
        event.getRegistry().register(FILLED_MENRIL_BRANCH);
        menrilFamily.addValidBranches(FILLED_MENRIL_BRANCH);
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        Item fruitItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("integrateddynamics","menril_berries"));
        MENRIL_BERRY_FRUIT.setDroppedItem(new ItemStack(fruitItem));
        final Species treeSpecies = Species.REGISTRY.get(MENRIL);
        if (treeSpecies.isValid())
            MENRIL_BERRY_FRUIT.setSpecies(treeSpecies);
    }

}
