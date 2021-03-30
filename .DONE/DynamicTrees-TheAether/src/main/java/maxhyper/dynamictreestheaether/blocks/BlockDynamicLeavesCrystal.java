package maxhyper.dynamictreestheaether.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.gildedgames.the_aether.entities.particles.ParticleCrystalLeaves;
import com.gildedgames.the_aether.entities.particles.ParticleGoldenOakLeaves;
import com.gildedgames.the_aether.entities.particles.ParticleHolidayLeaves;
import com.gildedgames.the_aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.DynamicTreesTheAether;
import maxhyper.dynamictreestheaether.ModConfigs;
import maxhyper.dynamictreestheaether.ModContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

public class BlockDynamicLeavesCrystal extends BlockDynamicLeaves {

    public BlockDynamicLeavesCrystal() {
        super();
        setRegistryName(DynamicTreesTheAether.MODID, "leaves_crystal");
        //setUnlocalizedName("leaves_crystal");
        //setTickRandomly(true);
        needsRandomTick = true;
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

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (ModConfigs.pickFruitFromLeaves && state.getValue(TREE) == 1) {
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ItemsAether.white_apple));
            world.setBlockState(pos, state.withProperty(TREE, 0));
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

}