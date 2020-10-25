package maxhyper.dynamictreestheaether.worldgen;

import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.world.biome.decoration.AetherGenGoldenIsland;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

    private final WorldGenerator CrystalIsland = new AetherGenDynamicFloatingIsland();
    private final WorldGenerator DungeonGoldenOak = new AetherGenDungeonGoldenOak();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.getDimension() == AetherConfig.dimension.aether_dimension_id){
            if (random.nextInt(37) == 0)
                CrystalIsland.generate(world, random, new BlockPos(chunkX*16 + 8, random.nextInt(64) + 32, chunkZ*16 + 8));
            for (int i = 0; i < 20; i++) {
                DungeonGoldenOak.generate(world, random, world.getHeight(new BlockPos(chunkX*16 + random.nextInt(16), 0, chunkZ*16 + random.nextInt(16))));
            }
        }
    }
}
