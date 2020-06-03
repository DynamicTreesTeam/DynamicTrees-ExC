package maxhyper.dynamictreestheaether.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.legacy.aether.entities.particles.ParticleCrystalLeaves;
import com.legacy.aether.entities.particles.ParticleGoldenOakLeaves;
import com.legacy.aether.entities.particles.ParticleHolidayLeaves;
import com.legacy.aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.DynamicTreesTheAether;
import maxhyper.dynamictreestheaether.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockDynamicLeavesCrystal extends BlockDynamicLeaves {

    public BlockDynamicLeavesCrystal() {
        super();
        setRegistryName(DynamicTreesTheAether.MODID, "leaves_crystal");
        setUnlocalizedName("leaves_crystal");
        setTickRandomly(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random)
    {
        super.randomDisplayTick(state, world, pos, random);

        if (!world.isRemote)
        {
            return;
        }

        if (Minecraft.getMinecraft().gameSettings.particleSetting != 2)
        {
            for (int ammount = 0; ammount < 4; ammount++)
            {
                double d = pos.getX() + (random.nextFloat() - 0.5D) * 10;
                double d1 = pos.getY() + (random.nextFloat() - 0.5D) * 10;
                double d2 = pos.getZ() + (random.nextFloat() - 0.5D) * 10;
                double d3 = (random.nextFloat() - 0.5D) * 0.5D;
                double d4 = (random.nextFloat() - 0.5D) * 0.5D;
                double d5 = (random.nextFloat() - 0.5D) * 0.5D;

                ParticlePortal obj = new ParticleCrystalLeaves(world, d, d1, d2, d3, d4, d5);
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(obj);
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
        if (!worldIn.isRemote && state.getValue(BlockDynamicLeaves.TREE) == 1){
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemsAether.white_apple)));
        }
    }

    @Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (rand.nextInt(50) == 0){
            boolean isNextToFlower = false;
            for(EnumFacing dir: EnumFacing.VALUES) {
                if (worldIn.getBlockState(pos.offset(dir)).getBlock() == ModContent.crystalLeavesProperties.getDynamicLeavesState().getBlock() && worldIn.getBlockState(pos.offset(dir)).getValue(BlockDynamicLeaves.TREE) != 0){
                    isNextToFlower = true;
                }
            }
            if (state.getValue(BlockDynamicLeaves.TREE ) == 0 && state.getValue(BlockDynamicLeaves.HYDRO)== 1 && !isNextToFlower){
                worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE,1));
            }
        }
        if (rand.nextInt(100) == 0){
            if (!worldIn.isRemote && state.getValue(BlockDynamicLeaves.TREE) == 1){
                worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE,0));
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemsAether.white_apple)));
            }
        }
    }
}