package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.overworld.NaturaOverworld;
import com.progwml6.natura.overworld.block.logs.BlockOverworldLog2;
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

public class TreeHopseed extends TreeFamily {

	public static Block leavesBlock = NaturaOverworld.overworldLeaves2;
    public static Block logBlock = NaturaOverworld.overworldLog2;
    public static Block saplingBlock = NaturaOverworld.overworldSapling2;
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockOverworldLog2.TYPE, BlockOverworldLog2.LogType.HOPSEED);

	public class SpeciesHopseed extends Species {

		SpeciesHopseed(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.hopseedLeavesProperties);

			setBasicGrowingParameters(1.8f, 7.0f, 0, 2, 0.8f);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("hopseed"));

			setSoilLongevity(2);

			setSeedStack(new ItemStack(ModContent.hopseedSeed));
			setupStandardSeedDropping();
		}

		@Override
		public boolean isThick() {
			return true;
		}
	}

	public TreeHopseed() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "hopseed"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockOverworldLog2.TYPE, BlockOverworldLog2.LogType.HOPSEED));

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
