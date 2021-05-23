package maxhyper.dtatum.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.trees.Family;
import net.minecraft.util.ResourceLocation;

public class PalmFamily extends Family {
    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(PalmFamily::new);

    public PalmFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    public int getPrimaryThickness() {
        return 3;
    }

    @Override
    public int getSecondaryThickness() {
        return 3;
    }

}
