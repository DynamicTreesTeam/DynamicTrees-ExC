package maxhyper.dynamictreestheaether2.trees;

import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.gildedgames.aether.api.registrar.BlocksAether;
import com.gildedgames.aether.api.registrar.ItemsAether;
import maxhyper.dynamictreestheaether2.DynamicTreesTheAether2;
import maxhyper.dynamictreestheaether2.ModContent;
import net.minecraft.block.Block;
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

public class A2TreeAmberoot extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("aether:amberoot_leaves");
	public static Block logBlock = Block.getBlockFromName("aether:skyroot_log");
	public static Block logBlock2 = Block.getBlockFromName("aether:golden_oak_log");
	public static Block saplingBlock = Block.getBlockFromName("aether:unique_sapling");

	public class SpeciesAmberoot extends Species {

		SpeciesAmberoot(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.amberootLeavesProperties);

			setBasicGrowingParameters(0.6f, 12f, upProbability - 2, lowestBranchHeight + 2, growthRate /2);

			envFactor(Type.COLD, 0.8f);
			envFactor(Type.HOT, 0.7f);

			generateSeed();

			setupStandardSeedDropping();
			clearAcceptableSoils();
			addAcceptableSoil(Block.getBlockFromName("aether:aether_grass"), Block.getBlockFromName("aether:thera_grass"),
					Block.getBlockFromName("aether:aether_dirt"), Block.getBlockFromName("aether:thera_dirt"));
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
			if(signal.isInTrunk() && pos.getY() - signal.rootPos.getY() > signal.getSpecies().getLowestBranchHeight()){
				probMap[EnumFacing.UP.ordinal()] = 0;
			}
			return probMap;
		}
	}

	public A2TreeAmberoot() {
		super(new ResourceLocation(DynamicTreesTheAether2.MODID, "amberoot"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.amberootLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesAmberoot(this));
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
