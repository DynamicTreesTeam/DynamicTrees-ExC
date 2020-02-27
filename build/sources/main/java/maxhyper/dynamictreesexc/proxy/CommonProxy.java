package maxhyper.dynamictreesexc.proxy;

import maxhyper.dynamictreesexc.compat.Compat;
import net.minecraftforge.fml.common.Loader;

public class CommonProxy {
	
	public void preInit() {

		if (Loader.isModLoaded("tconstruct")) {
			Compat.preInitTinkersConstruct();
		}

//		if (WorldGenRegistry.isWorldGenEnabled()) {
//			GameRegistry.registerWorldGenerator(new IWorldGenerator() {
//				@Override
//				public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
//					int blacklist = BiomeHandler.getDimBlacklist(world.provider.getDimension());
//
//					if ((blacklist == -1) && (!world.getWorldInfo().getTerrainType().getName().startsWith("flat"))) {
//						Biome bgb = world.getBiome(new BlockPos(chunkX * 16 + 8, 50, chunkZ * 16 + 8));
//						if (BiomeHandler.getBiomeBlacklist(Biome.getIdForBiome(bgb)) != -1) return;
//
//						int randPosX = chunkX * 16 + 8;
//						int randPosZ = chunkZ * 16 + 8;
//						BlockPos bp = world.getHeight(new BlockPos(randPosX, 0, randPosZ));
//
//						if (world.getBiome(bp).topBlock.getBlock() == Blocks.SAND && world.getBiome(bp).getTemperature(bp) > 1.0F && random.nextInt(30) == 0) {
//							ThaumcraftWorldGenerator.generateFlowers(world, random, bp, BlocksTC.cinderpearl, 0);
//						}
//					}
//				}
//			}, 0);
//
//			ModConfig.CONFIG_WORLD.generateTrees = false; // Disable Thaumcraft's vegetation generation
//		}
	}
	
	public void init() {
		if (Loader.isModLoaded("integrateddynamics")) {
			Compat.initIntegratedDynamics();
		}
		if (Loader.isModLoaded("tconstruct")) {
			Compat.initTinkersConstruct();
		}
		if (Loader.isModLoaded("thaumicbases")) {
			Compat.initThaumicBases();
		}
		if (Loader.isModLoaded("techreborn")) {
			Compat.initTechReborn();
		}

	}
	
	public void postInit() {
		if (Loader.isModLoaded("thaumicbases")) {
			Compat.postInitThaumicBases();
		}
	}
	
}
