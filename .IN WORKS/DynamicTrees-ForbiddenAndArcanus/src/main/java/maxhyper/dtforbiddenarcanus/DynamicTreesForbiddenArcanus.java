package maxhyper.dtforbiddenarcanus;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesForbiddenArcanus.MOD_ID)
public class DynamicTreesForbiddenArcanus
{
    public static final String MOD_ID = "dtforbiddenarcanus";

    public DynamicTreesForbiddenArcanus() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTForbiddenArcanusRegistries.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Item fruit = ForgeRegistries.ITEMS.getValue(new ResourceLocation("forbidden_arcanus","cherry_peach"));
        System.out.println(fruit);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

}
