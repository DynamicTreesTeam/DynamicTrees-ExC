package maxhyper.dtatum.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public final class DeadGrowthLogicKit extends GrowthLogicKit {

    public DeadGrowthLogicKit(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration,
                                                 DirectionManipulationContext context) {
        final int[] probMap = context.probMap();
        final GrowSignal signal = context.signal();
        final Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = probMap[2] = probMap[3] = probMap[4] = probMap[5] = probMap[1] = 10;
        probMap[originDir.ordinal()] = 0; // Disable the direction we came from and front
        if (!signal.isInTrunk()){
            probMap[originDir.getOpposite().ordinal()] = 0;
        }

        return probMap;
    }
}
