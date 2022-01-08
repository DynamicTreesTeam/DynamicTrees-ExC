package maxhyper.dtatum.genfeatures;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.VineBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class PalmVinesGenFeature extends GenFeature {


    public static final ConfigurationProperty<Integer> MAX_LENGTH = ConfigurationProperty.integer("max_length");
    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");

    public PalmVinesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(BLOCK, MAX_LENGTH, QUANTITY, PLACE_CHANCE);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BLOCK, AtumBlocks.OPHIDIAN_TONGUE)
                .with(MAX_LENGTH, 5)
                .with(QUANTITY, 4)
                .with(PLACE_CHANCE, 0.1f);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        boolean placed = false;
        for (int i = 0; i < 8; i++) {
            if (!context.endPoints().isEmpty() &&
                    context.world().getRandom().nextFloat() <= configuration.get(PLACE_CHANCE)) {
                addVine(configuration, context.world(), context.pos(), context.endPoints().get(0).above(),
                        1 + context.world().getRandom().nextInt(configuration.get(MAX_LENGTH)));
                placed = true;
            }
        }
        return placed;
    }

    private void addVine(GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos,
                         BlockPos leavesPos, int length) {
        if (rootPos.getY() == leavesPos.getY()) {
            return;
        }
        Direction placeDir = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        BooleanProperty property = VineBlock.SOUTH;
        switch (placeDir) {
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
        while (length > 0) {
            if (world.isEmptyBlock(leavesPos.offset(placeDir.getNormal()))) {
                world.setBlock(leavesPos.offset(placeDir.getNormal()),
                        configuration.get(BLOCK).defaultBlockState().setValue(property, true), 2);
                leavesPos = leavesPos.below();
            }
            length--;
        }
    }

}