package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenLogCritter;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.BlockTFLeaves;
import twilightforest.enums.LeavesVariant;

import java.util.List;
import java.util.Objects;

public class TreeMangrove extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.MANGROVE);
	public static int logsMeta = 2;
	public static int saplingMeta = 2;

	public class SpeciesMangrove extends Species {

		SpeciesMangrove(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.mangroveLeavesProperties);

			setBasicGrowingParameters(1f, 9, upProbability, 4, 0.2f);

			setSeedStack(new ItemStack(ModContent.mangroveSeed));
			setupStandardSeedDropping();
			addGenFeature(new FeatureGenLogCritter(getLowestBranchHeight(), ModContent.dynamicFirefly, 60, 2));
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundMangroveRoot, ModContent.undergroundRootExposed, ModContent.mangroveBranch,6,   8, 40, 5));
		}

//		@Override
//		public BlockRooty getRootyBlock(World world, BlockPos pos) {
//			return ModContent.rootyDirtMangrove;
//		}

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
				boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				boolean isBranchSide = world.getBlockState(pos.up()).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				probMap[EnumFacing.UP.getIndex()] = isBranchUp && !isBranchSide? 0:3;
				probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:1;
			}

			return probMap;
		}
	}

	public TreeMangrove() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "mangrove"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.mangroveLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesMangrove(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public BlockBranch createBranch() {
		return ModContent.mangroveBranch;
	}

}
