package maxhyper.dtquark;

import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTQuarkRegistries {

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(new ResourceLocation(DynamicTreesQuark.MOD_ID, "blossom"), BlossomLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        Block glowcelium = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("quark","glowcelium"));
        DirtHelper.registerSoil(glowcelium, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(glowcelium, DirtHelper.FUNGUS_LIKE);

        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(false, DynamicTreesQuark.MOD_ID))
            event.getRegistry().register(rooty);
    }

}
