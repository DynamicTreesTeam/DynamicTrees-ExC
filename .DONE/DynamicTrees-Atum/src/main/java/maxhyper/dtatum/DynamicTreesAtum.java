package maxhyper.dtatum;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.init.DTRegistries;
import com.ferreusveritas.dynamictrees.items.DendroPotion;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.DendroBrewingRecipe;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesAtum.MOD_ID)
public class DynamicTreesAtum
{
    public static final String MOD_ID = "dtatum";

    public DynamicTreesAtum() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTAtumRegistries.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        final Species deadwood = Species.REGISTRY.get(new ResourceLocation(DynamicTreesAtum.MOD_ID, "deadwood"));

        if (deadwood.isValid()){
            ItemStack transformationPotion = DTRegistries.DENDRO_POTION.applyIndexTag(new ItemStack(DTRegistries.DENDRO_POTION), (DendroPotion.DendroPotionType.TRANSFORM).getIndex());
            BrewingRecipeRegistry.addRecipe(new DendroBrewingRecipe(transformationPotion, new ItemStack(AtumItems.DEADWOOD_STICK),
                    DTRegistries.DENDRO_POTION.setTargetSpecies(transformationPotion, deadwood)));
        }

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

}
