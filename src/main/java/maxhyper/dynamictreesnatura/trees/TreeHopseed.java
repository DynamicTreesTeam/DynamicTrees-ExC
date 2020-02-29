package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.feautregen.FeatureGenGrowCoreToMax;
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

public class TreeHopseed extends TreeFamily {

	public static Block leavesBlock = NaturaOverworld.overworldLeaves2;
    public static Block logBlock = NaturaOverworld.overworldLog2;
    public static Block saplingBlock = NaturaOverworld.overworldSapling2;

	public class SpeciesHopseed extends Species {

		SpeciesHopseed(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.hopseedLeavesProperties);

			setBasicGrowingParameters(1.8f, 7.0f, 0, 2, 0.8f);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("hopseed"));
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();
			setupStandardSeedDropping();

		}

		@Override
		public boolean isThick() {
			return true;
		}
	}

	public TreeHopseed() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "hopseed"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 2));

		ModContent.hopseedLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 2);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}
	@Override
	public ItemStack getStick(int qty) {
		ItemStack stick = NaturaCommons.hopseed_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public boolean isThick() {
		return true;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesHopseed(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override public BlockBranch createBranch() {
        String branchName = getName() + "branch";
        return new BlockBranchThick(branchName){
			@Override
			public int getMaxRadius() {
				return 16;
			}
		};
    }
}
