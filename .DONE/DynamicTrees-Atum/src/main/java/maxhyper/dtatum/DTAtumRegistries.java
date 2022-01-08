package maxhyper.dtatum;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.world.gen.feature.DeadwoodFeature;
import maxhyper.dtatum.blocks.PalmFruitBlock;
import maxhyper.dtatum.genfeatures.BrokenLeavesGenFeature;
import maxhyper.dtatum.genfeatures.PalmFruitGenFeature;
import maxhyper.dtatum.genfeatures.PalmVinesGenFeature;
import maxhyper.dtatum.growthlogic.DeadGrowthLogicKit;
import maxhyper.dtatum.trees.DeadwoodSpecies;
import maxhyper.dtatum.worldgen.DeadwoodFeatureCanceller;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTAtumRegistries {

    public static GenFeature PALM_FRUIT_FEATURE;
    public static GenFeature PALM_VINES_FEATURE;
    public static GenFeature BROKEN_LEAVES_FEATURE;

    public static GrowthLogicKit DEAD_GROWTH_LOGIC_KIT = new DeadGrowthLogicKit(new ResourceLocation(DynamicTreesAtum.MOD_ID,"dead"));

    public static final FruitBlock DATE_FRUIT = new PalmFruitBlock().setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static void setup(){
        RegistryHandler.addBlock(new ResourceLocation(DynamicTreesAtum.MOD_ID,"date_fruit"), DATE_FRUIT);
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(new ResourceLocation(DynamicTreesAtum.MOD_ID, "deadwood"), DeadwoodSpecies.TYPE);
    }

    /** canceller for Atum's deadwood trees. Cancells all features of type {@link DeadwoodFeature}. */
    public static final FeatureCanceller DEADWOOD_CANCELLER = new DeadwoodFeatureCanceller<>(new ResourceLocation(DynamicTreesAtum.MOD_ID, "deadwood"));

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {
        event.getRegistry().registerAll(DEADWOOD_CANCELLER);
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        PALM_FRUIT_FEATURE = new PalmFruitGenFeature(new ResourceLocation(DynamicTreesAtum.MOD_ID,"palm_fruit"));
        PALM_VINES_FEATURE = new PalmVinesGenFeature(new ResourceLocation(DynamicTreesAtum.MOD_ID,"palm_vines"));
        BROKEN_LEAVES_FEATURE = new BrokenLeavesGenFeature(new ResourceLocation(DynamicTreesAtum.MOD_ID,"broken_leaves"));

        event.getRegistry().registerAll(PALM_FRUIT_FEATURE, PALM_VINES_FEATURE, BROKEN_LEAVES_FEATURE);
    }

    @SubscribeEvent
    public static void onRegisterGrowthLogicKits(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        event.getRegistry().registerAll(DEAD_GROWTH_LOGIC_KIT);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

        final Species palm = Species.REGISTRY.get(new ResourceLocation(DynamicTreesAtum.MOD_ID, "palm"));

        if (palm.isValid()){
            DATE_FRUIT.setSpecies(palm);
            DATE_FRUIT.setDroppedItem(new ItemStack(AtumItems.DATE));
        }

    }

}
