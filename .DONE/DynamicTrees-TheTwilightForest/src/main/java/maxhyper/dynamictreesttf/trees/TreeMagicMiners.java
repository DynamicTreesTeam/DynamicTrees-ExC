package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenTrunkCore;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.BlockTFMagicLog;
import twilightforest.enums.MagicWoodVariant;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class TreeMagicMiners extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:magic_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:magic_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockTFMagicLog.VARIANT, MagicWoodVariant.MINE);
	public static int logsMeta = 2;
	public static int saplingMeta = 7;

	public class SpeciesMagicMiners extends Species {

		SpeciesMagicMiners(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.minersLeavesProperties);

			setBasicGrowingParameters(tapering, 16, upProbability, 8, growthRate);

			generateSeed();
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 5, 3));
			addGenFeature(new FeatureGenTrunkCore(ModContent.minerCoreBranchOff, 7, 2));
		}

		@Override
		public Species generateSeed() {
			Seed seed = new Seed(getRegistryName().getResourcePath() + "seed"){
				@Override
				public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

					tooltip.set(0, TextFormatting.AQUA + tooltip.get(0));
					super.addInformation(stack, worldIn, tooltip, flagIn);
				}
			};
			setSeedStack(new ItemStack(seed));
			return this;
		}

		@Override
		public EnumFacing selectNewDirection(World world, BlockPos pos, BlockBranch branch, GrowSignal signal) {
			return super.selectNewDirection(world,pos,branch,signal);
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
			EnumFacing originDir = signal.dir.getOpposite();

			if (pos.getY() >= signal.rootPos.getY() + getLowestBranchHeight()  || !signal.isInTrunk()){
				probMap[EnumFacing.UP.getIndex()] = 0;
				for (EnumFacing dir: EnumFacing.HORIZONTALS){
					if (world.getBlockState(pos.offset(dir)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS)){
						probMap[dir.rotateY().getIndex()] = probMap[dir.rotateY().rotateY().rotateY().getIndex()] = 0;
					}
				}
			}
			if (!signal.isInTrunk()){
				EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);
				for (EnumFacing dir: EnumFacing.HORIZONTALS){
					probMap[dir.getIndex()] = 0;
				}
				EnumFacing[] sides = {relativePosToRoot.rotateY(), relativePosToRoot.rotateY().rotateY().rotateY(), EnumFacing.UP};
				for (EnumFacing dirSides: sides){
					if (world.isAirBlock(pos.offset(dirSides)) && world.getBlockState(pos).getValue(BlockDynamicTwilightRoots.RADIUS) > 1){
						probMap[dirSides.getIndex()] = 1;
					}
				}
				boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				boolean isBranchSide = world.getBlockState(pos.down()).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				probMap[EnumFacing.DOWN.getIndex()] = isBranchUp && !isBranchSide? 0:1;
				probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:1;
			}

			return probMap;
		}

		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			return super.newDirectionSelected(newDir, signal);
		}
	}

	public static BlockBranchTwilight branch;
	public TreeMagicMiners() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "minersTree"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.minerCoreBranch.setFamily(this);
		ModContent.minerCoreBranchOff.setFamily(this);
		ModContent.minersLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesMagicMiners(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(ModContent.minerCoreBranch);
		blockList.add(ModContent.minerCoreBranchOff);
		return super.getRegisterableBlocks( blockList);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "minersTreebranch";
		branch = new BlockBranchTwilight(branchName){
			@Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
				return false;
			}
		};
		return branch;
	}
}
