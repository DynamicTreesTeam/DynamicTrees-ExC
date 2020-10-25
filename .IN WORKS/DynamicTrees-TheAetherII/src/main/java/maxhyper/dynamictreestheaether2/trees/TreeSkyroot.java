package maxhyper.dynamictreestheaether2.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.gildedgames.aether.api.registrar.ItemsAether;
import maxhyper.dynamictreestheaether2.DynamicTreesTheAether2;
import maxhyper.dynamictreestheaether2.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeSkyroot extends TreeFamily {

	public static Block leavesBlockGreen = Block.getBlockFromName("aether:green_skyroot_leaves");
	public static Block leavesBlockBlue = Block.getBlockFromName("aether:blue_skyroot_leaves");
	public static Block leavesBlockDarkBlue = Block.getBlockFromName("aether:dark_blue_skyroot_leaves");
	public static Block logBlock = Block.getBlockFromName("aether:skyroot_log");
	public static Block saplingBlock = Block.getBlockFromName("aether:skyroot_sapling");

	public class SpeciesGreenSkyroot extends Species {

		SpeciesGreenSkyroot(String color, TreeFamily treeFamily, ILeavesProperties leavesProperties) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), color + treeFamily.getName().getResourcePath()), treeFamily, leavesProperties);

			setBasicGrowingParameters(0.4f, 8.0f, 1, 3, 1);

			addGenFeature(new FeatureGenRoots(4).setScaler(getRootScaler()));
			clearAcceptableSoils();
			addAcceptableSoils(ModContent.AETHERLIKE);
		}
		SpeciesGreenSkyroot(TreeFamily treeFamily) {
			this("green", treeFamily, ModContent.greenSkyrootLeavesProperties);


			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
		}
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 8 ? (trunkRadius / 12f) : 0, 0, 1);
				return (int) (inRadius * scale);
			};
		}
	}

	public class SpeciesBlueSkyroot extends SpeciesGreenSkyroot {

		SpeciesBlueSkyroot(TreeFamily treeFamily) {
			super("blue", treeFamily, ModContent.blueSkyrootLeavesProperties);

			setRequiresTileEntity(true);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public class SpeciesDarkBlueSkyroot extends SpeciesGreenSkyroot {

		SpeciesDarkBlueSkyroot(TreeFamily treeFamily) {
			super("darkblue", treeFamily, ModContent.darkblueSkyrootLeavesProperties);

			setRequiresTileEntity(true);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	Species blue;
	Species darkBlue;
	BlockSurfaceRoot roots;
	public TreeSkyroot() {
		super(new ResourceLocation(DynamicTreesTheAether2.MODID, "skyroot"));

		setPrimitiveLog(logBlock.getDefaultState());

		ModContent.greenSkyrootLeavesProperties.setTree(this);
		ModContent.blueSkyrootLeavesProperties.setTree(this);
		ModContent.darkblueSkyrootLeavesProperties.setTree(this);

		roots = new BlockSurfaceRoot(Material.WOOD, getName() + "_roots");

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlockGreen);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(logBlock, qty);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return roots;
	}

	@Override
	public ItemStack getStick(int qty) {
		return new ItemStack(ItemsAether.skyroot_stick, qty);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesGreenSkyroot(this));
		blue = new SpeciesBlueSkyroot(this);
		darkBlue = new SpeciesDarkBlueSkyroot(this);
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(blue);
		speciesRegistry.register(darkBlue);
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(roots);
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		itemList.add(blue.getSeed());
		itemList.add(darkBlue.getSeed());
		return super.getRegisterableItems(itemList);
	}
}
