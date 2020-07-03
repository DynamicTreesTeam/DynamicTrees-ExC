package maxhyper.dynamictreesttf.events;

import com.ferreusveritas.dynamictrees.ModConfigs;

import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import maxhyper.dynamictreesttf.worldgen.WorldGen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.*;

/**
 * @author ferreusveritas
 **/
public class TwilightGenCancelEventHandler {

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(PopulateChunkEvent.Populate event) {
        Biome biome = event.getWorld().getBiome(new BlockPos(event.getChunkX()*16,0,event.getChunkZ()*16));
        int dimensionId = event.getWorld().provider.getDimension();
        if(dimensionId == TFConfig.dimension.dimensionID) {
            if (biome == TFBiomes.darkForest || biome == TFBiomes.darkForestCenter){
                ((TFBiomeDecorator) biome.decorator).canopyPerChunk = 0;
            }
            ((TFBiomeDecorator) biome.decorator).hasCanopy = false;
            ((TFBiomeDecorator) biome.decorator).treesPerChunk = 0;
            ((TFBiomeDecorator) biome.decorator).mangrovesPerChunk = 0;
        }
    }
//    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
//    public void onEvent(RegistryEvent.Register<Biome> event) {
//        Biome biome = event.getRegistry().getValue(event.getName());
//        if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.getType("TWILIGHT"))) {
//            ((TFBiomeDecorator) biome.decorator).canopyPerChunk = 0;
//            ((TFBiomeDecorator) biome.decorator).hasCanopy = false;
//            ((TFBiomeDecorator) biome.decorator).treesPerChunk = 0;
//            ((TFBiomeDecorator) biome.decorator).mangrovesPerChunk = 0;
//        }
//    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onDecorateEvent(DecorateBiomeEvent.Decorate event) {
        int dimensionId = event.getWorld().provider.getDimension();
        BiomeDataBase dbase = TreeGenerator.getTreeGenerator().getBiomeDataBase(dimensionId);
        if(dimensionId == TFConfig.dimension.dimensionID) {
            Biome biome = event.getWorld().getBiome(event.getPos());
            if(dimensionId == TFConfig.dimension.dimensionID) {
                if (biome == TFBiomes.darkForest || biome == TFBiomes.darkForestCenter){
                    ((TFBiomeDecorator) biome.decorator).canopyPerChunk = 0;
                }
                ((TFBiomeDecorator) biome.decorator).hasCanopy = false;
                ((TFBiomeDecorator) biome.decorator).treesPerChunk = 0;
            }
            if (event.getType() == DecorateBiomeEvent.Decorate.EventType.TREE) {
                event.setResult(Result.DENY);
            }
        }
    }
}