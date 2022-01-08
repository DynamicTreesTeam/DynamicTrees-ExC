package maxhyper.dtatum.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import maxhyper.dtatum.DTAtumRegistries;
import maxhyper.dtatum.blocks.PalmFruitBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PalmFruitGenFeature extends GenFeature {

    public static final ConfigurationProperty<FruitBlock> FRUIT_BLOCK =
            ConfigurationProperty.property("fruit_block", FruitBlock.class);

    public PalmFruitGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(FRUIT_BLOCK, QUANTITY, FRUITING_RADIUS, PLACE_CHANCE);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(FRUIT_BLOCK, DTAtumRegistries.DATE_FRUIT)
                .with(QUANTITY, 8)
                .with(FRUITING_RADIUS, 6)
                .with(PLACE_CHANCE, 0.25f);
    }

    private BlockPos getLeavesHeight(BlockPos rootPos, IWorld world) {
        for (int y = 1; y < 20; y++) {
            BlockPos testPos = rootPos.above(y);
            if ((world.getBlockState(testPos).getBlock() instanceof LeavesBlock)) {
                return testPos;
            }
        }
        return rootPos;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        boolean placed = false;
        int qty = configuration.get(QUANTITY);
        qty *= context.fruitProductionFactor();
        for (int i = 0; i < qty; i++) {
            if (!context.endPoints().isEmpty() && context.world().getRandom().nextFloat() <= configuration.get(PLACE_CHANCE)) {
                addFruit(configuration, context.world(), context.pos(), context.endPoints().get(0), true);
                placed = true;
            }
        }
        return placed;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        World world = context.world();
        BlockPos rootPos = context.pos();
        if (TreeHelper.getRadius(world, rootPos.above()) >= configuration.get(FRUITING_RADIUS) &&
                context.natural() &&
                world.getRandom().nextInt() % 16 == 0 &&
                context.species().seasonalFruitProductionFactor(world, rootPos) > world.getRandom().nextFloat()) {
            addFruit(configuration, world, rootPos, getLeavesHeight(rootPos, world).below(), false);
            return true;
        }
        return false;
    }

    private void addFruit(GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos,
                          BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()) {
            return;
        }
        Direction placeDir = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        FruitBlock fruit = configuration.get(FRUIT_BLOCK);
        if (world.isEmptyBlock(leavesPos.offset(placeDir.getNormal())) && fruit instanceof PalmFruitBlock) {
            world.setBlock(leavesPos.offset(placeDir.getNormal()),
                    ((PalmFruitBlock) fruit).getStateForAgeAndDir(worldGen ? (1 + world.getRandom().nextInt(3)) : 0,
                            placeDir.getOpposite()), 3);
        }
    }

}