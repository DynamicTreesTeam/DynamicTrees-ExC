package maxhyper.dynamictreesthaumicbases.trees;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesthaumicbases.DynamicTreesThaumcraftAddons;
import maxhyper.dynamictreesthaumicbases.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TBTreeGoldenOak extends TreeFamily {

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

	public TBTreeGoldenOak() {
		super(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "goldenOak"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock));

		ModContent.goldenOakLeavesProperties.setTree(this);

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
