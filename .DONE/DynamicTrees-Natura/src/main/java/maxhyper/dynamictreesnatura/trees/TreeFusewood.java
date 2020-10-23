package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.nether.block.leaves.BlockNetherLeaves;
import com.progwml6.natura.nether.block.logs.BlockNetherLog;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
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

			addAcceptableSoils(DirtHelper.NETHERLIKE);
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos pos) {
			return ModContent.rootyNetherDirt;
		}
	}

	public TreeFusewood() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "fusewood"));

		setDynamicBranch(ModContent.fusewoodBranch);

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockNetherLog.TYPE, BlockNetherLog.LogType.FUSEWOOD));

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
