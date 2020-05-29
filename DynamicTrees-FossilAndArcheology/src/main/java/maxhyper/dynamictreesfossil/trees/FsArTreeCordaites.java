package maxhyper.dynamictreesfossil.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesfossil.DynamicTreesFossil;
import maxhyper.dynamictreesfossil.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class FsArTreeCordaites extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("fossil:cordaites_leaves");
	public static Block logBlock = Block.getBlockFromName("fossil:cordaites_log");
	public static Block saplingBlock = Block.getBlockFromName("fossil:cordaites_sapling");

	public class SpeciesCordaites extends Species {

		SpeciesCordaites(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.cordaitesLeavesProperties);

			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setupStandardSeedDropping();
		}
	}

	public FsArTreeCordaites() {
		super(new ResourceLocation(DynamicTreesFossil.MODID, "cordaites"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.cordaitesLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesCordaites(this));
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
