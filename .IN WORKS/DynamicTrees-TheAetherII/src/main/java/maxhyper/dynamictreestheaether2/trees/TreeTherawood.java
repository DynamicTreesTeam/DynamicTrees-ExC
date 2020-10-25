package maxhyper.dynamictreestheaether2.trees;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.gildedgames.aether.api.registrar.ItemsAether;
import maxhyper.dynamictreestheaether2.DynamicTreesTheAether2;
import maxhyper.dynamictreestheaether2.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeTherawood extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("aether:therawood_leaves");
	public static Block logBlock = Block.getBlockFromName("aether:therawood_log");

	public class SpeciesTherawood extends Species {

		SpeciesTherawood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.therawoodLeavesProperties);

			setBasicGrowingParameters(0.7f, 8.0f, 1, 2, 0.8f);
			setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.CONIFER));

			generateSeed();

			clearAcceptableSoils();
			addAcceptableSoils(ModContent.THERALIKE);
		}

	}

	public TreeTherawood() {
		super(new ResourceLocation(DynamicTreesTheAether2.MODID, "therawood"));

		setPrimitiveLog(logBlock.getDefaultState());

		ModContent.therawoodLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(logBlock, qty);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public ItemStack getStick(int qty) {
		return new ItemStack(ItemsAether.skyroot_stick, qty);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesTherawood(this));
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
