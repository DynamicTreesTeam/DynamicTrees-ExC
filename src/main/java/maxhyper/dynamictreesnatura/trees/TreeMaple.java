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

public class TreeMaple extends TreeFamily {

	public static Block leavesBlock = NaturaOverworld.overworldLeaves;
    public static Block logBlock = NaturaOverworld.overworldLog;
    public static Block saplingBlock = NaturaOverworld.overworldSapling;

	public class SpeciesMaple extends Species {

		SpeciesMaple(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.mapleLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy, upProbability, lowestBranchHeight, growthRate);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public TreeMaple() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "maple"));

		setPrimitiveLog(logBlock.getStateFromMeta(3), new ItemStack(logBlock, 1, 3));

		ModContent.mapleLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}
	@Override
	public ItemStack getStick(int qty) {
		ItemStack stick = NaturaCommons.maple_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesMaple(this));
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
