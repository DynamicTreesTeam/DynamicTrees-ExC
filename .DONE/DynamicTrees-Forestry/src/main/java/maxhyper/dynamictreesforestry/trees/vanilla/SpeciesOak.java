package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.models.ModelEntityFallingTree;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesFruit;
import maxhyper.dynamictreesforestry.genfeatures.FeatureGenFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

public class SpeciesOak extends Species {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 0;

    public SpeciesOak(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.OAK), treeFamily, ModContent.oakLeavesProperties);

        ModContent.oakLeavesProperties.setTree(treeFamily);
        for (int i=0;i<4;i++) ModContent.fruitAppleLeavesProperties[i].setTree(treeFamily);
        addValidLeavesBlocks(ModContent.fruitAppleLeavesProperties);

        //Oak trees are about as average as you can get
        setBasicGrowingParameters(0.4f, 10.0f, 1, 4, 0.7f);

        setRequiresTileEntity(true);

        envFactor(Type.COLD, 0.75f);
        envFactor(Type.HOT, 0.75f);
        envFactor(Type.DRY, 0.25f);

        generateSeed();
        //setupStandardSeedDropping();

        ModContent.appleLeaves.setSpecies(getFamily().getCommonSpecies());
        addGenFeature(new FeatureGenFruitLeaves(8, 10, ModContent.oakLeavesProperties.getDynamicLeavesState(), ModContent.fruitAppleLeavesProperties[0].getDynamicLeavesState(), 0.5f));

        addGenFeature(new FeatureGenFruit(ModBlocks.blockApple).setRayDistance(4));
    }

    @Override
    public int colorTreeQuads(int defaultColor, ModelEntityFallingTree.TreeQuadData treeQuad, @Nullable EntityFallingTree entity) {
        if (treeQuad.bakedQuad.getTintIndex() == 1){
            IBlockState state = treeQuad.state;
            if (state.getBlock() instanceof BlockDynamicLeavesFruit)
                return ((BlockDynamicLeavesFruit) state.getBlock()).fruitColor(state);
        }
        return defaultColor;
    }

    @Override
    public int getSeasonalTooltipFlags(int dimension) {
        return 2;//summer
    }

    @Override
    public boolean isBiomePerfect(Biome biome) {
        return isOneOfBiomes(biome, Biomes.FOREST, Biomes.FOREST_HILLS, Biomes.PLAINS);
    }

    @Override
    public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
        if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
            if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
                world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
                world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
            }
            return true;
        }

        return false;
    }

}