package maxhyper.dynamictreestheaether2.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
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

public class TreeGreatroot extends TreeFamily {

	public static Block leavesBlockGreen = Block.getBlockFromName("aether:green_dark_skyroot_leaves");
	public static Block leavesBlockBlue = Block.getBlockFromName("aether:blue_dark_skyroot_leaves");
	public static Block leavesBlockDarkBlue = Block.getBlockFromName("aether:dark_blue_dark_skyroot_leaves");
	public static Block logBlock = Block.getBlockFromName("aether:dark_skyroot_log");
	public static Block saplingBlock = Block.getBlockFromName("aether:dark_skyroot_sapling");

	public class SpeciesGreenGreatroot extends Species {

		SpeciesGreenGreatroot(String color, TreeFamily treeFamily, ILeavesProperties leavesProperties) {
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), color + treeFamily.getName().getResourcePath()), treeFamily, leavesProperties);

			setBasicGrowingParameters(0.6f, 25, 5, 6, 1.2f);
			setSoilLongevity(40);

			addGenFeature(new FeatureGenConiferTopper(leavesProperties));
			addGenFeature(new FeatureGenRoots(7).setScaler(getRootScaler()).setLevelLimit(6));
			clearAcceptableSoils();
			addAcceptableSoils(ModContent.AETHERLIKE);
		}
		SpeciesGreenGreatroot(TreeFamily treeFamily) {
			this("green", treeFamily, ModContent.greenGreatrootLeavesProperties);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
		}
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 8 ? (trunkRadius / 25f) : 0, 0, 1);
				return Math.min((int) (inRadius * scale), 8);
			};
		}

		@Override
		public boolean isThick() {
			return true;
		}

		@Override
		public int maxBranchRadius() {
			return 24;
		}
	}

	public class SpeciesBlueGreatroot extends SpeciesGreenGreatroot {

		SpeciesBlueGreatroot(TreeFamily treeFamily) {
			super("blue", treeFamily, ModContent.blueGreatrootLeavesProperties);

			setRequiresTileEntity(true);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public class SpeciesDarkBlueGreatroot extends SpeciesGreenGreatroot {

		SpeciesDarkBlueGreatroot(TreeFamily treeFamily) {
			super("darkblue", treeFamily, ModContent.darkblueGreatrootLeavesProperties);

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
	public TreeGreatroot() {
		super(new ResourceLocation(DynamicTreesTheAether2.MODID, "greatroot"));

		setPrimitiveLog(logBlock.getDefaultState());

		ModContent.greenGreatrootLeavesProperties.setTree(this);
		ModContent.blueGreatrootLeavesProperties.setTree(this);
		ModContent.darkblueGreatrootLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesGreenGreatroot(this));
		blue = new SpeciesBlueGreatroot(this);
		darkBlue = new SpeciesDarkBlueGreatroot(this);
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
	@Override
	public boolean isThick() {
		return true;
	}
}
