package maxhyper.dynamictreestbl.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thebetweenlands.common.block.terrain.*;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.List;
import java.util.Objects;

public class TreeSap extends TreeFamily {

	public static Block leavesBlock = BlockRegistry.LEAVES_SAP_TREE;
	public static Block logBlock = BlockRegistry.LOG_SAP;
	public static Block saplingBlock = BlockRegistry.SAPLING_SAP;

	public class SpeciesSap extends Species {

		SpeciesSap(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.sapLeavesProperties);

			setBasicGrowingParameters(0.6f, 15, 10, 4, 0.2f);

			generateSeed();
			addAcceptableSoils(DirtHelper.MUDLIKE);
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
			for (EnumFacing dir : EnumFacing.HORIZONTALS){
				probMap[dir.ordinal()] *= 3;
			}
			if (signal.isInTrunk()){
				probMap[EnumFacing.UP.ordinal()] = 0;
			}
			//Branching is prevented all together, only allowing radius 1 branches to expand
			if ((radius > 1) && !(signal.isInTrunk() && radius <= 2) && (signal.energy > 3)){
				for (EnumFacing dir : EnumFacing.values()){
					if (!TreeHelper.isBranch(world.getBlockState(pos.offset(dir)))){
						//minuscule chance that it branches out anyways, but if it does its short lived
						if (world.rand.nextFloat() < 0.999f){
							probMap[dir.ordinal()] = 0;
						} else {
							signal.energy = 4;
						}
					}
				}
			}

			probMap[signal.dir.getOpposite().ordinal()] = 0;
			probMap[EnumFacing.DOWN.ordinal()] = 0;

			return probMap;
		}

		private EnumFacing getRelativeFace (BlockPos signalPos, BlockPos rootPos) {
			if (signalPos.getZ() < rootPos.getZ()) {
				return EnumFacing.NORTH;
			} else if (signalPos.getZ() > rootPos.getZ()) {
				return EnumFacing.SOUTH;
			} else if (signalPos.getX() > rootPos.getX()) {
				return EnumFacing.EAST;
			} else if (signalPos.getX() < rootPos.getX()) {
				return EnumFacing.WEST;
			} else {
				return EnumFacing.UP;
			}
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
			if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.SANDLIKE))){
				return ModBlocks.blockRootySand;
			} else if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.MUDLIKE))){
				return ModContent.blockRootyMud;
			}else {
				return ModBlocks.blockRootyDirt;
			}
		}
	}

	public TreeSap() {
		super(new ResourceLocation(DynamicTreesTBL.MODID, "sap"));

		setPrimitiveLog(logBlock.getDefaultState());
		ModContent.sapLeavesProperties.setTree(this);
		
		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 4, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSap(this));
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		return super.getRegisterableBlocks(blockList);
	}

}
