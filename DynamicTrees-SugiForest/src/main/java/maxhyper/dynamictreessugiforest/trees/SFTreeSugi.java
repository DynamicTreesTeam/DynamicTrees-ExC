package maxhyper.dynamictreessugiforest.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreessugiforest.DynamicTreesSugiForest;
import maxhyper.dynamictreessugiforest.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class SFTreeSugi extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("sugiforest:sugi_leaves");
	public static Block logBlock = Block.getBlockFromName("sugiforest:sugi_log");
	public static Block saplingBlock = Block.getBlockFromName("sugiforest:sugi_sapling");
	public static Block leafPileBlock = Block.getBlockFromName("sugiforest:sugi_fallen_leaves");

	public class SpeciesSugi extends Species {

		SpeciesSugi(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.sugiLeavesProperties);

			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setupStandardSeedDropping();
		}
	}

	public SFTreeSugi() {
		super(new ResourceLocation(DynamicTreesSugiForest.MODID, "sugi"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.sugiLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesSugi(this));
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
