package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SpeciesSpruce extends Species {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 10;

    public SpeciesSpruce(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.SPRUCE), treeFamily, ModContent.spruceLeavesProperties);

        ModContent.spruceLeavesProperties.setTree(treeFamily);

        //Spruce are conical thick slower growing trees
        setBasicGrowingParameters(0.6f, 18.0f, 1, 5, 0.9f);
        setGrowthLogicKit(new ConiferLogic(2.0f));

        setRequiresTileEntity(true);

        envFactor(Type.HOT, 0.50f);
        envFactor(Type.DRY, 0.25f);
        envFactor(Type.WET, 0.75f);

        generateSeed();
        setupStandardSeedDropping();

        //Add species features
        addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));
        //addGenFeature(new FeatureGenPodzol());
    }

    @Override
    protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
        if (signal.isInTrunk()){
            probMap[EnumFacing.UP.getIndex()] = 2;
            for (EnumFacing dir : EnumFacing.HORIZONTALS){
                probMap[dir.getIndex()] = 2;
            }
        }
        probMap[signal.dir.getOpposite().getIndex()] = 0;
        return probMap;
    }

    @Override
    public boolean isBiomePerfect(Biome biome) {
        return BiomeDictionary.hasType(biome, Type.CONIFEROUS);
    }

}