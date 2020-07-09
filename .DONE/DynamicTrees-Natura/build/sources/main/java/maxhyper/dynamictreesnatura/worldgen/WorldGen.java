package maxhyper.dynamictreesnatura.worldgen;

import com.progwml6.natura.common.config.Config;
import maxhyper.dynamictreesnatura.ModContent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGen implements IWorldGenerator {

    private final WorldGenerator BloodwoodSapling = new NaturaGenBloodwoodSapling();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (ModContent.generateBloodwood && shouldGenerateInDimension(world.provider.getDimension())){
            runGenerator(BloodwoodSapling, world, random, chunkX*16 + 8, chunkZ*16 + 8);
        }
    }

    private void runGenerator(WorldGenerator generator, World world, Random random, int blockX, int blockZ){
        if (random.nextInt(Config.bloodwoodSpawnRarity) == 0){
            generator.generate(world, random, new BlockPos(blockX + random.nextInt(16), 72, blockZ + random.nextInt(16)));
        }
    }

    //from natura code
    public boolean shouldGenerateInDimension(int dimension)
    {
        for (int dimensionId : Config.netherWorldGenBlacklist)
        {
            if (dimension == dimensionId)
            {
                return false;
            }
        }

        return true;
    }

}