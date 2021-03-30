package maxhyper.dynamictreesextrautils2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesextrautils2.DynamicTreesExtraUtils2;
import maxhyper.dynamictreesextrautils2.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDynamicSaplingBurntFeJuniper extends BlockDynamicSapling {

    public BlockDynamicSaplingBurntFeJuniper() {
        super(new ResourceLocation(DynamicTreesExtraUtils2.MODID,"ferrousjuniperburntsapling").toString());
        setTickRandomly(false);
    }

    @Override public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) { return false; }
    @Override public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) { return false; }
    @Override public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) { return false; }
    @Override public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) { }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(ModContent.fejuniperSeedBurnt));
    }
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModContent.fejuniperSeedBurnt);
    }

    public static boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
        Block soilBlock = soilBlockState.getBlock();
        return soilBlock == Blocks.GRASS || soilBlock == Blocks.DIRT || soilBlock instanceof BlockRooty;
    }

    public static boolean canSaplingStay(World world, Species species, BlockPos pos) {
        //Ensure there are no adjacent branches or other saplings
        for(EnumFacing dir: EnumFacing.HORIZONTALS) {
            IBlockState blockState = world.getBlockState(pos.offset(dir));
            Block block = blockState.getBlock();
            if(TreeHelper.isBranch(block) || block instanceof BlockDynamicSapling) {
                return false;
            }
        }
        Species feSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExtraUtils2.MODID, "ferrousJuniper"));
        //Air above and acceptable soil below
        return world.isAirBlock(pos.up()) && feSpecies.isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()));
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return false;
    }

    @Override
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager) {
        return false;
    }
}
