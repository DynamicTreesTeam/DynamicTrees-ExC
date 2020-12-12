package maxhyper.dynamictreesdefiledlands.event;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockRooty;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import lykrast.defiledlands.common.init.ModBiomes;
import lykrast.defiledlands.common.world.biome.BiomeDefiled;
import maxhyper.dynamictreesdefiledlands.ModContent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;

public class EventListenerDefiledLands {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {

        Block[] rootyBlocks = new Block[] {ModContent.rootyDefiledDirt};

        for(Block block: rootyBlocks) {
            IBakedModel rootsObject = event.getModelRegistry().getObject(new ModelResourceLocation(block.getRegistryName(), "normal"));
            if (rootsObject != null) {
                BakedModelBlockRooty rootyModel = new BakedModelBlockRooty(rootsObject);
                event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "normal"), rootyModel);
            }
        }
    }
}