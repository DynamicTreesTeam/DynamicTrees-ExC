package maxhyper.dynamictreestheaether2.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    }

    private void runGenerator(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, int randBound){
        if (random.nextInt(randBound) == 0){
            generator.generate(world, random, new BlockPos(chunkX+8, random.nextInt(64)+32, chunkZ+8));
        }
    }

}
