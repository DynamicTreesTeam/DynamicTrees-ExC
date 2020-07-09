package maxhyper.dynamictreessimplytea.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import elucent.simplytea.SimplyTea;
import maxhyper.dynamictreessimplytea.DynamicTreesSimplyTea;
import maxhyper.dynamictreessimplytea.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public class STTreeTea extends TreeFamily {

//	public static Block leavesBlock = Block.getBlockFromName("simplytea:tea_trunk");
//	public static Block logBlock = Block.getBlockFromName("simplytea:tea_trunk");
	public static Block saplingBlock = Block.getBlockFromName("simplytea:tea_sapling");

	public class SpeciesTea extends Species {

		SpeciesTea(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.teaLeavesProperties);

			setBasicGrowingParameters(0.3f, 6.0f, upProbability, 2, 1.5f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setupStandardSeedDropping();
		}
	}

	public STTreeTea() {
		super(new ResourceLocation(DynamicTreesSimplyTea.MODID, "tea"));

		//setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.teaLeavesProperties.setTree(this);

	}

	@Override
	public ItemStack getStick(int qty) {
		ItemStack stick = new ItemStack(SimplyTea.tea_stick);
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesTea(this));
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
