package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.IAgeable;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class BlockRootyUpsideDown extends BlockRooty {

	static String name = "rootydirtupsidedown";

	public BlockRootyUpsideDown(boolean isTileEntity) {
		this(name + (isTileEntity ? "species" : ""), isTileEntity);
	}

	public BlockRootyUpsideDown(String name, boolean isTileEntity) {
		super(name, Material.GROUND, isTileEntity);
	}
	public BlockRootyUpsideDown(String name, Material material,boolean isTileEntity) { super(name, material, isTileEntity); }

	///////////////////////////////////////////
	// BLOCKSTATES
	///////////////////////////////////////////


	@Override
	public void updateTree(IBlockState rootyState, World world, BlockPos rootPos, Random random, boolean natural) {
		super.updateTree(rootyState, world, rootPos, random, natural);

		int halfWidth = 8;
		int height = 31;
		int iterations = 1;
		SafeChunkBounds safeBounds = SafeChunkBounds.ANY;

		//Slow and dirty iteration over a cuboid volume.  Try to avoid this by using a voxmap if you can
		Iterable<BlockPos.MutableBlockPos> iterable = BlockPos.getAllInBoxMutable(rootPos.add(new BlockPos(-halfWidth, -height, -halfWidth)), rootPos.add(new BlockPos(halfWidth, 0, halfWidth)));
		for(int i = 0; i < iterations; i++) {
			for(BlockPos.MutableBlockPos iPos: iterable) {
				IBlockState blockState = world.getBlockState(iPos);
				Block block = blockState.getBlock();
				if(block instanceof IAgeable) {
					((IAgeable)block).age(world, iPos, blockState, world.rand, safeBounds);//Treat as just a regular ageable block
				}
			}
		}
	}

	public static final Material[] materialOrder = {Material.GRASS, Material.GROUND};
	@Override
	public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
		return MimicProperty.getGenericMimic(access, pos, materialOrder, DirtHelper.getSoilFlags(DirtHelper.DIRTLIKE), ModBlocks.blockStates.dirt);
	}

	///////////////////////////////////////////
	// INTERACTION
	///////////////////////////////////////////

	public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
		return getMimic(access, pos);
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
