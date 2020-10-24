package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenTrunkCore;
import net.minecraft.block.Block;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class TreeMagicSorting extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:magic_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:magic_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockTFMagicLog.VARIANT, MagicWoodVariant.SORT);
	public static int logsMeta = 3;
	public static int saplingMeta = 8;

	public class SpeciesMagicSorting extends Species {

		SpeciesMagicSorting(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.sortingLeavesProperties);

			setBasicGrowingParameters(2.0f, 5f, upProbability, 4, growthRate);

			generateSeed();
			addGenFeature(new FeatureGenTrunkCore(ModContent.sortingCoreBranchOff, 7, 2));
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
	}

	public static BlockBranchTwilight branch;
	public TreeMagicSorting() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "sortingTree"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.sortingCoreBranch.setFamily(this);
		ModContent.sortingCoreBranchOff.setFamily(this);
		ModContent.sortingLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesMagicSorting(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(ModContent.sortingCoreBranch);
		blockList.add(ModContent.sortingCoreBranchOff);
		return super.getRegisterableBlocks( blockList);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "sortingTreebranch";
		branch = new BlockBranchTwilight(branchName){
			@Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
				return false;
			}
		};
		return branch;
	}
}
