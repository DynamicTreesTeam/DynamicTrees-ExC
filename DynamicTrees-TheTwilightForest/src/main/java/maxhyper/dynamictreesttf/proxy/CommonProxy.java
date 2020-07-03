package maxhyper.dynamictreesttf.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.dropcreators.DropCreatorOtherSeed;
import maxhyper.dynamictreesttf.events.TwilightGenCancelEventHandler;
import maxhyper.dynamictreesttf.worldgen.WorldGen;
import maxhyper.dynamictreesttf.worldgen.WorldGeneratorTreesTwilight;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy {
	
	public void preInit() {
		if(WorldGenRegistry.isWorldGenEnabled()) {
			MinecraftForge.TERRAIN_GEN_BUS.register(new TwilightGenCancelEventHandler());
			GameRegistry.registerWorldGenerator(new WorldGeneratorTreesTwilight(), 20);
			GameRegistry.registerWorldGenerator(new WorldGen(), 5);
		}
	}
	
	public void init() {
	}
	
	public void postInit(){
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "robustTwilightOak")).
				addDropCreator((new DropCreatorOtherSeed(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "sicklyTwilightOak")).
						getSeedStack(1))).setRarity(0.1f));
	}
	
}
