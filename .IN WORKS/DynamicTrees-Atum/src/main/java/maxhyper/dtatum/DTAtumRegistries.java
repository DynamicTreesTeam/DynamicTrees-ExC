package maxhyper.dtatum;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.models.bakedmodels.BranchBlockBakedModel;
import com.ferreusveritas.dynamictrees.models.loaders.BranchBlockModelLoader;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.teammetallurgy.atum.init.AtumBlocks;
import maxhyper.dtatum.growthlogic.DTAtumGrowthLogicKits;
import maxhyper.dtatum.leavesProperties.PalmLeavesProperties;
import maxhyper.dtatum.models.PalmLeavesBakedModel;
import maxhyper.dtatum.models.PalmLeavesModelLoader;
import maxhyper.dtatum.trees.PalmFamily;
import maxhyper.dtatum.trees.PalmSpecies;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
        event.registerType(new ResourceLocation(DynamicTreesAtum.MOD_ID, "palm"), PalmFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(new ResourceLocation(DynamicTreesAtum.MOD_ID, "palm"), PalmSpecies.TYPE);
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

        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(true, DynamicTreesAtum.MOD_ID))
            event.getRegistry().register(rooty);
    }

}
