package maxhyper.dynamictreesdefiledlands.event;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import lykrast.defiledlands.common.init.ModBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class TreeGenCancelDefiledEventHandler {

    Biome[] defiledBiomes = {
            ModBiomes.desertDefiled,
            ModBiomes.forestTenebra,
            ModBiomes.forestVilespine,
            ModBiomes.hillsDefiled,
            ModBiomes.icePlainsDefiled,
            ModBiomes.plainsDefiled,
            ModBiomes.swampDefiled
    };

    //ONLY cancels trees, as vilespine are considered cactus
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(DecorateBiomeEvent.Decorate event) {
        int dimensionId = event.getWorld().provider.getDimension();
        BiomeDataBase dbase = TreeGenerator.getTreeGenerator().getBiomeDataBase(dimensionId);
        if(dbase != TreeGenerator.DIMENSIONBLACKLISTED && !ModConfigs.dimensionBlacklist.contains(dimensionId)) {
            Biome biome = event.getWorld().getBiome(event.getPos());
            if (Arrays.asList(defiledBiomes).contains(biome) && event.getType() == DecorateBiomeEvent.Decorate.EventType.TREE) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}