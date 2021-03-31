package maxhyper.dynamictreesttf.trees.species;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.biomes.TFBiomes;

public class SpeciesSpruceHuge extends Species {

    public SpeciesSpruceHuge(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesTTF.MODID, "hugemegaspruce"), treeFamily);
        setBasicGrowingParameters(0.4f, 30.0f, 6, 9, 2f);
        setGrowthLogicKit(new ConiferLogic(7.0f));

        setSoilLongevity(32);//Grows for a while so it can actually get tall

        addGenFeature(new FeatureGenClearVolume(25));//Clear a spot for the thick tree trunk
        addGenFeature(new FeatureGenMound(999));//Place a 3x3 of dirt under thick trees

        Species megaspruce = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "megaspruce"));
        megaspruce.setMegaSpecies(this);

        getFamily().addSpeciesLocationOverride((access, trunkPos) -> {
            if(isTwilightHugeMegaBiome(access, trunkPos)) {
                return this;
            } else if(Species.isOneOfBiomes(access.getBiome(trunkPos), TFBiomes.highlands, TFBiomes.highlandsCenter)) {
                return TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "megaspruce"));
            }
            return Species.NULLSPECIES;
        });

        addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));
    }

    private boolean isTwilightHugeMegaBiome (World access, BlockPos trunkPos) {
        return Species.isOneOfBiomes(access.getBiome(trunkPos), TFBiomes.snowy_forest);
    }

    @Override
    public boolean getRequiresTileEntity(World world, BlockPos pos) {
        return !isTwilightHugeMegaBiome(world, pos);
    }

    @Override
    public boolean isMega() {
        return true;
    }

    @Override
    public ResourceLocation getSaplingName() {
        return new ResourceLocation(ModConstants.MODID, "spruce");
    }

    @Override
    public ItemStack getSeedStack(int qty) {
        return getFamily().getCommonSpecies().getSeedStack(qty);
    }

    @Override
    public Seed getSeed() {
        return getFamily().getCommonSpecies().getSeed();
    }

    @Override
    public int maxBranchRadius() {
        return 24;
    }

    @Override
    public boolean isThick() {
        return true;
    }

}