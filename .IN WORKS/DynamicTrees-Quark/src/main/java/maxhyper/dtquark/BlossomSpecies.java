package maxhyper.dtquark;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.specialspecies.SwampOakSpecies;
import net.minecraft.util.ResourceLocation;

public class BlossomSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(BlossomSpecies::new);

    public BlossomSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public boolean showSpeciesOnWaila() {
        return true;
    }
}
