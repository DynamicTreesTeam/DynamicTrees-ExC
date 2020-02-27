package maxhyper.dynamictreesexc.worldgen;

//import com.ferreusveritas.dynamictrees.api.TreeRegistry;
//import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.EnumChance;
//import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
//import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
//import com.ferreusveritas.dynamictrees.trees.Species;
//import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
//import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
//
//import com.maxhyper.extracompat.dynamictreesextra.DynamicTreesExtra;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.biome.Biome;
//import net.minecraftforge.common.BiomeDictionary;
//import thaumcraft.common.world.biomes.BiomeHandler;
//
//public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {
//
//	private static Species greatwood, silverwood, oakMagic;
//
//	private static void createStaticAliases() {
//		greatwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExtra.MODID, "greatwood"));
//		silverwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExtra.MODID, "silverwood"));
//		oakMagic = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExtra.MODID, "oakmagic"));
//	}
//
//	@Override
//	public void populate(BiomeDataBase dbase) {
//
//		if(greatwood == null) {
//			createStaticAliases();
//		}
//
//		Biome.REGISTRY.forEach(biome -> {
//			int biomeId = Biome.getIdForBiome(biome);
//			float greatwoodChance = BiomeHandler.getBiomeSupportsGreatwood(biomeId);
//
//			if (biome != BiomeHandler.MAGICAL_FOREST && biome != BiomeHandler.ELDRITCH) {
//				RandomSpeciesSelector selector = new RandomSpeciesSelector().add(640);
//				boolean flag = false;
//
//				if (greatwoodChance > 0) {
//					selector.add(greatwood, 3).add(silverwood, 1);
//					flag = true;
//				} else if (biomeId == 18 || biomeId == 28) {
//					selector.add(silverwood, 1);
//					flag = true;
//				}
//
//				if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) flag = false;
//
//				if (flag) {
//					dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);
//					dbase.setChanceSelector(biome, (rand, species, radius) -> {
//						if ((species == greatwood || species == silverwood) && radius < 3) {
//							return EnumChance.CANCEL;
//						}
//						return EnumChance.UNHANDLED;
//					}, Operation.SPLICE_BEFORE);
//				}
//			}
//		});
//
//		dbase.setSpeciesSelector(BiomeHandler.MAGICAL_FOREST, new RandomSpeciesSelector().add(oakMagic, 40).add(greatwood, 19).add(silverwood, 1), Operation.REPLACE);
//		dbase.setDensitySelector(BiomeHandler.MAGICAL_FOREST, (rand, noiseDensity) -> ((noiseDensity * 0.25) + 0.75) * 0.5, Operation.REPLACE);
//		dbase.setChanceSelector(BiomeHandler.MAGICAL_FOREST, (rand, species, radius) -> {
//			if (radius >= 3) { // Start dropping tree spawn opportunities when the radius gets bigger than 3
//				float chance = 2.0f / (radius);
//				return rand.nextFloat() < ((Math.sqrt(chance) * 1.125f) + 0.25f) ? EnumChance.OK : EnumChance.CANCEL;
//			}
//			return EnumChance.CANCEL;
//		}, Operation.REPLACE);
//		dbase.setCancelVanillaTreeGen(BiomeHandler.MAGICAL_FOREST, true);
//	}
//
//}
