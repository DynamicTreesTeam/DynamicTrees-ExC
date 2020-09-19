package maxhyper.dynamictreespamtrees.trees;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.pam.redbudtree.Redbudtree;
import maxhyper.dynamictreespamtrees.DynamicTreesPamTrees;
import maxhyper.dynamictreespamtrees.ModContent;
import net.minecraft.util.ResourceLocation;

public class SpeciesRedbud extends Species {

    public SpeciesRedbud(TreeFamily treeFamily) {
        super(new ResourceLocation(DynamicTreesPamTrees.MODID, "redbud"), treeFamily, ModContent.redbudLeavesProperties);

        this.setBasicGrowingParameters(0.4F, 10.0F, 1, 4, 0.7F);

        setRequiresTileEntity(true);

        generateSeed();

        setupStandardSeedDropping();
    }

    //Just have this here for no other reason that the imports need to be in an excluded class
    public static void clearVanillaTreeSpawning(){
        if (ModConfigs.worldGen){
            ModContent.redbudSpawnNaturally = Redbudtree.spawnNaturally;
            Redbudtree.spawnNaturally = false;
        }
    }

}
