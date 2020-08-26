package maxhyper.dynamictreestheaether2.trees;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.gildedgames.aether.api.registrar.BlocksAether;
import com.gildedgames.aether.api.registrar.ItemsAether;
import maxhyper.dynamictreestheaether2.DynamicTreesTheAether2;
import maxhyper.dynamictreestheaether2.ModContent;
import maxhyper.dynamictreestheaether2.genfeatures.FeatureGenRandomLeaves;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class A2TreeHoliday extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("aether:mutant_leaves");
	public static Block leavesBlock2 = Block.getBlockFromName("aether:mutant_leaves_decorated");
	public static Block logBlock = Block.getBlockFromName("aether:skyroot_log");
	public static Block saplingBlock = Block.getBlockFromName("aether:unique_sapling");

	public class SpeciesHoliday extends Species {

		SpeciesHoliday(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.holidayLeavesProperties);

			setBasicGrowingParameters(0.1f, 14.0f, 6, 3, 0.9f);
			setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.CONIFER));

			envFactor(Type.COLD, 1.8f);
			envFactor(Type.HOT, 0.5f);

			addGenFeature(new FeatureGenRandomLeaves(8, 16, ModContent.holidayLeavesProperties.getDynamicLeavesState(), ModContent.holidayDecorLeavesProperties.getDynamicLeavesState(), 0.1f));
			//addGenFeature(new FeatureGenSnowArea(15, BlocksAether.present.getDefaultState(), 80));
			generateSeed();
			clearAcceptableSoils();
			addAcceptableSoil(Block.getBlockFromName("aether:aether_grass"), Block.getBlockFromName("aether:thera_grass"),
					Block.getBlockFromName("aether:aether_dirt"), Block.getBlockFromName("aether:thera_dirt"));
		}
	}

	public A2TreeHoliday() {
		super(new ResourceLocation(DynamicTreesTheAether2.MODID, "holiday"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.holidayLeavesProperties.setTree(this);
		ModContent.holidayDecorLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesHoliday(this));
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
