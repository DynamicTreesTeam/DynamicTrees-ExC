package maxhyper.dynamictreestbl.models;

import com.ferreusveritas.dynamictrees.models.ModelRootyWater;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = DynamicTreesTBL.MODID, value = { Side.CLIENT })
public class ModelBakery {
	
	@SubscribeEvent
	public static void onModelBakeEvent(ModelBakeEvent event) {
		ModelResourceLocation rlWater = new ModelResourceLocation(new ResourceLocation(DynamicTreesTBL.MODID, "rootywater"), "normal");
		IBakedModel originalModelWater = event.getModelRegistry().getObject(rlWater);
		event.getModelRegistry().putObject(rlWater, new ModelRootyWater(originalModelWater));

		ModelResourceLocation rlWaterSwamp = new ModelResourceLocation(new ResourceLocation(DynamicTreesTBL.MODID, "rootywaterswamp"), "normal");
		IBakedModel originalModelWaterSwamp = event.getModelRegistry().getObject(rlWaterSwamp);
		event.getModelRegistry().putObject(rlWaterSwamp, new ModelRootyWaterTBL(originalModelWaterSwamp, "thebetweenlands:fluids/swamp_water_still", "thebetweenlands:fluids/swamp_water_flowing"));

		ModelResourceLocation rlWaterStagnant = new ModelResourceLocation(new ResourceLocation(DynamicTreesTBL.MODID, "rootywaterstagnant"), "normal");
		IBakedModel originalModelWaterStagnant = event.getModelRegistry().getObject(rlWaterStagnant);
		event.getModelRegistry().putObject(rlWaterStagnant, new ModelRootyWaterTBL(originalModelWaterStagnant, "thebetweenlands:fluids/stagnant_water_still", "thebetweenlands:fluids/stagnant_water_flowing"));

	}
	
}
