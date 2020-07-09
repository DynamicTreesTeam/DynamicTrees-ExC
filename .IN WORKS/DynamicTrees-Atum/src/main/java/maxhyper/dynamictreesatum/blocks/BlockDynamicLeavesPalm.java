package maxhyper.dynamictreesatum.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreesatum.DynamicTreesAtum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockDynamicLeavesPalm extends BlockDynamicLeaves {

	public BlockDynamicLeavesPalm() {
		super();
		setRegistryName(DynamicTreesAtum.MODID, "leaves_palm");
		setUnlocalizedName("leaves_palm");
	}

	@Override
	public int branchSupport(IBlockState blockState, IBlockAccess blockAccess, BlockBranch branch, BlockPos pos, EnumFacing dir, int radius) {
		//Leaves are only support for "twigs"
		return radius <= 3 && branch.getFamily() == getFamily(blockState, blockAccess, pos) ? BlockBranch.setSupport(0, 1) : 0;
	}

}
