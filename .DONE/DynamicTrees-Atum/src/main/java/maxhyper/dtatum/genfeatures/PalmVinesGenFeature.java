package maxhyper.dtatum.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.GenFeatureProperty;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class PalmVinesGenFeature extends GenFeature implements IPostGenFeature {


    public static final GenFeatureProperty<Integer> MAX_LENGTH = GenFeatureProperty.createIntegerProperty("max_length");
    public static final GenFeatureProperty<Block> BLOCK = GenFeatureProperty.createBlockProperty("block");

    public PalmVinesGenFeature(ResourceLocation registryName) {
        super(registryName, BLOCK, MAX_LENGTH, QUANTITY, PLACE_CHANCE);
    }

    @Override
    public ConfiguredGenFeature<GenFeature> createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(BLOCK, AtumBlocks.OPHIDIAN_TONGUE).with(MAX_LENGTH, 5)
                .with(QUANTITY, 4).with(PLACE_CHANCE, 0.1f);
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        boolean placed = false;
        for (int i=0;i<8;i++){
            if(!endPoints.isEmpty() && world.getRandom().nextFloat() <= configuredGenFeature.get(PLACE_CHANCE)) {
                addVine(configuredGenFeature, world, rootPos, endPoints.get(0).above(),1 + world.getRandom().nextInt(configuredGenFeature.get(MAX_LENGTH)));
                placed = true;
            }
        }
        return placed;
    }

    private void addVine(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, BlockPos leavesPos, int length) {
        if (rootPos.getY() == leavesPos.getY())
            return;
        Direction placeDir = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        BooleanProperty property = VineBlock.SOUTH;
        switch (placeDir){
            case EAST:
                property = VineBlock.WEST;
                break;
            case SOUTH:
                property = VineBlock.NORTH;
                break;
            case WEST:
                property = VineBlock.EAST;
                break;
        }
        while (length > 0){
            if (world.isEmptyBlock(leavesPos.offset(placeDir.getNormal()))){
                world.setBlock(leavesPos.offset(placeDir.getNormal()), configuredGenFeature.get(BLOCK).defaultBlockState().setValue(property, true), 2);
                leavesPos = leavesPos.below();
            }
            length--;
        }
    }

}