package maxhyper.dynamictreesttf.trees.species;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenWeb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import twilightforest.biomes.TFBiomes;

public class SpeciesBirchShadow extends Species {

    public SpeciesBirchShadow(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesTTF.MODID, treeFamily.getName().getResourcePath() + "darkforest"), treeFamily, ModContent.shadowBirchLeavesProperties);

        ModContent.shadowBirchLeavesProperties.setTree(treeFamily);

        this.setBasicGrowingParameters(0.1F, 14.0F, 4, 4, 1.25F);

        this.envFactor(BiomeDictionary.Type.COLD, 0.75F);
        this.envFactor(BiomeDictionary.Type.HOT, 0.5F);
        this.envFactor(BiomeDictionary.Type.DRY, 0.5F);
        this.envFactor(BiomeDictionary.Type.FOREST, 1.05F);

        setupStandardSeedDropping();

        getFamily().addSpeciesLocationOverride((access, trunkPos) -> {
            if(access.getLight(trunkPos) < 8 && Species.isOneOfBiomes(access.getBiome(trunkPos), TFBiomes.darkForest, TFBiomes.darkForestCenter)) {
                return this;
            }
            return Species.NULLSPECIES;
        });
    }

    @Override
    public boolean showSpeciesOnWaila() {
        return false;
    }

    @Override
    public ResourceLocation getSaplingName() {
        return new ResourceLocation(ModConstants.MODID, "birch");
    }

    @Override
    public boolean isBiomePerfect(Biome biome) {
        return biome == TFBiomes.darkForest || biome == TFBiomes.darkForestCenter;
    }

    @Override
    public ItemStack getSeedStack(int qty) {
        return getFamily().getCommonSpecies().getSeedStack(qty);
    }

    @Override
    public Seed getSeed() {
        return getFamily().getCommonSpecies().getSeed();
    }

}