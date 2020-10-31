package maxhyper.dynamictreesforbiddenarcanus.trees;

import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforbiddenarcanus.DynamicTreesForbiddenArcanus;
import maxhyper.dynamictreesforbiddenarcanus.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeCherrywood extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("forbidden_arcanus:cherrywood_leaves");
	public static Block logBlock = Block.getBlockFromName("forbidden_arcanus:cherrywood_log");
	public static Block saplingBlock = Block.getBlockFromName("forbidden_arcanus:cherrywood_sapling");

	public static float fruitingOffset = 0f; //summer

	public class SpeciesCherrywood extends Species {

		SpeciesCherrywood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.cherrywoodLeavesProperties);

			setBasicGrowingParameters(0.5f, 8.0f, upProbability, lowestBranchHeight, 0.8f);

			setSoilLongevity(10);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setFlowerSeasonHold(fruitingOffset - 0.5f, fruitingOffset + 0.5f);

			ModContent.blockCherry.setSpecies(this);
			this.addGenFeature(new FeatureGenFruit(ModContent.blockCherry).setRayDistance(4.0F).setFruitingRadius(6));
		}

		@Override
		public float seasonalFruitProductionFactor(World world, BlockPos pos) {
			float offset = fruitingOffset;
			return SeasonHelper.globalSeasonalFruitProductionFactor(world, pos, offset);
		}

		@Override
		public boolean testFlowerSeasonHold(World world, BlockPos pos, float seasonValue) {
			return SeasonHelper.isSeasonBetween(seasonValue, flowerSeasonHoldMin, flowerSeasonHoldMax);
		}
	}

	public TreeCherrywood() {
		super(new ResourceLocation(DynamicTreesForbiddenArcanus.MODID, "cherrywood"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.cherrywoodLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesCherrywood(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}
	
}
