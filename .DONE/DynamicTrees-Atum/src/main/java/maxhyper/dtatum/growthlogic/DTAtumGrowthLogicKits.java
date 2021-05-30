package maxhyper.dtatum.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import maxhyper.dtatum.DynamicTreesAtum;
import net.minecraft.util.ResourceLocation;

public class DTAtumGrowthLogicKits {

    public static final GrowthLogicKit PALM = new PalmGrowthLogic(new ResourceLocation(DynamicTreesAtum.MOD_ID, "palm"));
    public static final GrowthLogicKit DEADWOOD = new DeadwoodGrowthLogic(new ResourceLocation(DynamicTreesAtum.MOD_ID, "deadwood"));

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(PALM, DEADWOOD);
    }

}
