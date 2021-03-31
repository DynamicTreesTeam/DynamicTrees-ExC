package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeDarkwood extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:dark_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getDefaultState();
	public static int logsMeta = 3;
	public static int saplingMeta = 3;

	public static int darkForestHeight = 8;

	public class SpeciesDarkwood extends Species {

		SpeciesDarkwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.darkwoodLeavesProperties);

			setBasicGrowingParameters(0.2f, 10, 2, 3, growthRate);

			generateSeed();
			setupStandardSeedDropping();

			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 5, 3));

		}

		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month

			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos.up(month)) % 16); // Vary the height energy by a psuedorandom hash function
		}
		@Override
		public int getLowestBranchHeight(World world, BlockPos pos) {
			long day = world.getWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month

			return (int) ((getLowestBranchHeight() + ((coordHashCode(pos.up(month)) % 16) * 0.1f)) * biomeSuitability(world, pos));
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
			if (pos.getY()-signal.rootPos.getY() > darkForestHeight){
				signal.energy = 2;
			}

			if (signal.isInTrunk() && pos.getY() > signal.rootPos.getY() + getLowestBranchHeight() +1){
				probMap[EnumFacing.UP.getIndex()] = 0;
			}
			if (!signal.isInTrunk()){
				EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);
				if (signal.energy > 3){
					probMap[EnumFacing.DOWN.getIndex()] = 0;
					for (EnumFacing dir: EnumFacing.HORIZONTALS){
						probMap[dir.getIndex()] = 0;
					}
				}
				boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				boolean isBranchSide = world.getBlockState(pos.up()).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				probMap[EnumFacing.UP.getIndex()] = isBranchUp && !isBranchSide? 0:2;
				probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:1;
			}

			return probMap;
		}

	}

	public static BlockDynamicLeaves darkwoodLeaves;
	public TreeDarkwood() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "darkwood"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		darkwoodLeaves = new BlockDynamicLeaves(){
			@Override
			public boolean isOpaqueCube(IBlockState state) {
				return true;
			}
		};
		darkwoodLeaves.setRegistryName("leaves_darkwood");

		ModContent.darkwoodLeavesProperties.setTree(this);
		ModContent.darkwoodLeavesProperties.setDynamicLeavesState(darkwoodLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		darkwoodLeaves.setProperties(0, ModContent.darkwoodLeavesProperties);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, logsMeta);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesDarkwood(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(darkwoodLeaves);
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "darkwoodbranch";
		return new BlockBranchTwilight(branchName);
	}

}
