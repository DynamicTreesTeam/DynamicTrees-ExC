package maxhyper.dynamictreesplants.event;

import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockRooty;
import maxhyper.dynamictreesplants.ModContent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventListenerPlants {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {

        Block[] rootyBlocks = new Block[] {ModContent.rootyNetherDirt, ModContent.rootyCrystalDirt};

        for(Block block: rootyBlocks) {
            IBakedModel rootsObject = event.getModelRegistry().getObject(new ModelResourceLocation(block.getRegistryName(), "normal"));
            if (rootsObject != null) {
                BakedModelBlockRooty rootyModel = new BakedModelBlockRooty((IBakedModel) rootsObject);
                event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "normal"), rootyModel);
            }
        }
    }

}