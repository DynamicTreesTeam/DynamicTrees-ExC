package maxhyper.dynamictreestf.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestf.DynamicTreesTF;
import maxhyper.dynamictreestf.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.TFBlocks;

import java.util.List;
import java.util.Objects;

public class TreeDarkwood extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:dark_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
    public static int leavesMeta = 0;
	public static int logsMeta = 3;
	public static int saplingMeta = 3;

	public class SpeciesDarkwood extends Species {

		SpeciesDarkwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.darkwoodLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy, upProbability, lowestBranchHeight, growthRate);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public TreeDarkwood() {
		super(new ResourceLocation(DynamicTreesTF.MODID, "darkwood"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.darkwoodLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesDarkwood(this));
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
