package maxhyper.dynamictreesttf.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.dropcreators.DropCreatorOtherSeed;
import maxhyper.dynamictreesttf.worldgen.BiomeDataBasePopulator;
import maxhyper.dynamictreesttf.worldgen.WorldGenPreDT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import twilightforest.biomes.TFBiomeDecorator;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.feature.TFGenCanopyTree;

import java.lang.reflect.Field;
import java.util.Random;


public class CommonProxy {

	public void preInit() {
		if(WorldGenRegistry.isWorldGenEnabled()) {
			GameRegistry.registerWorldGenerator(new WorldGenPreDT(), 19);
		}
	}

	public void init() {
		for (Biome biome : BiomeDataBasePopulator.TwilightBiomes){
			if (biome == TFBiomes.darkForest || biome == TFBiomes.darkForestCenter){
				((TFBiomeDecorator) biome.decorator).canopyPerChunk = 0;
			}
			((TFBiomeDecorator) biome.decorator).hasCanopy = false;
			((TFBiomeDecorator) biome.decorator).treesPerChunk = 0;
			((TFBiomeDecorator) biome.decorator).mangrovesPerChunk = 0;
			if (biome == TFBiomes.spookyForest){
				try {
					WorldGenerator nullGen = new TFGenCanopyTree(){
						@Override
						public boolean generate(World world, Random random, BlockPos pos, boolean hasLeaves) {
							return false;
						}
						@Override
						public boolean generate(World worldIn, Random rand, BlockPos position) {
							return false;
						}
					};
					Field decor = TFBiomeDecorator.class.getDeclaredField("canopyTreeGen");
					decor.setAccessible(true);
					decor.set(TFBiomes.spookyForest.decorator, nullGen);
				} catch (Exception ignored){}
			}
		}
	}
	
	public void postInit(){
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakRobust")).
				addDropCreator((new DropCreatorOtherSeed(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakSickly")).
						getSeedStack(1))).setRarity(0.5f));

	}
	
}
