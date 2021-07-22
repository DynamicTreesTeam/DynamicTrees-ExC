package maxhyper.dtintegrateddynamics.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MenrilLogic extends GrowthLogicKit {

    public MenrilLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        return probMap;
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
        return newDir;
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy;
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return lowestBranchHeight;
    }
}
