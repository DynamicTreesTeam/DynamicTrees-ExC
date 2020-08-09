package maxhyper.dynamictreestotemic.trees;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import maxhyper.dynamictreestotemic.DynamicTreesTotemic;
import maxhyper.dynamictreestotemic.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeRedCedar extends TreeFamily {

	public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("totemic","cedar_leaves"));
	public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("totemic","cedar_log"));
	public static Block saplingBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("totemic","cedar_sapling"));

	public class SpeciesRedCedar extends Species {

		SpeciesRedCedar(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.redCedarLeavesProperties);
			setGrowthLogicKit(new ConiferLogic(3.0f));

			setBasicGrowingParameters(0.5f, 10.0f, 20, 1, 0.8f);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.FOREST, 1.05f);

			generateSeed();

			setupStandardSeedDropping();
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			probMap[EnumFacing.DOWN.ordinal()] = 0;

			if (!signal.isInTrunk()){
				probMap[signal.dir.ordinal()] /= 2f;
			}

			return probMap;
		}

	}

	public TreeRedCedar() {
		super(new ResourceLocation(DynamicTreesTotemic.MODID, "redCedar"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.redCedarLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesRedCedar(this));
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
