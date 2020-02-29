package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.cells.CellMetadata;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeDisease;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.blocks.BlockDynamicBranchBloodwood;
import maxhyper.dynamictreesnatura.blocks.BlockDynamicSaplingBloodwood;
import com.progwml6.natura.nether.NaturaNether;
import com.progwml6.natura.shared.NaturaCommons;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeBloodwood extends TreeFamily {

	public static Block leavesBlock = NaturaNether.netherLeaves;
    public static Block logBlock = NaturaNether.netherLog2;
    public static Block saplingBlock = NaturaNether.netherSapling;

	public class SpeciesBloodwood extends Species {

		SpeciesBloodwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.bloodwoodLeavesProperties);

			setBasicGrowingParameters(0.2f, 24.0f, 0, 10, 2.8f);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("bloodwood"));
			envFactor(Type.COLD, 0.25f);
			envFactor(Type.HOT, 1.10f);
			envFactor(Type.DRY, 0.90f);
			envFactor(Type.NETHER, 1.50f);

			setSeedStack(new ItemStack(ModContent.bloodwoodSeed));
			setupStandardSeedDropping();
			this.addAcceptableSoil(Blocks.NETHERRACK, Blocks.SOUL_SAND);
		}

		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, Biomes.HELL);
		}

		@Override public boolean isThick() {
			return true;
		}

		@Override
		public BlockRooty getRootyBlock() {
			return ModContent.rootyUpsidedownDirt;
		}

		@Override public boolean plantSapling(World world, BlockPos pos) {
			if(world.getBlockState(pos).getBlock().isReplaceable(world, pos) && BlockDynamicSaplingBloodwood.canSaplingStay(world, this, pos)) {
				ModContent.bloodwoodSapling.setSpecies(world, pos, this);
				return true;
			}

			return false;
		}
		@Override public boolean transitionToTree(World world, BlockPos pos) {
			//Ensure planting conditions are right
			TreeFamily family = getFamily();
			if(world.isAirBlock(pos.down()) && isAcceptableSoil(world, pos.up(), world.getBlockState(pos.up()))) {
				family.getDynamicBranch().setRadius(world, pos, (int)family.getPrimaryThickness(), null);//set to a single branch with 1 radius
				world.setBlockState(pos.down(), getLeavesProperties().getDynamicLeavesState());//Place a single leaf block below
				placeRootyDirtBlock(world, pos.up(), 15);//Set to fully fertilized rooty dirt above
				return true;
			}

			return false;
		}
		public AxisAlignedBB getSaplingBoundingBox() {
			return new AxisAlignedBB(0.25f, 0.25f, 0.25f, 0.75f, 1.0f, 0.75f);
		}

		private final EnumFacing downFirst[] = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST};
		@Override public boolean handleRot(World world, List<BlockPos> ends, BlockPos rootPos, BlockPos treePos, int soilLife, SafeChunkBounds safeBounds) {

			Iterator<BlockPos> iter = ends.iterator();//We need an iterator since we may be removing elements.
			SimpleVoxmap leafMap = getLeavesProperties().getCellKit().getLeafCluster();

			while (iter.hasNext()) {
				BlockPos endPos = iter.next();
				IBlockState branchState = world.getBlockState(endPos);
				BlockBranch branch = TreeHelper.getBranch(branchState);
				if(branch != null) {
					int radius = branch.getRadius(branchState);
					float rotChance = rotChance(world, endPos, world.rand, radius);
					if(branch.checkForRot(world, endPos, this, radius, world.rand, rotChance, safeBounds != SafeChunkBounds.ANY) || radius != 1) {
						if(safeBounds != SafeChunkBounds.ANY) { //worldgen
							TreeHelper.ageVolume(world, endPos.up((leafMap.getLenZ() - 1) / 2), (leafMap.getLenX() - 1) / 2, leafMap.getLenY(), 2, safeBounds);
						}
						iter.remove();//Prune out the rotted end points so we don't spawn fruit from them.
					}
				}
			}

			return ends.isEmpty() && !TreeHelper.isBranch(world.getBlockState(treePos));//There are no endpoints and the trunk is missing
		}
		@Override public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {

			if(radius <= 1) {
				BlockDynamicLeaves leaves = (BlockDynamicLeaves) getLeavesProperties().getDynamicLeavesState().getBlock();
				for(EnumFacing dir: downFirst) {
					if(leaves.growLeavesIfLocationIsSuitable(world, getLeavesProperties(), pos.offset(dir), 0)) {
						return false;
					}
				}
			}

			if(rapid || (ModConfigs.maxBranchRotRadius != 0 && radius <= ModConfigs.maxBranchRotRadius)) {
				BlockBranch branch = TreeHelper.getBranch(world.getBlockState(pos));
				if(branch != null) {
					branch.rot(world, pos);
				}
				return true;
			}

			return false;
		}
		@Override public boolean handleDisease(World world, ITreePart baseTreePart, BlockPos treePos, Random random, int soilLife) {
			if(soilLife == 0 && ModConfigs.diseaseChance > random.nextFloat() ) {
				baseTreePart.analyse(world.getBlockState(treePos), world, treePos, EnumFacing.UP, new MapSignal(new NodeDisease(this)));
				return true;
			}

			return false;
		}

		@Override public EnumFacing selectNewDirection(World world, BlockPos pos, BlockBranch branch, GrowSignal signal) {
			EnumFacing originDir = signal.dir.getOpposite();

			//prevent branches on the ground
			if(signal.numSteps + 1 <= getLowestBranchHeight(world, signal.rootPos)) {
				return EnumFacing.DOWN;
			}

			int probMap[] = new int[6];//6 directions possible DUNSWE

			//Probability taking direction into account
			probMap[EnumFacing.DOWN.ordinal()] = signal.dir != EnumFacing.UP ? getUpProbability(): 0;//Favor down
			probMap[signal.dir.ordinal()] += getReinfTravel(); //Favor current direction

			//Create probability map for direction change
			for(EnumFacing dir: EnumFacing.VALUES) {
				if(!dir.equals(originDir)) {
					BlockPos deltaPos = pos.offset(dir);
					//Check probability for surrounding blocks
					//Typically Air:1, Leaves:2, Branches: 2+r
					IBlockState deltaBlockState = world.getBlockState(deltaPos);
					probMap[dir.getIndex()] += TreeHelper.getTreePart(deltaBlockState).probabilityForBlock(deltaBlockState, world, deltaPos, branch);
				}
			}

			//Do custom stuff or override probability map for various species
			probMap = customDirectionManipulation(world, pos, branch.getRadius(world.getBlockState(pos)), signal, probMap);

			//Select a direction from the probability map
			int choice = com.ferreusveritas.dynamictrees.util.MathHelper.selectRandomFromDistribution(signal.rand, probMap);//Select a direction from the probability map
			return newDirectionSelected(EnumFacing.getFront(choice != -1 ? choice : 0), signal);//Default to down if things are screwy
		}

	}

	public TreeBloodwood() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "bloodwood"));

		setDynamicBranch(ModContent.bloodwoodBranch);

		ModContent.bloodwoodLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 15);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}
	@Override public ItemStack getStick(int qty) {
		ItemStack stick = NaturaCommons.bloodwood_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override public boolean isThick() {
        return true;
    }

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesBloodwood(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	public int getRadiusForCellKit(IBlockAccess blockAccess, BlockPos pos, IBlockState blockState, EnumFacing dir, BlockBranch branch) {
		int radius = branch.getRadius(blockState);
		int meta = CellMetadata.NONE;
		if(hasConiferVariants && radius == 1) {
			if(blockAccess.getBlockState(pos.up()).getBlock() == branch) {
				meta = CellMetadata.CONIFERTOP;
			}
		}

		return CellMetadata.radiusAndMeta(radius, meta);
	}

}
