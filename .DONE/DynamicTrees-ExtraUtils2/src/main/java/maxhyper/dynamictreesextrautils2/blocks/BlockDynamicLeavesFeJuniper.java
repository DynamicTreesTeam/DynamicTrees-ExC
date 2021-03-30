package maxhyper.dynamictreesextrautils2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreesextrautils2.DynamicTreesExtraUtils2;
import maxhyper.dynamictreesextrautils2.ModContent;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockDynamicLeavesFeJuniper extends BlockDynamicLeaves {

	public BlockDynamicLeavesFeJuniper() {
		super();
		setRegistryName(DynamicTreesExtraUtils2.MODID, "leaves_ferrous_juniper");
		setUnlocalizedName("leaves_ferrous_juniper");
		setTickRandomly(true);
	}

	@Override public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 0;
	}
	@Override public boolean isFlammable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing face) { return true; }
	@Override public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return world.getBlockState(pos).getValue(BlockDynamicLeaves.TREE) == 1 ? 0 : 15;
	}
	@Override public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
		return true;
	}

	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		IBlockState blockState = worldIn.getBlockState(pos);
		if (blockState.getBlock() == ModContent.fejuniperLeaves && blockState.getValue(BlockDynamicLeaves.TREE) == 1)
			if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entityIn)) {
				entityIn.setFire(1);
				entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
			}

		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		performUpdate(worldIn, pos, state, rand);
	}
	private void performUpdate(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (state.getValue(BlockDynamicLeaves.TREE) == 0) {
			for (EnumFacing facing : EnumFacing.values()) {
				IBlockState fireState = worldIn.getBlockState(pos.offset(facing));
				if (fireState.getBlock() == Blocks.FIRE
						|| fireState.getMaterial() == Material.FIRE
						|| (fireState.getBlock() == ModContent.fejuniperLeaves && fireState.getValue(BlockDynamicLeaves.TREE) == 1)
				) {
					for (EnumFacing enumFacing : EnumFacing.values()) {
						BlockPos offset = pos.offset(enumFacing);
						IBlockState blockState = worldIn.getBlockState(offset);
						if (blockState.getBlock() == Blocks.FIRE || blockState.getBlock().isAir(blockState, worldIn, offset)) {
							worldIn.setBlockState(offset, Blocks.FIRE.getDefaultState().withProperty(BlockFire.AGE, 0), 3);
						} else if (blockState.getProperties().containsKey(BlockDynamicLeaves.TREE)) {
							for (EnumFacing enumFacing2 : EnumFacing.values()) {
								if (rand.nextBoolean()) continue;
								BlockPos offset2 = pos.offset(enumFacing2);
								IBlockState blockState2 = worldIn.getBlockState(offset2);
								if (blockState2.getBlock() == Blocks.FIRE || blockState2.getBlock().isAir(blockState2, worldIn, offset2) || blockState2.getBlock().isReplaceable(worldIn, offset2) ) {
									worldIn.setBlockState(offset2, Blocks.FIRE.getDefaultState().withProperty(BlockFire.AGE, 0), 3);
								}
							}
						}
					}

					worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE,1));

					for (EnumFacing enumFacing : EnumFacing.values()) {
						if (rand.nextBoolean()) continue;
						BlockPos offset = pos.offset(enumFacing);
						IBlockState offsetBlockState = worldIn.getBlockState(offset);
						if (offsetBlockState.getProperties().containsKey(BlockDynamicLeaves.TREE) && offsetBlockState.getValue(BlockDynamicLeaves.TREE) == 0) {
							worldIn.setBlockState(offset, offsetBlockState.withProperty(BlockDynamicLeaves.TREE, 1));
						}
					}
					return;
				}
			}
		}
	}

}
