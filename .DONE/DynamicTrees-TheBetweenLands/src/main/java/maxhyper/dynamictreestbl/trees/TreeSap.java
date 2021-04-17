package maxhyper.dynamictreestbl.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModContent;
import maxhyper.dynamictreestbl.blocks.BlockBranchSap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
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

			setBasicGrowingParameters(0.6f, 18, 2, 4, 0.9f); //

			generateSeed();
			addDropCreator(new DropCreatorSeed(3));

			addAcceptableSoils(DirtHelper.MUDLIKE);
			addGenFeature(new FeatureGenRoots(8).setScaler((inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 8 ? (trunkRadius / 10f) : 0, 0, 1);
				return (int) (inRadius * scale);
			}));
		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}

		private boolean isNextToTrunk (BlockPos signalPos, GrowSignal signal){
			if (signal.numTurns != 1) return false;
			BlockPos rootPos = signal.rootPos;
			if (signalPos.getZ() < rootPos.getZ()){
				return  rootPos.getZ() - signalPos.getZ() == 1;
			} else if (signalPos.getZ() > rootPos.getZ()){
				return signalPos.getZ() - rootPos.getZ() == 1;
			}else if (signalPos.getX() > rootPos.getX()){
				return signalPos.getX() - rootPos.getX() == 1;
			}else if (signalPos.getX() < rootPos.getX()){
				return rootPos.getX() - signalPos.getX() == 1;
			}else {
				return false;
			}
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
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {

			if (!signal.isInTrunk()){
				signal.tapering = 0.6f;
				EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);

				for (EnumFacing dir: EnumFacing.values()){
					if (dir != EnumFacing.DOWN){
						probMap[dir.getIndex()] = TreeHelper.isBranch(world.getBlockState(pos.offset(dir)))?2:0;
					}
				}

				boolean isBranchSide = TreeHelper.isBranch(world.getBlockState(pos.offset(relativePosToRoot)));
				boolean isBranchUp = TreeHelper.isBranch(world.getBlockState(pos.up()));
				probMap[EnumFacing.UP.getIndex()] = isBranchSide && !isBranchUp? 0:2;
				probMap[relativePosToRoot.getIndex()] = isBranchUp && !isBranchSide? 0:2;

				if (isNextToTrunk(pos, signal)){
					signal.energy = 5;
					probMap[EnumFacing.UP.getIndex()] = 0;
				}

				probMap[EnumFacing.DOWN.getIndex()] = 0;

			} else {
				signal.tapering = 0.2f;

				for (EnumFacing dir: EnumFacing.HORIZONTALS){
					if ( TreeHelper.isBranch(world.getBlockState(pos.offset(dir).up(2))) || TreeHelper.isBranch(world.getBlockState(pos.offset(dir).down(2))) ){
						probMap[dir.getIndex()] = 0;
					}
				}
				probMap[EnumFacing.UP.getIndex()] += 5;
			}

			probMap[signal.dir.getOpposite().ordinal()] = 0;

			return probMap;
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

	BlockSurfaceRoot root;
	public TreeSap() {
		super(new ResourceLocation(DynamicTreesTBL.MODID, "sap"));

		setPrimitiveLog(logBlock.getDefaultState());
		ModContent.sapLeavesProperties.setTree(this);

		root = new BlockSurfaceRoot(Material.WOOD, getName() + "root");

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 4, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return root;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSap(this));
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(root);
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public BlockBranch createBranch() {
		return new BlockBranchSap(getName()+"branch");
	}
}
