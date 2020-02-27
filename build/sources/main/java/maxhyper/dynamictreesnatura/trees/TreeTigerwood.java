package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import com.progwml6.natura.overworld.NaturaOverworld;
import com.progwml6.natura.shared.NaturaCommons;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeTigerwood extends TreeFamily {

	public static Block leavesBlock = NaturaOverworld.overworldLeaves;
    public static Block logBlock = NaturaOverworld.overworldLog;
    public static Block saplingBlock = NaturaOverworld.overworldSapling;

	public class SpeciesTigerwood extends Species {

		SpeciesTigerwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.tigerwoodLeavesProperties);

			setBasicGrowingParameters(0.3f, 10.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public TreeTigerwood() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "tigerwood"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 3));

		ModContent.tigerwoodLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 3);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}
	@Override
	public ItemStack getStick(int qty) {
		ItemStack stick = NaturaCommons.tiger_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesTigerwood(this));
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
