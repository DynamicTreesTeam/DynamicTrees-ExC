package maxhyper.dynamictreesforbiddenarcanus.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforbiddenarcanus.DynamicTreesForbiddenArcanus;
import maxhyper.dynamictreesforbiddenarcanus.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeMysterywood extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("forbidden_arcanus:mysterywood_leaves");
	public static Block logBlock = Block.getBlockFromName("forbidden_arcanus:mysterywood_log");
	public static Block saplingBlock = Block.getBlockFromName("forbidden_arcanus:mysterywood_sapling");

	public class SpeciesMysterywood extends Species {

		SpeciesMysterywood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.mysterywoodLeavesProperties);

			setBasicGrowingParameters(0.3f, 10.0f, 4, 3, 0.8f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();
			setupStandardSeedDropping();

			ModContent.blockGoldenApple.setSpecies(this);
			this.addGenFeature(new FeatureGenFruit(ModContent.blockGoldenApple).setRayDistance(4.0F));
		}
		@Override
		public float seasonalFruitProductionFactor(World world, BlockPos pos) {
			return 1;
		}
	}

	public TreeMysterywood() {
		super(new ResourceLocation(DynamicTreesForbiddenArcanus.MODID, "mysterywood"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.mysterywoodLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock));
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesMysterywood(this));
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
		return new BlockBranchBasic(getName()+"branch"){
			@Override
			public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
				return Math.max(TreeHelper.getRadius(world, pos) - 1, 0);
			}
		};
	}
}
