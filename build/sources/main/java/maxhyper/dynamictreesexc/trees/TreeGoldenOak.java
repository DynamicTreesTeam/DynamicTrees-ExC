package maxhyper.dynamictreesexc.trees;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public class TreeGoldenOak extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("thaumicbases:goldenleaves");
	public static Block logBlock = Block.getBlockFromName("thaumicbases:goldenlogs");
	public static Block saplingBlock = Block.getBlockFromName("thaumicbases:goldensapling");

	public class SpeciesGoldenOak extends Species {

		SpeciesGoldenOak(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.goldenOakLeavesProperties);
			setBasicGrowingParameters(0.3f, 12.0f, upProbability, lowestBranchHeight, 0.8f);

			generateSeed();
			addDropCreator(new DropCreatorSeed(0.5f));

			this.addGenFeature((new FeatureGenFruit(ModContent.blockGoldenApple)).setRayDistance(4.0F));
		}
	}

	public TreeGoldenOak() {
		super(new ResourceLocation(DynamicTreesExC.MODID, "goldenOak"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock));

		ModContent.goldenOakLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesGoldenOak(this));
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
