package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class BlockRootyNetherUpsideDown extends BlockRooty {

	static String name = "rootydirtupsidedown";

	public BlockRootyNetherUpsideDown(boolean isTileEntity) {
		this(name + (isTileEntity ? "species" : ""), isTileEntity);
	}

	public BlockRootyNetherUpsideDown(String name, boolean isTileEntity) {
		super(name, Material.GROUND, isTileEntity);
	}

	///////////////////////////////////////////
	// BLOCKSTATES
	///////////////////////////////////////////

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[]{LIFE}, new IUnlistedProperty[] {NetherMimicProperty.MIMIC});
	}

	@Override public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
		return NetherMimicProperty.getNetherMimic(access, pos);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess access, BlockPos pos) {
		return state instanceof IExtendedBlockState ? ((IExtendedBlockState)state).withProperty(NetherMimicProperty.MIMIC, getMimic(access, pos)) : state;
	}

	///////////////////////////////////////////
	// INTERACTION
	///////////////////////////////////////////

	public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
		return getMimic(access, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.DIRT);
	}

	@Override public EnumFacing getTrunkDirection(IBlockAccess access, BlockPos rootPos) {
		return EnumFacing.DOWN;
	}

	@Override public void destroyTree(World world, BlockPos rootPos) {
		Optional<BlockBranch> branch = TreeHelper.getBranchOpt(world.getBlockState(rootPos.down()));

		if(branch.isPresent()) {
			BranchDestructionData destroyData = branch.get().destroyBranchFromNode(world, rootPos.down(), EnumFacing.UP, true);
			EntityFallingTree.dropTree(world, destroyData, new ArrayList<ItemStack>(0), EntityFallingTree.DestroyType.ROOT);
		}
	}

	@Override
	public int branchSupport(IBlockState blockState, IBlockAccess blockAccess, BlockBranch branch, BlockPos pos, EnumFacing dir, int radius) {
		return dir == EnumFacing.UP ? BlockBranch.setSupport(1, 1) : 0;
	}

	///////////////////////////////////////////
	// RENDERING
	///////////////////////////////////////////

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.SOLID;
	}
	
}
