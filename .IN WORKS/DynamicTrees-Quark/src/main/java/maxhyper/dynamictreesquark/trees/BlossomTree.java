package maxhyper.dynamictreesquark.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesquark.DynamicTreesQuark;
import maxhyper.dynamictreesquark.init.DTQuarkRegistries;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;

public class BlossomTree extends TreeFamily {

    public class BlossomSpecies extends Species {
        BlossomSpecies(ResourceLocation name, TreeFamily treeFamily) {
            super(name, treeFamily);

            ILeavesProperties leavesProperties = DTQuarkRegistries.leaves.get(name.getPath());
            leavesProperties.setTree(treeFamily);
            setLeavesProperties(leavesProperties);

            setBasicGrowingParameters(0.3f, 14.0f, 2, 4, 0.8f);

            setupStandardSeedDropping();
        }

        @Override
        public boolean showSpeciesOnWaila() { return true; }
    }

    public BlossomTree (){
        super(new ResourceLocation(DynamicTreesQuark.MOD_ID, "blossom"));
        setPrimitiveLog(Blocks.SPRUCE_LOG);
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new BlossomSpecies(new ResourceLocation(getName().getNamespace(), "sweet_"+ getName().getPath()),this));
    }
}
