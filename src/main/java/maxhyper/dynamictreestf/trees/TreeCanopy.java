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
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.TFBlocks;

import java.util.List;
import java.util.Objects;

public class TreeCanopy extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
    public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
    public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
    public static int leavesMeta = 1;
	public static int logsMeta = 1;
	public static int saplingMeta = 1;

	public class SpeciesCanopy extends Species {

		SpeciesCanopy(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.canopyLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy, upProbability, lowestBranchHeight, growthRate);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public TreeCanopy() {
		super(new ResourceLocation(DynamicTreesTF.MODID, "canopy"));

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
	
}
