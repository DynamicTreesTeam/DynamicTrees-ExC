package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorApple;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorHarvest;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeInflator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilightThick;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import maxhyper.dynamictreesttf.dropcreators.DropCreatorOtherSeed;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenLogCritter;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.BlockTFLeaves;
import twilightforest.enums.LeavesVariant;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;

public class TreeRobustTwilightOak extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.OAK);
	public static int logsMeta = 0;
	public static int saplingMeta = 4;

	public class SpeciesRobustTwilightOak extends Species {

		SpeciesRobustTwilightOak(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.robustTwilightOakLeavesProperties);

			setBasicGrowingParameters(0.4f, 25.0f, 50, 15, 1.5f);

			setSoilLongevity(50);

			generateSeed();

            //addGenFeature(new FeatureGenLogCritter(getLowestBranchHeight() + 10, ModContent.dynamicFirefly, 100, 3));
			//addGenFeature(new FeatureGenLogCritter(getLowestBranchHeight() + 10, ModContent.dynamicCicada, 200, 3));
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 20, 10));
            addGenFeature(new FeatureGenMound(6));//Establish mounds
			addGenFeature(new FeatureGenClearVolume(20));
            addGenFeature(new FeatureGenRoots(10).setScaler(getRootScaler()));//Finally Generate Roots
		}
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 13 ? (trunkRadius / 24f) : 0, 0, 1);
				return (int) (inRadius * scale);
			};
		}

		@Override
		public boolean isThick() {
			return true;
		}

        @Override
        public int maxBranchRadius() {
            return 18;
        }

//        @Override
//		public float getEnergy(World world, BlockPos pos) {
//			long day = world.getWorldTime() / 24000L;
//			int month = (int) day / 30; // Change the hashs every in-game month
//
//			return Math.min(super.getEnergy(world, pos) * biomeSuitability(world, pos) + (coordHashCode(pos.up(month)) % 16), 30); // Vary the height energy by a psuedorandom hash function
//		}
//		@Override
//		public int getLowestBranchHeight(World world, BlockPos pos) {
//			long day = world.getWorldTime() / 24000L;
//			int month = (int) day / 30; // Change the hashs every in-game month
//
//			return (int) ((getLowestBranchHeight() + ((coordHashCode(pos.up(month)) % 16) * 0.5f)) * biomeSuitability(world, pos));
//		}

//		@Override
//		public EnumFacing selectNewDirection(World world, BlockPos pos, BlockBranch branch, GrowSignal signal) {
////			for (EnumFacing dir : EnumFacing.HORIZONTALS) {
////				if (world.isAirBlock(pos.offset(dir))&& signal.rand.nextInt(2) != 0) {
////					return EnumFacing.UP;
////				}
////			}
//			return super.selectNewDirection(world,pos,branch,signal);
//		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();

			probMap[0] = 1;
			probMap[1] = signal.isInTrunk() ? getUpProbability() : 0; //disable up when out of trunk
			probMap[2] = probMap[3] = probMap[4] = probMap[5] =  signal.isInTrunk() ? 1 : 20;
			probMap[originDir.ordinal()] = 0;
			probMap[signal.dir.getIndex()] *= 2;

			return probMap;
		}

		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (signal.energy < 6f && !signal.isInTrunk()) {
				signal.energy = 6f;
			}
			return newDir;
		}

	}

    public BlockSurfaceRoot twilightRoots;
	public TreeRobustTwilightOak() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "robustTwilightOak"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.robustTwilightOakLeavesProperties.setTree(this);
        twilightRoots = new BlockSurfaceRoot(Material.WOOD, "twilight_roots");

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, logsMeta);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesRobustTwilightOak(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public boolean isThick() {
		return true;
	}

	    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList = super.getRegisterableBlocks(blockList);
        blockList.add(twilightRoots);
        return super.getRegisterableBlocks(blockList);
    }

	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return twilightRoots;
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "robustTwilightOakbranch";
		return new BlockBranchTwilightThick(branchName);
	}

}
