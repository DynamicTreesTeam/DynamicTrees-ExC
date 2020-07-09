package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import com.progwml6.natura.shared.NaturaCommons;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDynamicLeavesDarkwood extends BlockDynamicLeaves {

	public BlockDynamicLeavesDarkwood() {
		super();
		setRegistryName(DynamicTreesNatura.MODID, "leaves_darkwood");
		setUnlocalizedName("leaves_darkwood");
		setTickRandomly(true);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		if (state.getValue(BlockDynamicLeaves.TREE) == 2){
			worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), NaturaCommons.potashApple));
		}
	}

	@Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		if (rand.nextInt(1000) == 0){
			boolean isNextToFlower = false;
			for(EnumFacing dir: EnumFacing.VALUES) {
				if (worldIn.getBlockState(pos.offset(dir)).getBlock() == ModContent.darkwoodLeaves && worldIn.getBlockState(pos.offset(dir)).getValue(BlockDynamicLeaves.TREE) != 0){
					isNextToFlower = true;
				}
			}
			if (state.getValue(BlockDynamicLeaves.TREE ) == 0 && !isNextToFlower){
				worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE,1));
			}
			if (state.getValue(BlockDynamicLeaves.TREE) == 1){
				worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE,2));
			}
		}
		if (rand.nextInt(1000) == 0){
			if (state.getValue(BlockDynamicLeaves.TREE) == 2){
				worldIn.setBlockState(pos, state.withProperty(BlockDynamicLeaves.TREE,0));
				//WorldUtils.dropItem(NaturaCommons.potashApple, worldIn, pos);
			}
		}
	}
}
