package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModConfigs;
import maxhyper.dynamictreesnatura.ModContent;
import com.progwml6.natura.shared.NaturaCommons;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

public class BlockDynamicLeavesDarkwood extends BlockDynamicLeaves {

	public BlockDynamicLeavesDarkwood() {
		super();
		setRegistryName(DynamicTreesNatura.MODID, "leaves_darkwood");
		setUnlocalizedName("leaves_darkwood");
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		if (state.getValue(BlockDynamicLeaves.TREE) == 2){
			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), NaturaCommons.potashApple));
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (ModConfigs.pickFruitFromLeaves && state.getValue(TREE) == 2) {
			ItemHandlerHelper.giveItemToPlayer(player, NaturaCommons.potashApple);
			world.setBlockState(pos, state.withProperty(TREE, 0));
			return true;
		}
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
}
