package maxhyper.dynamictreestheaether2.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.gildedgames.aether.client.renderer.particles.ParticleGolden;
import com.gildedgames.aether.client.renderer.particles.ParticleLeaf;
import maxhyper.dynamictreestheaether2.DynamicTreesTheAether2;
import maxhyper.dynamictreestheaether2.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockDynamicLeavesAether extends BlockDynamicLeaves {

    public BlockDynamicLeavesAether(String regName) {
        super();
        setRegistryName(DynamicTreesTheAether2.MODID, regName);
        setUnlocalizedName(regName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random rand)
    {
        if (Minecraft.getMinecraft().gameSettings.particleSetting != 0)
        {
            return;
        }

        if (this == ModContent.specialLeaves && world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE) == 0) //is amberoot leaves
        {
            if (rand.nextInt(100) > 90)
            {
                final double x = pos.getX() + (rand.nextFloat() * 6f) - 3f;
                final double y = pos.getY() + (rand.nextFloat() * 6f) - 3f;
                final double z = pos.getZ() + (rand.nextFloat() * 6f) - 3f;

                final ParticleGolden effect = new ParticleGolden(world, x, y, z, 0, 0, 0);

                FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
            }
        }

        if (world.isAirBlock(pos.down()))
        {
            if (rand.nextInt(100) > 97)
            {
                final double x = pos.getX() + rand.nextFloat();
                final double y = pos.getY();
                final double z = pos.getZ() + rand.nextFloat();

                final ParticleLeaf effect = new ParticleLeaf(world, x, y, z,
                        -0.04D + (rand.nextFloat() * 0.08f),
                        -0.05D + (rand.nextFloat() * -0.02f),
                        -0.04D + (rand.nextFloat() * 0.08f),
                        getProperties(world.getBlockState(pos)).getPrimitiveLeaves().getBlock());

                FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
            }
        }

        super.randomDisplayTick(state, world, pos, rand);
    }



}