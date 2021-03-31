package maxhyper.dynamictreesttf.worldgen;

import net.minecraft.block.BlockLeaves;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import twilightforest.TFConfig;
import twilightforest.biomes.TFBiomeDecorator;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.enums.LeavesVariant;
import twilightforest.enums.WoodVariant;
import twilightforest.world.feature.TFGenCanopyMushroom;

import java.util.Random;

public class WorldGenPreDT implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == TFConfig.dimension.dimensionID){
            float canopyPerChunk = TFConfig.performance.canopyCoverage;

            WorldGenerator alternateCanopyGen = new TFGenCanopyMushroom();
            // add canopy trees
            int nc = (int) canopyPerChunk + ((random.nextFloat() < (canopyPerChunk - (int) canopyPerChunk)) ? 1 : 0);
            for (int i = 0; i < nc; i++) {
                int rx = chunkX * 16 + random.nextInt(16) + 8;
                int rz = chunkZ * 16 + random.nextInt(16) + 8;
                BlockPos genPos = new BlockPos(rx, world.getHeight(rx, rz), rz);
                Biome biome = world.getBiome(new BlockPos(rx,0,rz));
                if (canopyPerChunk > 0 && biome != TFBiomes.oakSavanna) {
                    if (((TFBiomeDecorator) biome.decorator).alternateCanopyChance > 0 && random.nextFloat() < ((TFBiomeDecorator) biome.decorator).alternateCanopyChance) {
                        alternateCanopyGen.generate(world, random, genPos);
                    }
                }
            }

            WorldGenerator shrub = new WorldGenShrub(
                    TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.OAK),
                    TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.OAK).withProperty(BlockLeaves.CHECK_DECAY, false)
            );
            for (int i = 0; i < 25; i++) {
                int rx = chunkX * 16 + random.nextInt(16) + 8;
                int rz = chunkZ * 16 + random.nextInt(16) + 8;
                BlockPos genPos = new BlockPos(rx, world.getHeight(rx, rz), rz);
                Biome biome = world.getBiome(new BlockPos(rx,0,rz));
                if (biome == TFBiomes.denseTwilightForest || biome == TFBiomes.darkForest || biome == TFBiomes.darkForestCenter) {
                    if (random.nextInt(5) == 0) {
                        shrub.generate(world, random, genPos);
                    }
                }
            }
        }
    }

}
