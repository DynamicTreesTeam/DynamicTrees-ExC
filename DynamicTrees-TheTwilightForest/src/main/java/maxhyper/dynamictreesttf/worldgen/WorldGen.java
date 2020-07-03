package maxhyper.dynamictreesttf.worldgen;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import twilightforest.TFConfig;
import twilightforest.biomes.TFBiomeDecorator;
import twilightforest.biomes.TFBiomes;
import twilightforest.world.feature.TFGenCanopyMushroom;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        Biome biome = world.getBiome(new BlockPos(chunkX*16,0,chunkZ*16));

        if (world.provider.getDimension() == TFConfig.dimension.dimensionID){
            if (biome == TFBiomes.tfSwamp || biome == TFBiomes.fireSwamp) {
                WorldGenerator mangroveTreeGen = new TFGenDynamicMangrove();
                float mangrovePerChunk = 2;

                for (int i = 0; i < mangrovePerChunk; i++) {
                    int rx = chunkX * 16 + random.nextInt(16) + 8;
                    int rz = chunkZ * 16 + random.nextInt(16) + 8;
                    mangroveTreeGen.generate(world, random, new BlockPos(rx, world.getHeight(rx, rz), rz));
                }
            }

            float canopyPerChunk = TFConfig.performance.canopyCoverage;
            if (canopyPerChunk > 0 && biome != TFBiomes.oakSavanna) {
                //TODO: is there a better place for this? We want to load this value once the config file is loaded.

                WorldGenerator alternateCanopyGen = new TFGenCanopyMushroom();
                // add canopy trees
                int nc = (int) canopyPerChunk + ((random.nextFloat() < (canopyPerChunk - (int) canopyPerChunk)) ? 1 : 0);
                for (int i = 0; i < nc; i++) {
                    int rx = chunkX * 16 + random.nextInt(16) + 8;
                    int rz = chunkZ * 16 + random.nextInt(16) + 8;
                    BlockPos genPos = new BlockPos(rx, world.getHeight(rx, rz), rz);
                    if (((TFBiomeDecorator) biome.decorator).alternateCanopyChance > 0 && random.nextFloat() < ((TFBiomeDecorator) biome.decorator).alternateCanopyChance) {
                        alternateCanopyGen.generate(world, random, genPos);
                    }
                }
            }
        }
    }

}
