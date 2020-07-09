package maxhyper.dynamictreestheaether2.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.gildedgames.aether.api.registrar.BlocksAether;
import com.gildedgames.aether.api.registrar.ItemsAether;
import maxhyper.dynamictreestheaether2.DynamicTreesTheAether2;
import maxhyper.dynamictreestheaether2.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class A2TreeGreenSkyroot extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("aether:green_skyroot_leaves");
	public static Block logBlock = Block.getBlockFromName("aether:skyroot_log");
	public static Block saplingBlock = Block.getBlockFromName("aether:skyroot_sapling");

	public class SpeciesSkyroot extends Species {

		SpeciesSkyroot(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.greenSkyrootLeavesProperties);

			setBasicGrowingParameters(0.8f, 8.0f, upProbability, lowestBranchHeight, growthRate);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
			clearAcceptableSoils();
			addAcceptableSoil(Block.getBlockFromName("aether:aether_grass"), Block.getBlockFromName("aether:thera_grass"),
					Block.getBlockFromName("aether:aether_dirt"), Block.getBlockFromName("aether:thera_dirt"));
		}

	}

	public A2TreeGreenSkyroot() {
		super(new ResourceLocation(DynamicTreesTheAether2.MODID, "greenskyroot"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.greenSkyrootLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, 0);
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
