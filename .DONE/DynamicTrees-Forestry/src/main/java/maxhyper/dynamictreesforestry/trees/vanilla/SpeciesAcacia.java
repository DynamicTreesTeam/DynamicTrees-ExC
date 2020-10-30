package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SpeciesAcacia extends Species {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.1"));
    public static int leavesMeta = 6;

    public SpeciesAcacia(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.ACACIA), treeFamily, ModContent.acaciaLeavesProperties);

        ModContent.acaciaLeavesProperties.setTree(treeFamily);

        setBasicGrowingParameters(0.4f, 10.0f, 1, 4, 0.7f);

        setRequiresTileEntity(true);

        envFactor(Type.COLD, 0.25f);
        envFactor(Type.NETHER, 0.75f);
        envFactor(Type.WET, 0.75f);

        generateSeed();
        setupStandardSeedDropping();
    }

    @Override
    public boolean isBiomePerfect(Biome biome) {
        return BiomeDictionary.hasType(biome, Type.SAVANNA);
    }

}