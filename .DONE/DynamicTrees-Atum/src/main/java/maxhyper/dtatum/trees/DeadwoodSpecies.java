package maxhyper.dtatum.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;

public class DeadwoodSpecies extends Species {
    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(DeadwoodSpecies::new);

    public DeadwoodSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public boolean handleRot(IWorld world, List<BlockPos> ends, BlockPos rootPos, BlockPos treePos, int fertility, SafeChunkBounds safeBounds) {
        return false;
    }

    @Override
    protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = probMap[2] = probMap[3] = probMap[4] = probMap[5] = probMap[1] = 10;
        probMap[originDir.ordinal()] = 0; // Disable the direction we came from and front
        if (!signal.isInTrunk()){
            probMap[originDir.getOpposite().ordinal()] = 0;
        }

        return probMap;
    }

}
