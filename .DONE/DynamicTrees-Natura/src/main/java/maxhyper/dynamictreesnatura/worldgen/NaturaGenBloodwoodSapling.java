package maxhyper.dynamictreesnatura.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.progwml6.natura.nether.NaturaNether;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class NaturaGenBloodwoodSapling extends WorldGenerator {

    public static Species bloodwood;

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {

        BlockPos ceiling = findCeiling(worldIn, position);
        if (worldIn.isBlockLoaded(ceiling)){
            bloodwood = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "bloodwood"));
            bloodwood.plantSapling(worldIn, ceiling);
        }

        return true;
    }

    //from natura code
    BlockPos findCeiling(World world, BlockPos pos)
    {
        int returnHeight = 0;

        int height = pos.getY();

        do
        {
            BlockPos position = new BlockPos(pos.getX(), height, pos.getZ());

            Block block = world.getBlockState(position).getBlock();

            if ((block == Blocks.NETHERRACK || block == Blocks.SOUL_SAND || block == NaturaNether.netherTaintedSoil) && !world.getBlockState(position.down()).isFullBlock())
            {
                returnHeight = height - 1;
                break;
            }

            height++;
        }
        while (height <= 120);

        return new BlockPos(pos.getX(), returnHeight, pos.getZ());
    }

}
