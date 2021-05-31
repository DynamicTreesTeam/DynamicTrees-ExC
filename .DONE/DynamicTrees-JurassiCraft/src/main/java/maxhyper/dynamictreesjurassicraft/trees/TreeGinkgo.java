package maxhyper.dynamictreesjurassicraft.trees;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesjurassicraft.DynamicTreesJurassiCraft;
import maxhyper.dynamictreesjurassicraft.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeGinkgo extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("jurassicraft:ginkgo_leaves");
	public static Block logBlock = Block.getBlockFromName("jurassicraft:ginkgo_log");
	public static Block saplingBlock = Block.getBlockFromName("jurassicraft:ginkgo_sapling");

	public class SpeciesGinko extends Species {

		SpeciesGinko(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.ginkgoLeavesProperties);

			this.setBasicGrowingParameters(0.25F, 16.0F, 3, 3, 0.9F);
			this.setGrowthLogicKit(new ConiferLogic().setHorizontalLimiter(4));

			setSoilLongevity(6);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public TreeGinkgo() {
		super(new ResourceLocation(DynamicTreesJurassiCraft.MODID, "ginkgo"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.ginkgoLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesGinko(this));
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
