package maxhyper.dynamictreesttf.models;

import com.ferreusveritas.dynamictrees.models.ModelRootyWater;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = DynamicTreesTTF.MODID, value = { Side.CLIENT })
public class ModelBakery {
	
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		ModelResourceLocation rl = new ModelResourceLocation(new ResourceLocation(DynamicTreesTTF.MODID, "rootywater"), "normal");
		IBakedModel originalModel = event.getModelRegistry().getObject(rl);
		event.getModelRegistry().putObject(rl, new ModelRootyWater(originalModel));
	}
	
}
