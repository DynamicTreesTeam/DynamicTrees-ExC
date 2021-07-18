package maxhyper.dtquark;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlossomLeavesProperties extends LeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(BlossomLeavesProperties::new);

    public BlossomLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(AbstractBlock.Properties properties) {
        return new DynamicLeavesBlock(this, properties){
            @Override
            @OnlyIn(Dist.CLIENT)
            public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
                if (worldIn.isEmptyBlock(pos.below()) && rand.nextInt(5) == 0) {
                    double windStrength = 5.0D + Math.cos((double)worldIn.getGameTime() / 2000.0D) * 2.0D;
                    double windX = Math.cos((double)worldIn.getGameTime() / 1200.0D) * windStrength;
                    double windZ = Math.sin((double)worldIn.getGameTime() / 1000.0D) * windStrength;
                    worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, stateIn), (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, windX, -1.0D, windZ);
                }
            }
        };
    }

}
