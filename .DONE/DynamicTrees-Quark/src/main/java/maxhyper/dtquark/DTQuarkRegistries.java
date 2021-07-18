package maxhyper.dtquark;

import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTQuarkRegistries {

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(new ResourceLocation(DynamicTreesQuark.MOD_ID, "blossom"), BlossomLeavesProperties.TYPE);
    }

}
