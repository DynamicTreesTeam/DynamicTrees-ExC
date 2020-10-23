package maxhyper.dynamictreesextrautils2.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.rwtema.extrautils2.blocks.TreeIronWoods;
import maxhyper.dynamictreesextrautils2.DynamicTreesExtraUtils2;
import maxhyper.dynamictreesextrautils2.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public class EU2TreeFeJuniper extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("extrautils2:ironwood_leaves");
	public static Block logBlock = Block.getBlockFromName("extrautils2:ironwood_log");
	public static Block saplingBlock = Block.getBlockFromName("extrautils2:ironwood_sapling");
	public static Block planksBlock = Block.getBlockFromName("extrautils2:ironwood_planks");

	public static IBlockState leavesStateRaw = leavesBlock.getDefaultState().withProperty(TreeIronWoods.TREE_TYPE, TreeIronWoods.TreeType.RAW);
	public static IBlockState leavesStateBurnt = leavesBlock.getDefaultState().withProperty(TreeIronWoods.TREE_TYPE, TreeIronWoods.TreeType.BURNT);

	public class SpeciesFeJuniper extends Species {

		SpeciesFeJuniper(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.fejuniperLeavesRawProperties);
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.HOT, 1.10f);
			envFactor(Type.COLD, 0.90f);

			generateSeed();
			setupStandardSeedDropping();
		}

	}

	public EU2TreeFeJuniper() {
		super(new ResourceLocation(DynamicTreesExtraUtils2.MODID, "ferrousJuniper"));

		setDynamicBranch(ModContent.fejuniperBranchRaw);
		ModContent.fejuniperBranchBurnt.setFamily(this);

		ModContent.fejuniperLeavesRawProperties.setTree(this);
		ModContent.fejuniperLeavesBurntProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesFeJuniper(this));
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
