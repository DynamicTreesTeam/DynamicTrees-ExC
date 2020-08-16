package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.nether.block.leaves.BlockNetherLeaves;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import com.progwml6.natura.nether.NaturaNether;
import com.progwml6.natura.shared.NaturaCommons;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeFusewood extends TreeFamily {

	public static Block leavesBlock = NaturaNether.netherLeaves;
    public static Block logBlock = NaturaNether.netherLog;
    public static Block saplingBlock = NaturaNether.netherSapling;
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockNetherLeaves.TYPE, BlockNetherLeaves.LeavesType.FUSEWOOD);

	public class SpeciesFusewood extends Species {

		SpeciesFusewood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.fusewoodLeavesProperties);

			setBasicGrowingParameters(0.3f, 10.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			setSeedStack(new ItemStack(ModContent.fusewoodSeed));
			setupStandardSeedDropping();
			this.addAcceptableSoil(Blocks.NETHERRACK, Blocks.SOUL_SAND);
		}
		@Override
		public BlockRooty getRootyBlock() {
			return ModContent.rootyNetherDirt;
		}
	}

	public TreeFusewood() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "fusewood"));

		setDynamicBranch(ModContent.fusewoodBranch);

		ModContent.fusewoodLeavesProperties.setTree(this);

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
		ItemStack stick = NaturaCommons.fusewood_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesFusewood(this));
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
