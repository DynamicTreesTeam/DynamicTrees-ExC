package maxhyper.dtquark;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.quark.base.world.config.BiomeConfig;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.content.world.config.BlossomTreeConfig;
import vazkii.quark.content.world.module.BlossomTreesModule;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesQuark.MOD_ID)
public class DynamicTreesQuark
{
    public static final String MOD_ID = "dtquark";

    public DynamicTreesQuark() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        for (BlossomTreeConfig config : BlossomTreesModule.trees.values()){
            config.biomeConfig = BiomeConfig.fromBiomeTypes(false);
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

}
