package maxhyper.dynamictreestbl.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
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
import thebetweenlands.common.registries.ItemRegistry;

import java.util.List;
import java.util.Objects;

public class TreeNibbletwig extends TreeFamily {

	public static Block leavesBlock = BlockRegistry.LEAVES_NIBBLETWIG_TREE;
	public static Block logBlock = BlockRegistry.LOG_NIBBLETWIG;
	public static Block saplingBlock = BlockRegistry.SAPLING_NIBBLETWIG;

	public class SpeciesSap extends Species {

		SpeciesSap(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.nibbletwigLeavesProperties);

			setBasicGrowingParameters(0.6f, 20, 10, 4, 0.2f);

			generateSeed();
			addAcceptableSoils(DirtHelper.MUDLIKE);
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
			for (EnumFacing dir : EnumFacing.HORIZONTALS){
				probMap[dir.ordinal()] *= 2;
			}
			if (signal.isInTrunk()){
				probMap[EnumFacing.UP.ordinal()] = 0;
			}
			//Branching is prevented all together, only allowing radius 1 branches to expand
			if ((radius > 1) && !(signal.isInTrunk()) && (signal.energy > 3)){
				for (EnumFacing dir : EnumFacing.values()){
					if (!TreeHelper.isBranch(world.getBlockState(pos.offset(dir)))){
						probMap[dir.ordinal()] = 0;
					}
				}
			}

			//This creates the bulb of leaves near the base of the tree
			if (signal.rootPos.getDistance(pos.getX(), signal.rootPos.getY(), pos.getZ()) <= 1 && signal.numTurns == 1){
				BlockPos centerPos = new BlockPos(signal.rootPos.getX(), pos.getY(), signal.rootPos.getZ());
				EnumFacing thisFace = getRelativeFace(pos, signal.rootPos);
				EnumFacing[] otherFaces = {thisFace.rotateY(), thisFace.getOpposite(), thisFace.rotateYCCW()};
				for (EnumFacing dir : otherFaces){
					if (TreeHelper.isBranch(world.getBlockState(centerPos.offset(dir))) && TreeHelper.getRadius(world, centerPos.offset(dir)) > 1){
						signal.energy = 1;
						break;
					}
				}

			} else {
				probMap[EnumFacing.DOWN.ordinal()] = 0;
			}
			probMap[signal.dir.getOpposite().ordinal()] = 0;

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
			} else {
				return ModBlocks.blockRootyDirt;
			}
		}

		@Override
		public float getEnergy(World world, BlockPos rootPos) {
			long day = world.getTotalWorldTime() / 24000L;
			int month = (int)day / 30;//Change the hashs every in-game month

			return signalEnergy * biomeSuitability(world, rootPos) + (CoordUtils.coordHashCode(rootPos.up(month), 2) % 10);//Vary the height energy by a psuedorandom hash function
		}
	}

	public TreeNibbletwig() {
		super(new ResourceLocation(DynamicTreesTBL.MODID, "nibbletwig"));

		setPrimitiveLog(logBlock.getDefaultState());
		setStick(new ItemStack(ItemRegistry.NIBBLESTICK));

		ModContent.nibbletwigLeavesProperties.setTree(this);
		
		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 4, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public ItemStack getStick(int qty) {
		ItemStack stack = new ItemStack(ItemRegistry.NIBBLESTICK);
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
