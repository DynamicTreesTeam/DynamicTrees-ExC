package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenLogCritter;
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
import twilightforest.block.BlockTFLeaves;
import twilightforest.enums.LeavesVariant;

import java.util.List;
import java.util.Objects;

public class TreeCanopy extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
    public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
    public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.CANOPY);
	public static int logsMeta = 1;
	public static int saplingMeta = 1;

	public static int heightLimitOverLowestBranch = 10;

	public class SpeciesCanopy extends Species {

		SpeciesCanopy(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.canopyLeavesProperties);

			setBasicGrowingParameters(0.9f, 80, 10, 12, growthRate);

			generateSeed();
			setupStandardSeedDropping();

			addGenFeature(new FeatureGenLogCritter(getLowestBranchHeight() + heightLimitOverLowestBranch, ModContent.dynamicFirefly, 100, 5));
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 5, 3));
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			if (pos.getY() > signal.rootPos.getY() + this.getLowestBranchHeight() + heightLimitOverLowestBranch){
				probMap[EnumFacing.UP.ordinal()] = 0; //Forces the tree to flatten out 5 blocks above min branch
			}
			return probMap;
		}
	}

	public TreeCanopy() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "canopy"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.canopyLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesCanopy(this));
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
		String branchName = "canopybranch";
		return new BlockBranchTwilight(branchName);
	}
}
