package maxhyper.dynamictreestbl.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModContent;
import maxhyper.dynamictreestbl.blocks.BlockDynamicHearthgroveRoots;
import maxhyper.dynamictreestbl.genfeatures.FeatureGenUndergroundRoots;
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

public class TreeHearthgrove extends TreeFamily {

	public static Block leavesBlock = BlockRegistry.LEAVES_HEARTHGROVE_TREE;
	public static Block logBlock = BlockRegistry.LOG_HEARTHGROVE;
	public static Block saplingBlock = BlockRegistry.SAPLING_HEARTHGROVE;

	public class SpeciesSap extends Species {

		SpeciesSap(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.hearthgroveLeavesProperties);

			setBasicGrowingParameters(1f, 12, upProbability, 4, 0.2f);

			generateSeed();

			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundHearthgroveRoot, ModContent.undergroundHearthgroveRootSwamp, ModContent.hearthgroveBranch, 5,   8, 80, 6));
		}

		private EnumFacing getRelativeFace (BlockPos signalPos, BlockPos rootPos){
			if (signalPos.getZ() < rootPos.getZ()){
				return EnumFacing.NORTH;
			} else if (signalPos.getZ() > rootPos.getZ()){
				return EnumFacing.SOUTH;
			}else if (signalPos.getX() > rootPos.getX()){
				return EnumFacing.EAST;
			}else if (signalPos.getX() < rootPos.getX()){
				return EnumFacing.WEST;
			}else {
				return EnumFacing.UP;
			}
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {

			if (!signal.isInTrunk()){
				EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);
				probMap[EnumFacing.DOWN.getIndex()] = 0;
				for (EnumFacing dir: EnumFacing.HORIZONTALS){
					probMap[dir.getIndex()] = 0;
				}
				boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot)).getProperties().containsKey(BlockDynamicHearthgroveRoots.RADIUS);
				boolean isBranchSide = world.getBlockState(pos.up()).getProperties().containsKey(BlockDynamicHearthgroveRoots.RADIUS);
				probMap[EnumFacing.UP.getIndex()] = isBranchUp && !isBranchSide? 0:3;
				probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:1;
			}

			return probMap;
		}

		@Override
		public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
			return super.isAcceptableSoil(world, pos, soilBlockState)
					|| soilBlockState.getBlock() instanceof BlockSwampDirt
					|| soilBlockState.getBlock() instanceof BlockDeadGrass
					|| soilBlockState.getBlock() instanceof BlockSwampGrass
					|| soilBlockState.getBlock() instanceof BlockSludgyDirt
					|| soilBlockState.getBlock() instanceof BlockSpreadingSludgyDirt
					|| soilBlockState.getBlock() instanceof BlockMud
					|| soilBlockState.getBlock() instanceof BlockSilt
					|| (soilBlockState.getBlock() instanceof BlockCragrock && soilBlockState.getValue(BlockCragrock.VARIANT) != BlockCragrock.EnumCragrockType.DEFAULT)
					|| soilBlockState.getBlock() instanceof BlockPeat;
		}
	}

	public TreeHearthgrove() {
		super(new ResourceLocation(DynamicTreesTBL.MODID, "hearthgrove"));

		ModContent.hearthgroveLeavesProperties.setTree(this);
		
		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 4, 1);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSap(this));
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(ModContent.hearthgroveBranch);
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public BlockBranch createBranch() {
		return ModContent.hearthgroveBranch;
	}
	
}
