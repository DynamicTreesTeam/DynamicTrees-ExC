package maxhyper.dynamictreesatum.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import maxhyper.dynamictreesatum.DynamicTreesAtum;
import maxhyper.dynamictreesatum.ModContent;
import maxhyper.dynamictreesatum.blocks.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesatum.genfeatures.FeatureGenFruitPod;
import maxhyper.dynamictreesatum.genfeatures.FeatureGenOphidianTongue;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class A2TreePalm extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("atum:palm_leaves");
	public static Block logBlock = AtumBlocks.PALM_LOG;
	public static Block saplingBlock = Block.getBlockFromName("atum:palm_sapling");

	public class SpeciesPalm extends Species {

		SpeciesPalm(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.palmLeavesProperties);

			//Dark Oak Trees are tall, slowly growing, thick trees
			setBasicGrowingParameters(0.5f, 8.0f, 4, 3, 0.8f);

			generateSeed();
			//setupStandardSeedDropping();

			addGenFeature(new FeatureGenFruitPod((BlockDate)AtumBlocks.DATE_BLOCK));
			addGenFeature(new FeatureGenOphidianTongue());

			addAcceptableSoil(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("atum", "fertile_soil")));

			addDropCreator(new DropCreatorSeed(3.0f) {
				@Override
				public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
					if(random.nextInt(16) == 0) { // 1 in 4 chance to drop a seed on destruction..
						dropList.add(getFruit());
					}
					return dropList;
				}

				private ItemStack getFruit (){
					return new ItemStack(AtumItems.DATE);
				}

				@Override
				public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
					int chance = 16;
					if (fortune > 0) {
						chance -= fortune;
						if (chance < 3) {
							chance = 3;
						}
					}
					if (random.nextInt(chance) == 0) {
						dropList.add(getFruit());
					}
					return dropList;
				}
			});
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();

			// Alter probability map for direction change
			probMap[0] = 0; // Down is always disallowed for palm
			probMap[1] = 10;
			probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from

			return probMap;
		}

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

		@Override
		public boolean placeRootyDirtBlock(World world, BlockPos rootPos, int life) {
			if (world.getBlockState(rootPos).getMaterial() == Material.SAND) {
				world.setBlockState(rootPos, ModBlocks.blockRootySand.getDefaultState().withProperty(BlockRooty.LIFE, life));
			} else {
				world.setBlockState(rootPos, getRootyBlock().getDefaultState().withProperty(BlockRooty.LIFE, life));
			}
			return true;
		}
	}

	public A2TreePalm() {
		super(new ResourceLocation(DynamicTreesAtum.MODID, "palm"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.palmLeavesProperties.setTree(this);

		this.canSupportCocoa = true;

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
		return new ItemStack(Objects.requireNonNull(Item.getByNameOrId("atum:palm_stick")));
	}
	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesPalm(this));
	}

	@Override
	public float getPrimaryThickness() {
		return 3.0f;
	}

	@Override
	public float getSecondaryThickness() {
		return 3.0f;
	}

	@Override
	public HashMap<BlockPos, IBlockState> getFellingLeavesClusters(BranchDestructionData destructionData) {

		if(destructionData.getNumEndpoints() < 1) {
			return null;
		}

		HashMap<BlockPos, IBlockState> leaves = new HashMap<>();
		BlockPos relPos = destructionData.getEndPointRelPos(0).up();//A palm tree is only supposed to have one endpoint at it's top.
		ILeavesProperties leavesProperties = getCommonSpecies().getLeavesProperties();

		leaves.put(relPos, leavesProperties.getDynamicLeavesState(4));//The barky overlapping part of the palm frond cluster
		leaves.put(relPos.up(), leavesProperties.getDynamicLeavesState(3));//The leafy top of the palm frond cluster

		//The 4 corners and 4 sides of the palm frond cluster
		for(int hydro = 1; hydro <= 2; hydro++) {
			IExtendedBlockState extState = (IExtendedBlockState) leavesProperties.getDynamicLeavesState(hydro);
			for(CoordUtils.Surround surr : BlockDynamicLeavesPalm.hydroSurroundMap[hydro]) {
				leaves.put(relPos.add(surr.getOpposite().getOffset()), extState.withProperty(BlockDynamicLeavesPalm.CONNECTIONS[surr.ordinal()], true));
			}
		}

		return leaves;
	}

}
