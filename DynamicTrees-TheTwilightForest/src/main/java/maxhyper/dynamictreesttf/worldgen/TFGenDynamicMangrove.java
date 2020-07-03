package maxhyper.dynamictreesttf.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenDynamicMangrove extends WorldGenerator {

    public Species mangrove;

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {

        mangrove = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "mangrove"));
        int offset = 3;

        Block groundBlock = worldIn.getBlockState(position.down()).getBlock();
        if (groundBlock != Blocks.SAND && groundBlock != Blocks.GRASS && groundBlock != Blocks.WATER && groundBlock != Blocks.FLOWING_WATER &&  groundBlock != Blocks.WATERLILY && groundBlock != Blocks.DIRT && groundBlock != TFBlocks.huge_lilypad){
            return false;
        }

        worldIn.setBlockState(position.up(offset), Blocks.DIRT.getDefaultState());

        return mangrove.generate(worldIn, position.up(offset), worldIn.getBiome(position), rand, 2+rand.nextInt(7), SafeChunkBounds.ANY);

    }
}
