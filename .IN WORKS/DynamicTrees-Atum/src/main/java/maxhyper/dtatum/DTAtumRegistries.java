package maxhyper.dtatum;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.systems.dropcreators.FruitDropCreator;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.cancellers.MushroomFeatureCanceller;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.world.gen.feature.DeadwoodFeature;
import maxhyper.dtatum.growthlogic.DTAtumGrowthLogicKits;
import maxhyper.dtatum.leavesProperties.PalmLeavesProperties;
import maxhyper.dtatum.trees.PalmSpecies;
import maxhyper.dtatum.worldgen.DeadwoodFeatureCanceller;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.BigMushroomFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTAtumRegistries {

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(new ResourceLocation(DynamicTreesAtum.MOD_ID, "palm"), PalmLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(new ResourceLocation(DynamicTreesAtum.MOD_ID, "palm"), PalmSpecies.TYPE);
    }

    /** canceller for Atum's deadwood trees. Cancells all features of type {@link DeadwoodFeature}. */
    public static final FeatureCanceller DEADWOOD_CANCELLER = new DeadwoodFeatureCanceller<>(new ResourceLocation(DynamicTreesAtum.MOD_ID, "deadwood"));

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {
        event.getRegistry().registerAll(DEADWOOD_CANCELLER);
    }

    @SubscribeEvent
    public static void onGrowthLogicKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        DTAtumGrowthLogicKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

        DirtHelper.registerSoil(AtumBlocks.SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(AtumBlocks.FERTILE_SOIL, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(AtumBlocks.FERTILE_SOIL, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(AtumBlocks.FERTILE_SOIL_TILLED, DirtHelper.DIRT_LIKE, AtumBlocks.FERTILE_SOIL);
        DirtHelper.registerSoil(AtumBlocks.LIMESTONE_GRAVEL, DirtHelper.GRAVEL_LIKE);
        DirtHelper.registerSoil(AtumBlocks.MARL, DirtHelper.MUD_LIKE);

        final Species palm = Species.REGISTRY.get(new ResourceLocation(DynamicTreesAtum.MOD_ID, "palm"));

        if (palm.isValid()){
            palm.addDropCreator(new FruitDropCreator().setFruitItem(AtumItems.DATE));
        }

        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(true, DynamicTreesAtum.MOD_ID))
            event.getRegistry().register(rooty);
    }

}
