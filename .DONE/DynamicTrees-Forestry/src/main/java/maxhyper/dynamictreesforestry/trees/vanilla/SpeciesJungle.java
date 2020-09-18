package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenCocoa;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenUndergrowth;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
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

public class SpeciesJungle extends Species {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 15;

    public SpeciesJungle(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.JUNGLE), treeFamily, ModContent.jungleLeavesProperties);

        ModContent.jungleLeavesProperties.setTree(treeFamily);

        //Jungle Trees are tall, wildly growing, fast growing trees with low branches to provide inconvenient obstruction and climbing
        setBasicGrowingParameters(0.2f, 28.0f, 3, 2, 1.0f);
        setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.JUNGLE));

        setRequiresTileEntity(true);

        envFactor(Type.COLD, 0.15f);
        envFactor(Type.DRY,  0.20f);
        envFactor(Type.HOT, 1.1f);
        envFactor(Type.WET, 1.1f);

        generateSeed();
        setupStandardSeedDropping();

        //Add species features
        addGenFeature(new FeatureGenCocoa());
        addGenFeature(new FeatureGenVine().setQuantity(16).setMaxLength(16));
        addGenFeature(new FeatureGenUndergrowth());
    }

    @Override
    public boolean isBiomePerfect(Biome biome) {
        return BiomeDictionary.hasType(biome, Type.JUNGLE);
    };

}