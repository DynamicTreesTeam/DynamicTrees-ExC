package maxhyper.dtneapolitan.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dtneapolitan.DTNeapolitanRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class BananaFruitGenFeature extends GenFeature implements IPostGrowFeature, IPostGenFeature {

    public static final ConfigurationProperty<FruitBlock> FRUIT_BLOCK = ConfigurationProperty.property("fruit_block", FruitBlock.class);

    public BananaFruitGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(FRUIT_BLOCK, QUANTITY, FRUITING_RADIUS, PLACE_CHANCE);
    }

    @Override
    public ConfiguredGenFeature<GenFeature> createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(FRUIT_BLOCK, DTNeapolitanRegistries.BANANA_FRUIT)
                .with(QUANTITY, 8).with(FRUITING_RADIUS, 6).with(PLACE_CHANCE, 0.25f);
    }

    private BlockPos getLeavesHeight (BlockPos rootPos, IWorld world){
        for (int y= 1; y < 20; y++){
            BlockPos testPos = rootPos.above(y);
            if ((world.getBlockState(testPos).getBlock() instanceof LeavesBlock))
                return testPos;
        }
        return rootPos;
    }

    @Override
    public boolean postGrow(ConfiguredGenFeature<?> configuredGenFeature, World world, BlockPos rootPos, BlockPos treePos, Species species, int fertility, boolean natural) {
        if((TreeHelper.getRadius(world, rootPos.above()) >= configuredGenFeature.get(FRUITING_RADIUS)) && natural && world.getRandom().nextInt() % 16 == 0) {
            if(species.seasonalFruitProductionFactor(world, rootPos) > world.getRandom().nextFloat()) {
                addFruit(configuredGenFeature, world, rootPos, getLeavesHeight(rootPos, world).below(), false);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        boolean placed = false;
        int qty = configuredGenFeature.get(QUANTITY);
        qty *= seasonFruitProductionFactor;
        for (int i=0;i<qty;i++){
            if(!endPoints.isEmpty() && world.getRandom().nextFloat() <= configuredGenFeature.get(PLACE_CHANCE)) {
                addFruit(configuredGenFeature, world, rootPos, endPoints.get(0),true);
                placed = true;
            }
        }
        return placed;
    }

    private void addFruit(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY())
            return;
        Direction placeDir = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        FruitBlock fruit = configuredGenFeature.get(FRUIT_BLOCK);
        if (world.isEmptyBlock(leavesPos.offset(placeDir.getNormal()))){
            world.setBlock(leavesPos.offset(placeDir.getNormal()), fruit.getStateForAge(worldGen?(1 + world.getRandom().nextInt(3)):0), 3);
        }
    }

}