package maxhyper.dynamictreesttf.worldgen;

import net.minecraft.block.BlockLeaves;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
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

public class WorldGenPostDT implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == TFConfig.dimension.dimensionID){

            WorldGenerator mangroveTreeGen = new TFGenDynamicMangrove();
            float mangrovePerChunk = 2;
            for (int i = 0; i < mangrovePerChunk; i++) {
                int rx = chunkX * 16 + random.nextInt(16) + 8;
                int rz = chunkZ * 16 + random.nextInt(16) + 8;
                Biome biome = world.getBiome(new BlockPos(rx,0,rz));
                if (biome == TFBiomes.tfSwamp || biome == TFBiomes.fireSwamp) {
                    mangroveTreeGen.generate(world, random, new BlockPos(rx, world.getHeight(rx, rz), rz));
                }
            }
        }
    }

}
