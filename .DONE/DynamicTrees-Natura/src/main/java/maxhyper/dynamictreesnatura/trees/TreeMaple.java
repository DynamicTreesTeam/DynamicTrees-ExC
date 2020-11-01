package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.overworld.NaturaOverworld;
import com.progwml6.natura.overworld.block.logs.BlockOverworldLog;
import com.progwml6.natura.shared.NaturaCommons;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeMaple extends TreeFamily {

	public static Block leavesBlock = NaturaOverworld.overworldLeaves;
    public static Block logBlock = NaturaOverworld.overworldLog;
    public static Block saplingBlock = NaturaOverworld.overworldSapling;
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockOverworldLog.TYPE, BlockOverworldLog.LogType.MAPLE);

	public class SpeciesMaple extends Species {

		SpeciesMaple(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.mapleLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy, upProbability, lowestBranchHeight, growthRate);

			setSeedStack(new ItemStack(ModContent.mapleSeed));
			setupStandardSeedDropping();
		}
	}

	public TreeMaple() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "maple"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockOverworldLog.TYPE, BlockOverworldLog.LogType.MAPLE));

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
