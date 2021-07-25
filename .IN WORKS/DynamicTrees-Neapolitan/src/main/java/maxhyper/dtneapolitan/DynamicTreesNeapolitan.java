package maxhyper.dtneapolitan;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.minecraftabnormals.neapolitan.core.NeapolitanConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesNeapolitan.MOD_ID)
public class DynamicTreesNeapolitan
{
    public static final String MOD_ID = "dtneapolitan";

    public DynamicTreesNeapolitan() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTNeapolitanRegistries.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NeapolitanConfig.COMMON.bananaPlantJungleGeneration.set(false);
        NeapolitanConfig.COMMON.bananaPlantBeachGeneration.set(false);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

}
