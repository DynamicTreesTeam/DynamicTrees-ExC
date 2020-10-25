package maxhyper.dynamictreestheaether.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLeaves;
import com.gildedgames.the_aether.blocks.util.EnumLeafType;
import com.gildedgames.the_aether.entities.particles.ParticleGoldenOakLeaves;
import com.gildedgames.the_aether.entities.particles.ParticleHolidayLeaves;
import com.gildedgames.the_aether.items.ItemsAether;
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

public class BlockDynamicLeavesAether extends BlockDynamicLeaves {

    public BlockDynamicLeavesAether() {
        super();
        setRegistryName(DynamicTreesTheAether.MODID, "leaves0");
        //setUnlocalizedName("leaves0");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random)
    {
        if (state.getValue(BlockDynamicLeaves.TREE) == 0){
            setTickRandomly(false);
        }
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

                ParticlePortal obj;
                switch ((state.getValue(BlockDynamicLeaves.TREE))){
                    case 1:
                        obj = new ParticleGoldenOakLeaves(world, d, d1, d2, d3, d4, d5);
                        break;
                    case 2:
                    case 3:
                        obj = new ParticleHolidayLeaves(world, d, d1, d2, d3, d4, d5);
                        break;
                    default:
                        obj = null;
                }
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(obj);
            }
        }
    }


}