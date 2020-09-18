package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Random;

public class SpeciesBirch extends Species {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 2;

    public SpeciesBirch(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.BIRCH), treeFamily, ModContent.birchLeavesProperties);

        ModContent.birchLeavesProperties.setTree(treeFamily);

        //Birch are tall, skinny, fast growing trees
        setBasicGrowingParameters(0.1f, 14.0f, 4, 4, 1.25f);

        setRequiresTileEntity(true);

        envFactor(Type.COLD, 0.75f);
        envFactor(Type.HOT, 0.50f);
        envFactor(Type.DRY, 0.50f);
        envFactor(Type.FOREST, 1.05f);

        generateSeed();
        setupStandardSeedDropping();
    }

    @Override
    public boolean isBiomePerfect(Biome biome) {
        return isOneOfBiomes(biome, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS);
    };

    @Override
    public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
        if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
            if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
                world.setBlockState(pos, Blocks.BROWN_MUSHROOM.getDefaultState());//Change branch to a brown mushroom
                world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 3);//Change rooty dirt to dirt
            }
            return true;
        }

        return false;
    }

}