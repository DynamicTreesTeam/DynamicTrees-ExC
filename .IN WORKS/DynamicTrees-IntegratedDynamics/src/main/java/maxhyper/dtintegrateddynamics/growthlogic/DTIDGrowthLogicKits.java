package maxhyper.dtintegrateddynamics.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import maxhyper.dtintegrateddynamics.DTIDRegistries;

public class DTIDGrowthLogicKits {

    public static final GrowthLogicKit MENRIL = new MenrilLogic(DTIDRegistries.MENRIL);

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(MENRIL);
    }

}
