package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenLogCritter;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenTrunkCore;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
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
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFMagicLog;
import twilightforest.client.particle.TFParticleType;
import twilightforest.enums.MagicWoodVariant;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeMagicTransformation extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:magic_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:magic_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockTFMagicLog.VARIANT, MagicWoodVariant.TRANS);
	public static int logsMeta = 1;
	public static int saplingMeta = 6;

	public class SpeciesMagicTransformation extends Species {

		SpeciesMagicTransformation(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.transformationLeavesProperties);

			setBasicGrowingParameters(0.5f, 16, upProbability, lowestBranchHeight, growthRate);

			generateSeed();
			addGenFeature(new FeatureGenLogCritter(getLowestBranchHeight(), ModContent.dynamicFirefly, 60, 2));
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 5, 3));
			addGenFeature(new FeatureGenTrunkCore(ModContent.transformCoreBranchOff, 7, 4));
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

			if (signal.isInTrunk() && pos.getY() > signal.rootPos.getY() + getLowestBranchHeight() +1){
				probMap[EnumFacing.UP.getIndex()] = 0;
			}
			if (!signal.isInTrunk()){
				EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);
				if (signal.energy > 2){
					probMap[EnumFacing.DOWN.getIndex()] = 0;
					for (EnumFacing dir: EnumFacing.HORIZONTALS){
						probMap[dir.getIndex()] = 0;
					}
				}
				boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot)).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				boolean isBranchSide = world.getBlockState(pos.up()).getProperties().containsKey(BlockDynamicTwilightRoots.RADIUS);
				probMap[EnumFacing.UP.getIndex()] = isBranchUp && !isBranchSide? 0:3;
				probMap[relativePosToRoot.getIndex()] = isBranchSide && !isBranchUp? 0:1;
			}

			return probMap;
		}
	}

	public static BlockBranchTwilight branch;
	public static BlockDynamicLeaves transformationLeaves;
	public TreeMagicTransformation() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "treeOfTransformation"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		transformationLeaves = new BlockDynamicLeaves(){

			@Override
			public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
				for (int i = 0; i < 1; ++i) {
					this.sparkleRunes(world, pos, random);
				}
			}
			private void sparkleRunes(World world, BlockPos pos, Random rand) {
				double offset = 0.0625D;

				EnumFacing side = EnumFacing.random(rand);
				double rx = pos.getX() + rand.nextFloat();
				double ry = pos.getY() + rand.nextFloat();
				double rz = pos.getZ() + rand.nextFloat();

				if (side == EnumFacing.DOWN && world.isAirBlock(pos.up())) {
					ry = pos.getY() + 1 + offset;
				}

				if (side == EnumFacing.UP && world.isAirBlock(pos.down())) {
					ry = pos.getY() - offset;
				}

				if (side == EnumFacing.NORTH && world.isAirBlock(pos.south())) {
					rz = pos.getZ() + 1 + offset;
				}

				if (side == EnumFacing.SOUTH && world.isAirBlock(pos.north())) {
					rz = pos.getZ() - offset;
				}

				if (side == EnumFacing.WEST && world.isAirBlock(pos.east())) {
					rx = pos.getX() + 1 + offset;
				}

				if (side == EnumFacing.EAST && world.isAirBlock(pos.west())) {
					rx = pos.getX() - offset;
				}

				if (rx < pos.getX() || rx > pos.getX() + 1 || ry < pos.getY() || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
					TwilightForestMod.proxy.spawnParticle(TFParticleType.LEAF_RUNE, rx, ry, rz, 0.0D, 0.0D, 0.0D);
				}
			}
		};
		transformationLeaves.setRegistryName("leaves_of_transformation");

		ModContent.transformCoreBranch.setFamily(this);
		ModContent.transformCoreBranchOff.setFamily(this);
		ModContent.transformationLeavesProperties.setTree(this);
		ModContent.transformationLeavesProperties.setDynamicLeavesState(transformationLeaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, 0));
		transformationLeaves.setProperties(0, ModContent.transformationLeavesProperties);

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
		setCommonSpecies(new SpeciesMagicTransformation(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(transformationLeaves);
		blockList.add(ModContent.transformCoreBranch);
		blockList.add(ModContent.transformCoreBranchOff);
		return super.getRegisterableBlocks( blockList);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "treeOfTransformationbranch";
		branch = new BlockBranchTwilight(branchName){
			@Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
				return false;
			}
		};
		return branch;
	}

}
