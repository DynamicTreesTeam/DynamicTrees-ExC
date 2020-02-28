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

public class TreeMagicTime extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:magic_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:magic_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
    public static int leavesMeta = 0;
	public static int logsMeta = 0;
	public static int saplingMeta = 5;

	public class SpeciesMagicTime extends Species {

		SpeciesMagicTime(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.timeLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy, upProbability, lowestBranchHeight, growthRate);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public TreeMagicTime() {
		super(new ResourceLocation(DynamicTreesTF.MODID, "treeOfTime"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.timeLeavesProperties.setTree(this);

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
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}
	
}
