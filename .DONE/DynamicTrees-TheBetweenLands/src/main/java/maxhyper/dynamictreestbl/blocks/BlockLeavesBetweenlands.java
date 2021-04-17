package maxhyper.dynamictreestbl.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.client.render.particle.BLParticles;
import thebetweenlands.client.render.particle.ParticleFactory;

import java.util.Random;

public class BlockLeavesBetweenlands extends BlockDynamicLeaves {

    public BlockLeavesBetweenlands() {
        super();
        setRegistryName(DynamicTreesTBL.MODID, "leaves0");
        //setUnlocalizedName("leaves0");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if(world.rand.nextInt(160) == 0) {
            if(world.isAirBlock(pos.down())) {
                BLParticles.WEEDWOOD_LEAF.spawn(world, pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat(), ParticleFactory.ParticleArgs.get().withScale(1.0F + rand.nextFloat() * 1.25F));
            }
        }
    }


}