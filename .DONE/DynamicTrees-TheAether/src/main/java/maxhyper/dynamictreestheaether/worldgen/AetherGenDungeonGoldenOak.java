package maxhyper.dynamictreestheaether.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.blocks.natural.BlockAetherGrass;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLeaves;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLog;
import com.gildedgames.the_aether.blocks.util.EnumLeafType;
import com.gildedgames.the_aether.blocks.util.EnumLogType;
import com.gildedgames.the_aether.world.biome.decoration.AetherGenFloatingIsland;
import maxhyper.dynamictreestheaether.DynamicTreesTheAether;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class AetherGenDungeonGoldenOak extends AetherGenFloatingIsland {

    public Species GoldenOak;

    @Override
    public boolean generate(World world, Random random, BlockPos pos)
    {
        if(world.getBlockState(pos.down()) != BlocksAether.aether_grass.getDefaultState().withProperty(BlockAetherGrass.dungeon_block, Boolean.TRUE))
            return false;

        GoldenOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether.MODID, "goldenoak"));
        GoldenOak.generate(world,pos.down(),world.getBiome(pos),random,6, SafeChunkBounds.ANY);

        return true;
    }


}
