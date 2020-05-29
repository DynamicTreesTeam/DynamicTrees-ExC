package maxhyper.dynamictreesforbiddenarcanus.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforbiddenarcanus.DynamicTreesForbiddenArcanus;
import maxhyper.dynamictreesforbiddenarcanus.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class FnATreeCherrywood extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("forbidden_arcanus:cherrywood_leaves");
	public static Block logBlock = Block.getBlockFromName("forbidden_arcanus:cherrywood_log");
	public static Block saplingBlock = Block.getBlockFromName("forbidden_arcanus:cherrywood_sapling");

	public class SpeciesCherrywood extends Species {

		SpeciesCherrywood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.cherrywoodLeavesProperties);

			setBasicGrowingParameters(0.3f, 8.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setupStandardSeedDropping();
		}
	}

	public FnATreeCherrywood() {
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
