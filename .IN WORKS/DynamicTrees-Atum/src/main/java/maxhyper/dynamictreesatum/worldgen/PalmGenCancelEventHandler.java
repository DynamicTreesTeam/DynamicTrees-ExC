package maxhyper.dynamictreesatum.worldgen;

import com.teammetallurgy.atum.world.biome.BiomeDeadOasis;
import com.teammetallurgy.atum.world.biome.BiomeOasis;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PalmGenCancelEventHandler {

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onDecorateEvent(DecorateBiomeEvent.Decorate event){
        Biome biome = event.getWorld().getBiome(event.getPos());
        if (biome instanceof BiomeOasis || biome instanceof BiomeDeadOasis){
            event.setResult(Event.Result.DENY);
        }
    }

}
