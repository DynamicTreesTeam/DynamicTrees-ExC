package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.substances.ISubstanceEffect;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.systems.substances.SubstanceTransform;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilightThick;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;

@Deprecated
public class TreeRobustTwilightOak extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getStateFromMeta(0);
	public static int logsMeta = 0;
	public static int saplingMeta = 4;

	public class SpeciesRobustTwilightOak extends Species {

		SpeciesRobustTwilightOak(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.robustTwilightOakLeavesProperties);

			setBasicGrowingParameters(0.4f, 25.0f, 50, 15, 1.5f);

			setSoilLongevity(50);

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
		public ResourceLocation getSaplingName() {
			return new ResourceLocation(DynamicTreesTTF.MODID, "twilightoakrobust");
		}

		@Override
		public boolean onTreeActivated(World world, BlockPos rootPos, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
			Species realSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakRobust"));
			ISubstanceEffect effect = new SubstanceTransform(realSpecies);
			effect.apply(world, rootPos);
			return super.onTreeActivated(world, rootPos, hitPos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
		}

		@Override
		public boolean isThick() {
			return true;
		}

        @Override
        public int maxBranchRadius() {
            return 18;
        }

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

	public TreeRobustTwilightOak() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "robustTwilightOak"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

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
		return new ArrayList<>();
	}

	@Override
	public boolean isThick() {
		return true;
	}

	    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        return super.getRegisterableBlocks(blockList);
    }

	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return TreeTwilightOak.twilightRoots;
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "robustTwilightOakbranch";
		BlockBranchTwilightThick branch = new BlockBranchTwilightThick(branchName){
			@Override
			public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
				BlockPos rootPos = TreeHelper.findRootNode(state, world, pos);
				Species realSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakSickly"));
				ISubstanceEffect effect = new SubstanceTransform(realSpecies);
				effect.apply(world, rootPos);
			}
		};
		branch.setTickRandomly(true);
		return branch;
	}

}
