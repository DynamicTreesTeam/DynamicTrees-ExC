package maxhyper.dynamictreesttf.worldgen;

import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import com.ferreusveritas.dynamictrees.worldgen.WorldGeneratorTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import java.util.ArrayList;
import java.util.Random;

import com.ferreusveritas.dynamictrees.api.worldgen.IGroundFinder;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import twilightforest.TFConfig;

public class WorldGeneratorTreesTwilight extends WorldGeneratorTrees {

    @Override
    public void generate(Random randomUnused, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(world.getWorldType() == WorldType.FLAT) {
            return;
        }
        if (world.provider.getDimension() == TFConfig.dimension.dimensionID){
            TreeGenerator treeGenerator = TreeGenerator.getTreeGenerator();
            BiomeDataBase dbase = treeGenerator.getBiomeDataBase(world);
            SafeChunkBounds safeBounds = new SafeChunkBounds(world, new ChunkPos(chunkX, chunkZ));//Area that is safe to place blocks during worldgen
            treeGenerator.getCircleProvider().getPoissonDiscs(world, chunkX, 0, chunkZ).forEach(c -> treeGenerator.makeTree(world, dbase, c, new GroundFinder(), safeBounds));
        }
    }
}
