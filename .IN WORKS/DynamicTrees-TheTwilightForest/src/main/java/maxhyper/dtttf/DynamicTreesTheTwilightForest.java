package maxhyper.dtttf;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesTheTwilightForest.MOD_ID)
public class DynamicTreesTheTwilightForest
{
    public static final String MOD_ID = "dtttf";

    public DynamicTreesTheTwilightForest() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

}
