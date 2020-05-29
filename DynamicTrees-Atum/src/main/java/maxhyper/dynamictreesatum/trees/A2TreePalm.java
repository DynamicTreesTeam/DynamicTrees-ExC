package maxhyper.dynamictreesatum.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesatum.DynamicTreesAtum;
import maxhyper.dynamictreesatum.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class A2TreePalm extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("atum:palm_leaves");
	public static Block logBlock = Block.getBlockFromName("atum:palm_log");
	public static Block saplingBlock = Block.getBlockFromName("atum:palm_sapling");

	public class SpeciesPalm extends Species {

		SpeciesPalm(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.palmLeavesProperties);

			setBasicGrowingParameters(0.4f, 9.0f, 4, 3, 0.9f);

			envFactor(Type.COLD, 0.25f);

			generateSeed();

			setupStandardSeedDropping();
		}

		//CODE FROM DYNAMICTREES-BOP
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();

			// Alter probability map for direction change
			probMap[0] = probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;
			probMap[1] = 10;
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from

			Random rand = new Random();
			if (world.getBlockState(pos.up()).getBlock() != world.getBlockState(pos).getBlock()){
				if (rand.nextInt(5) != 0){
					probMap[1] = 0;
					probMap[1 + rand.nextInt(5)] = 10;
					signal.numTurns = 0;
				}
			}

			return probMap;
		}

		// Palm trees are so similar that it makes sense to randomize their height for a little variation
		// but we don't want the trees to always be the same height all the time when planted in the same location
		// so we feed the hash function the in-game month
		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month

			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3); // Vary the height energy by a psuedorandom hash function
		}
		@Override
		public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, int soilLife, boolean natural) {
			IBlockState trunkBlockState = world.getBlockState(treePos);
			BlockBranch branch = TreeHelper.getBranch(trunkBlockState);
			NodeFindEnds endFinder = new NodeFindEnds();
			MapSignal signal = new MapSignal(endFinder);
			branch.analyse(trunkBlockState, world, treePos, EnumFacing.DOWN, signal);
			List<BlockPos> endPoints = endFinder.getEnds();

			for (BlockPos endPoint: endPoints) {
				TreeHelper.ageVolume(world, endPoint, 2, 3, 3, SafeChunkBounds.ANY);
			}

			// Make sure the bottom block is always just a little thicker that the block above it.
			int radius = branch.getRadius(world.getBlockState(treePos.up()));
			if (radius != 0) {
				branch.setRadius(world, treePos, radius + 1, null);
			}

			return super.postGrow(world, rootPos, treePos, soilLife, natural);
		}

		@Override
		public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
			for (BlockPos endPoint : endPoints) {
				TreeHelper.ageVolume(world, endPoint, 1, 2, 3, safeBounds);
			}
		}
	}

	public A2TreePalm() {
		super(new ResourceLocation(DynamicTreesAtum.MODID, "palm"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.palmLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesPalm(this));
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
