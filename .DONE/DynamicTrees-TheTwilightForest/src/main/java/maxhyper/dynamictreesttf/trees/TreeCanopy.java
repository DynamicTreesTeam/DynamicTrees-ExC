package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeShrinker;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenLogCritter;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenWeb;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.BlockTFLeaves;
import twilightforest.enums.LeavesVariant;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeCanopy extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
    public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
    public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getStateFromMeta(1);
	public static int logsMeta = 1;
	public static int saplingMeta = 1;

	public static int heightLimitOverLowestBranch = 10;

	public class SpeciesCanopy extends Species {

		SpeciesCanopy(ResourceLocation name, TreeFamily treeFamily, ILeavesProperties leavesProperties) {
			super(name, treeFamily, leavesProperties);

			setBasicGrowingParameters(0.9f, 80, 10, 12, growthRate);

			generateSeed();
			setupStandardSeedDropping();

			addGenFeature(new FeatureGenLogCritter(getLowestBranchHeight() + heightLimitOverLowestBranch, ModContent.dynamicFirefly, 100, 5));
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  8, 5, 1));
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			if (pos.getY() > signal.rootPos.getY() + this.getLowestBranchHeight() + heightLimitOverLowestBranch){
				probMap[EnumFacing.UP.ordinal()] = 0; //Forces the tree to flatten out 5 blocks above min branch
			}
			return probMap;
		}
	}

	public class SpeciesSpookyCanopy extends SpeciesCanopy {

		SpeciesSpookyCanopy(TreeFamily treeFamily) {
			super(new ResourceLocation(DynamicTreesTTF.MODID, "canopyspooky"), treeFamily, ModContent.spookyCanopyLeavesProperties);

			addGenFeature(new FeatureGenWeb(this, 4));
		}

		@Override
		public ResourceLocation getSaplingName() {
			return new ResourceLocation(DynamicTreesTTF.MODID, "canopy");
		}

		@Override
		public boolean canGrowWithBoneMeal(World world, BlockPos pos) {
			return false;
		}

		@Override
		public boolean grow(World world, BlockRooty rootyDirt, BlockPos rootPos, int soilLife, ITreePart treeBase, BlockPos treePos, Random random, boolean natural) {
			return false;
		}

		@Override
		public boolean transitionToTree(World world, BlockPos pos) {
			//Ensure planting conditions are right
			TreeFamily tree = getFamily();
			if(world.isAirBlock(pos.up()) && isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()))) {
				world.setBlockState(pos, tree.getDynamicBranch().getDefaultState());//set to a single branch
				placeRootyDirtBlock(world, pos.down(), 15);//Set to fully fertilized rooty sand underneath
				return true;
			}

			return false;
		}
	}

	Species spookySpecies;

	public TreeCanopy() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "canopy"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.canopyLeavesProperties.setTree(this);
		ModContent.spookyCanopyLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);

		addSpeciesLocationOverride((access, trunkPos) -> {
			if(Species.isOneOfBiomes(access.getBiome(trunkPos), TFBiomes.spookyForest)) {
				return spookySpecies;
			}
			return Species.NULLSPECIES;
		});
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, logsMeta);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		spookySpecies = new SpeciesSpookyCanopy(this);
		setCommonSpecies(new SpeciesCanopy(getName(),this, ModContent.canopyLeavesProperties));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
		speciesRegistry.register(spookySpecies);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "canopybranch";
		return new BlockBranchTwilight(branchName);
	}
}
