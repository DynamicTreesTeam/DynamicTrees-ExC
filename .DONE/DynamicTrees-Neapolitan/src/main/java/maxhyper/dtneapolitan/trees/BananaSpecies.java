package maxhyper.dtneapolitan.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilHelper;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.species.PalmSpecies;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BananaSpecies extends PalmSpecies {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(BananaSpecies::new);

    public BananaSpecies(ResourceLocation resourceLocation, Family family, LeavesProperties leavesProperties) {
        super(resourceLocation, family, leavesProperties);
    }

    @Override
    public boolean canSaplingGrowNaturally(World world, BlockPos pos) {
        return SoilHelper.isSoilAcceptable(world.getBlockState(pos.below()),
                SoilHelper.getSoilFlags("sand_like", "gravel_like"))
                && super.canSaplingGrowNaturally(world, pos);
    }

    @Override
    public boolean canSaplingGrow(World world, BlockPos pos) {
        return world.isRainingAt(pos) && super.canSaplingGrow(world, pos);
    }
}
