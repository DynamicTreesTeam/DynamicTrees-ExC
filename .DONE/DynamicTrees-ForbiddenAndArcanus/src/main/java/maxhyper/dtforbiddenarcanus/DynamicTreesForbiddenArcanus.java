package maxhyper.dtforbiddenarcanus;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.stal111.forbidden_arcanus.config.WorldGenConfig;
import com.stal111.forbidden_arcanus.init.world.ModConfiguredFeatures;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesForbiddenArcanus.MOD_ID)
public class DynamicTreesForbiddenArcanus {
    public static final String MOD_ID = "dtforbiddenarcanus";

    public DynamicTreesForbiddenArcanus() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTForbiddenArcanusRegistries.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        WorldGenConfig.CHERRYWOOD_TREE_GENERATE.set(false);
        WorldGenConfig.MYSTERYWOOD_TREE_GENERATE.set(false);
    }

    private void clientSetup(final FMLClientSetupEvent event) { }

}
