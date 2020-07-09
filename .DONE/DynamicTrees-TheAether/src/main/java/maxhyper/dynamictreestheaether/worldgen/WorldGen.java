package maxhyper.dynamictreestheaether.worldgen;

import com.legacy.aether.Aether;
import com.legacy.aether.AetherConfig;
import com.legacy.aether.world.AetherWorld;
import com.legacy.aether.world.biome.AetherBiome;
import com.legacy.aether.world.biome.AetherBiomeDecorator;
import com.legacy.aether.world.biome.decoration.AetherGenFloatingIsland;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

    private final WorldGenerator CrystalIsland = new AetherGenDynamicFloatingIsland();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == AetherConfig.dimension.aether_dimension_id){
            runGenerator(CrystalIsland, world, random, chunkX*16, chunkZ*16, 37);
        }
    }

    private void runGenerator(WorldGenerator generator, World world, Random random, int chunkX, int chunkZ, int randBound){
        if (random.nextInt(randBound) == 0){
            generator.generate(world, random, new BlockPos(chunkX+8, random.nextInt(64)+32, chunkZ+8));
        }
    }

}
