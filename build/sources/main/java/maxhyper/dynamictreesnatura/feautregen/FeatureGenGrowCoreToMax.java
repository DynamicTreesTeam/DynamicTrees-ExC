package maxhyper.dynamictreesnatura.feautregen;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class FeatureGenGrowCoreToMax implements IPostGenFeature, IPostGrowFeature {

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {
        if(soilLife > 0) {
            growCoreToMax(world, rootPos, species);
            return true;
        }
        return false;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        growCoreToMax(world, rootPos, species);
        return true;
    }

    public void growCoreToMax(World world, BlockPos rootPos, Species species) {
        TreeFamily family = species.getFamily();

        int radius1 = TreeHelper.getRadius(world, rootPos.up(1));

        if (radius1 >= 4){
            family.getDynamicBranch().setRadius(world, rootPos.up(3), Math.min(radius1, family.getDynamicBranch().getMaxRadius()-1), EnumFacing.UP);
            family.getDynamicBranch().setRadius(world, rootPos.up(2), Math.min(radius1, family.getDynamicBranch().getMaxRadius()), EnumFacing.UP);
            family.getDynamicBranch().setRadius(world, rootPos.up(1), Math.min(radius1 + 1, family.getDynamicBranch().getMaxRadius()), EnumFacing.UP);
        }

    }

}