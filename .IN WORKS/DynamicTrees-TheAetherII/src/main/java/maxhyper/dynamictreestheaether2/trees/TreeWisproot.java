package maxhyper.dynamictreestheaether2.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeWisproot extends TreeFamily {

	public static Block leavesBlockGreen = Block.getBlockFromName("aether:green_light_skyroot_leaves");
	public static Block leavesBlockBlue = Block.getBlockFromName("aether:blue_light_skyroot_leaves");
	public static Block leavesBlockDarkBlue = Block.getBlockFromName("aether:dark_blue_light_skyroot_leaves");
	public static Block logBlock = Block.getBlockFromName("aether:light_skyroot_log");
	public static Block saplingBlock = Block.getBlockFromName("aether:light_skyroot_sapling");

	public class SpeciesGreenWisproot extends Species {

		SpeciesGreenWisproot (String color, TreeFamily treeFamily, ILeavesProperties leavesProperties){
			super(new ResourceLocation(treeFamily.getName().getResourceDomain(), color + treeFamily.getName().getResourcePath()), treeFamily, leavesProperties);

			setBasicGrowingParameters(0.2f, 28, 10, 4, growthRate);
			addGenFeature(new FeatureGenRoots(4).setScaler(getRootScaler()).setLevelLimit(8));
			clearAcceptableSoils();
			addAcceptableSoils(ModContent.AETHERLIKE);
		}
		SpeciesGreenWisproot(TreeFamily treeFamily) {
			this("green", treeFamily, ModContent.greenWisprootLeavesProperties);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
		}
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 8 ? (trunkRadius / 10f) : 0, 0, 1);
				return (int) (inRadius * scale);
			};
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
			for (EnumFacing dir : EnumFacing.HORIZONTALS){
				probMap[dir.ordinal()] *= 3;
			}
			if (signal.isInTrunk()){
				probMap[EnumFacing.UP.ordinal()] = 0;
			}
			//Branching is prevented all together, only allowing radius 1 branches to expand
			if ((radius > 1) && !(signal.isInTrunk() && radius <= 2) && (signal.energy > 3)){
				for (EnumFacing dir : EnumFacing.values()){
					if (!TreeHelper.isBranch(world.getBlockState(pos.offset(dir)))){
						//minuscule chance that it branches out anyways, but if it does its short lived
						if (world.rand.nextFloat() < 0.999f){
							probMap[dir.ordinal()] = 0;
						} else {
							signal.energy = 4;
						}
					}
				}
			}

			probMap[signal.dir.getOpposite().ordinal()] = 0;
			probMap[EnumFacing.DOWN.ordinal()] = 0;

			return probMap;
		}

	}

	public class SpeciesBlueWisproot extends SpeciesGreenWisproot {

		SpeciesBlueWisproot(TreeFamily treeFamily) {
			super("blue", treeFamily, ModContent.blueWisprootLeavesProperties);

			setRequiresTileEntity(true);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
		}
	}

	public class SpeciesDarkBlueWisproot extends SpeciesGreenWisproot {

		SpeciesDarkBlueWisproot(TreeFamily treeFamily) {
			super("darkblue", treeFamily, ModContent.darkblueWisprootLeavesProperties);

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
	public TreeWisproot() {
		super(new ResourceLocation(DynamicTreesTheAether2.MODID, "wisproot"));

		setPrimitiveLog(logBlock.getDefaultState());

		ModContent.greenWisprootLeavesProperties.setTree(this);
		ModContent.blueWisprootLeavesProperties.setTree(this);
		ModContent.darkblueWisprootLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesGreenWisproot(this));
		blue = new SpeciesBlueWisproot(this);
		darkBlue = new SpeciesDarkBlueWisproot(this);
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
	public BlockBranch createBranch() {
		return new BlockBranchBasic("wisprootbranch"){
			@Override
			protected int getMaxSignalDepth() {
				return 64;
			}
		};
	}
}
