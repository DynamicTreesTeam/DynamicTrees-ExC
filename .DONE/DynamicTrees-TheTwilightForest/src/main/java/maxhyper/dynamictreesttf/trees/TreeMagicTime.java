package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilightThick;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenTrunkCore;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.BlockTFMagicLog;
import twilightforest.enums.MagicWoodVariant;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import com.ferreusveritas.dynamictrees.util.CoordUtils.Surround;

import javax.annotation.Nullable;

public class TreeMagicTime extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:magic_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:magic_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
    public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockTFMagicLog.VARIANT, MagicWoodVariant.TIME);
	public static int logsMeta = 0;
	public static int saplingMeta = 5;

	public class SpeciesMagicTime extends Species {

		SpeciesMagicTime(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.timeLeavesProperties);

			setBasicGrowingParameters(0.8f, signalEnergy, upProbability, 4, growthRate);

			generateSeed();
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 5, 3));
			addGenFeature(new FeatureGenRoots(12).setScaler(getRootScaler()));//Finally Generate Roots
			//addGenFeature(new FeatureGenTrunkCore(ModContent.timeCoreShell, 16, 2, Surround.E));
			addGenFeature(new FeatureGenTrunkCore(ModContent.timeCoreBranchOff, 17, 3, Surround.E)); //dont set to exactly 16
		}
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 13 ? (trunkRadius / 24f) : 0, 0, 1);
				return (int) (inRadius * scale);
			};
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
		public boolean isThick() {
			return true;
		}
	}

	public static BlockBranchTwilightThick branch;
	public BlockSurfaceRoot timeRoots;
	public TreeMagicTime() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "treeOfTime"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.timeLeavesProperties.setTree(this);
		ModContent.timeCoreBranch.setFamily(this);
		ModContent.timeCoreBranchOff.setFamily(this);
		timeRoots = new BlockSurfaceRoot(Material.WOOD, "time_roots");

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
		setCommonSpecies(new SpeciesMagicTime(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return timeRoots;
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(timeRoots);
		blockList.add(((BlockBranchThick)ModContent.timeCoreBranch).getPairSide(true));
		blockList.add(((BlockBranchThick)ModContent.timeCoreBranch).getPairSide(false));
		blockList.add(((BlockBranchThick)ModContent.timeCoreBranchOff).getPairSide(true));
		blockList.add(((BlockBranchThick)ModContent.timeCoreBranchOff).getPairSide(false));
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public boolean isThick() {
		return true;
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "treeOfTimeBranch";
		branch = new BlockBranchTwilightThick(branchName){
			@Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
				return false;
			}
		};
		return branch;
	}
}
