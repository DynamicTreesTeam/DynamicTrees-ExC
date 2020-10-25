package maxhyper.dynamictreestheaether.worldgen;

import java.util.Random;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreestheaether.DynamicTreesTheAether;
import maxhyper.dynamictreestheaether.trees.ALTreeCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.world.biome.decoration.AetherGenFloatingIsland;

public class AetherGenDynamicFloatingIsland extends AetherGenFloatingIsland {

    public Species CrystalTree;

    @Override
    public boolean generate(World world, Random random, BlockPos pos)
    {
        boolean cangen = true;

        for(int x1 = pos.getX() - 6; x1 < pos.getX() + 12; x1++)
        {
            for(int y1 = pos.getY() - 6; y1 < pos.getY() + 17; y1++)
            {
                for(int z1 = pos.getZ() - 6 ; z1 < pos.getZ() + 12; z1++)
                {
                    if(world.getBlockState(new BlockPos.MutableBlockPos().setPos(x1, y1, z1)).getBlock() != Blocks.AIR)
                    {
                        cangen = false;
                    }

                }
            }
        }

        if(pos.getY() + 11 <= world.getHeight() && cangen)
        {
            for (int z = 1; z < 2; ++z)
            {
                world.setBlockState(pos.south(z), BlocksAether.holystone.getDefaultState());
            }

            for (int x = -1; x < 2; ++x)
            {
                world.setBlockState(pos.east(x), BlocksAether.holystone.getDefaultState());
            }

            BlockPos supportPos = pos.up();

            for (int z = -2; z < 3; ++z)
            {
                world.setBlockState(supportPos.south(z), BlocksAether.holystone.getDefaultState());
            }

            for (int x = -2; x < 3; ++x)
            {
                world.setBlockState(supportPos.east(x), BlocksAether.holystone.getDefaultState());
            }

            for (int x = -1; x < 2; ++x)
            {
                for (int z = 1; z > -2; --z)
                {
                    if (x != 0 || z != 0)
                    {
                        world.setBlockState(supportPos.add(x, 0, z), BlocksAether.holystone.getDefaultState());
                    }
                }
            }

            world.setBlockState(supportPos.add(1, 0, 1), BlocksAether.holystone.getDefaultState());
            world.setBlockState(supportPos.add(-1, 0, -1), BlocksAether.holystone.getDefaultState());

            BlockPos grassPos = pos.up(2);

            for (int z = -2; z < 3; ++z)
            {
                world.setBlockState(grassPos.south(z), BlocksAether.aether_grass.getDefaultState());
            }

            for (int x = -2; x < 3; ++x)
            {
                world.setBlockState(grassPos.east(x), BlocksAether.aether_grass.getDefaultState());
            }

            for (int x = -1; x < 2; ++x)
            {
                for (int z = 1; z > -2; --z)
                {
                    if (x != 0 || z != 0)
                    {
                        world.setBlockState(grassPos.add(x, 0, z), BlocksAether.aether_grass.getDefaultState());
                    }
                }
            }

            world.setBlockState(grassPos.add(1, 0, 1), BlocksAether.aether_grass.getDefaultState());
            world.setBlockState(grassPos.add(-1, 0, -1), BlocksAether.aether_grass.getDefaultState());

            CrystalTree = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether.MODID, "crystal"));
            CrystalTree.generate(world,grassPos,world.getBiome(pos),random,8, SafeChunkBounds.ANY);

            return true;
        }
        return false;
    }


}
