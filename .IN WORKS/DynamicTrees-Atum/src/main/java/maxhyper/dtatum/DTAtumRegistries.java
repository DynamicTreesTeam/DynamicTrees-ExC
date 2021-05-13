package maxhyper.dtatum;

import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTAtumRegistries {

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        //event.registerType(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "blossom"), BlossomLeavesProperties.TYPE);
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
