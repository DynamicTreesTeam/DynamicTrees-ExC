package maxhyper.dynamictreesexc.event;

import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockRooty;

import maxhyper.dynamictreesexc.ModContent;
import maxhyper.dynamictreesexc.compat.CompatEvents;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventListenerExC {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        if (Loader.isModLoaded("tconstruct")) {
            CompatEvents.ModelBakeTinkersConstructCompat(event);
        }
    }

}