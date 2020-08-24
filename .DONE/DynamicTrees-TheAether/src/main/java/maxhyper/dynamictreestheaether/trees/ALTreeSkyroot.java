package maxhyper.dynamictreestheaether.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLeaves;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLog;
import com.gildedgames.the_aether.blocks.util.EnumLeafType;
import com.gildedgames.the_aether.blocks.util.EnumLogType;
import com.gildedgames.the_aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class ALTreeSkyroot extends TreeFamily {

	public static Block leavesBlock = BlocksAether.aether_leaves;
	public static Block logBlock = BlocksAether.aether_log;
	public static Block saplingBlock = BlocksAether.skyroot_sapling;

	public class SpeciesSkyroot extends Species {

		SpeciesSkyroot(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.skyrootLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy / 2, upProbability, lowestBranchHeight, growthRate);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
			clearAcceptableSoils();
			addAcceptableSoil(BlocksAether.aether_grass, BlocksAether.enchanted_aether_grass, BlocksAether.aether_dirt);
		}

		@Override
		public BlockRooty getRootyBlock() {
			return ModContent.rootyDirtAether;
		}

	}

	public ALTreeSkyroot() {
		super(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "skyroot"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockAetherLog.wood_type, EnumLogType.Skyroot), new ItemStack(logBlock, 1, 0));

		ModContent.skyrootLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public ItemStack getStick(int qty) {
		return new ItemStack(ItemsAether.skyroot_stick, qty);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSkyroot(this));
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
