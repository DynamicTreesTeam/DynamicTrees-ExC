package maxhyper.dtquark;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilProperties;
import com.ferreusveritas.dynamictrees.resources.DTResourceRegistries;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.quark.base.module.config.type.CompoundBiomeConfig;
import vazkii.quark.content.world.config.BlossomTreeConfig;
import vazkii.quark.content.world.module.BlossomTreesModule;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesQuark.MOD_ID)
public class DynamicTreesQuark
{
    public static final String MOD_ID = "dtquark";

    public DynamicTreesQuark() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        //modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        for (BlossomTreeConfig config : BlossomTreesModule.trees.values()){
            config.biomeConfig = CompoundBiomeConfig.fromBiomeTypes(false);
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    public void gatherData(final GatherDataEvent event) {
        DTResourceRegistries.TREES_RESOURCE_MANAGER.gatherData();
        GatherDataHelper.gatherAllData(
                MOD_ID,
                event,
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY
        );
    }

}
