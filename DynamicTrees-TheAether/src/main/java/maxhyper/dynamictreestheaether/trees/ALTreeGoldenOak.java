package maxhyper.dynamictreestheaether.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.legacy.aether.blocks.BlocksAether;
import com.legacy.aether.blocks.natural.BlockAetherLeaves;
import com.legacy.aether.blocks.natural.BlockAetherLog;
import com.legacy.aether.blocks.util.EnumLeafType;
import com.legacy.aether.blocks.util.EnumLogType;
import com.legacy.aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.ModContent;
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

public class ALTreeGoldenOak extends TreeFamily {

	public static Block leavesBlock = BlocksAether.aether_leaves;
	public static Block logBlock = BlocksAether.aether_log;
	public static Block saplingBlock = BlocksAether.golden_oak_sapling;

	public class SpeciesSkyroot extends Species {

		SpeciesSkyroot(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.goldenOakLeavesProperties);

			setBasicGrowingParameters(0.6f, 20f, upProbability + 2, lowestBranchHeight + 5, growthRate /2);

			envFactor(Type.COLD, 0.8f);
			envFactor(Type.HOT, 0.7f);

			generateSeed();

			setupStandardSeedDropping();
			clearAcceptableSoils();
			addAcceptableSoil(BlocksAether.aether_grass, BlocksAether.enchanted_aether_grass);
		}

	}

	public ALTreeGoldenOak() {
		super(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "goldenoak"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockAetherLog.wood_type, EnumLogType.Oak), new ItemStack(logBlock, 1, 1));

		ModContent.goldenOakLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, 1);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public ItemStack getStick(int qty) {
		return new ItemStack(ItemsAether.skyroot_stick, qty);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSkyroot(this));
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
