package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.cells.CellMetadata;
import com.ferreusveritas.dynamictrees.event.SpeciesPostGenerationEvent;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeCoder;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeDisease;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import com.progwml6.natura.nether.NaturaNether;
import com.progwml6.natura.nether.block.leaves.BlockNetherLeaves;
import com.progwml6.natura.shared.NaturaCommons;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.blocks.BlockDynamicSaplingBloodwood;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;

public class TreeBloodwood extends TreeFamily {

	public static Block leavesBlock = NaturaNether.netherLeaves;
    public static Block logBlock = NaturaNether.netherLog2;
    public static Block saplingBlock = NaturaNether.netherSapling2;
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockNetherLeaves.TYPE, BlockNetherLeaves.LeavesType.BLOODWOOD);

	public class SpeciesBloodwood extends Species {

		SpeciesBloodwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.bloodwoodLeavesProperties);

			setBasicGrowingParameters(0.2f, 24.0f, 0, 10, 2.8f);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("bloodwood"));

			envFactor(Type.COLD, 0.75f);

			setSeedStack(new ItemStack(ModContent.bloodwoodSeed));
			setupStandardSeedDropping();

			addAcceptableSoils(DirtHelper.NETHERLIKE);
		}

		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, Biomes.HELL);
		}

		public boolean isAcceptableSoilForWorldgen(World world, BlockPos pos, IBlockState soilBlockState) {
			return true; //This method checks the block on the ground, but since bloodwood generates on the ceiling we don't care about this
		}

		@Override public boolean isThick() {
			return true;
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos pos) {
			if (DirtHelper.isSoilAcceptable(world.getBlockState(pos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.NETHERLIKE))){
				return ModContent.rootyNetherUpsidedownDirt;
			} else {
				return ModContent.rootyUpsidedownDirt;
			}
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

		private final EnumFacing[] downFirst = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST};
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

			int[] probMap = new int[6];//6 directions possible DUNSWE

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

		BlockPos findCeiling(World world, BlockPos pos) {
			BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos(pos);
			do {
				if (isAcceptableSoil(world.getBlockState(testPos)) && !world.getBlockState(testPos.down()).isFullBlock()) {
					BlockPos returnPos = testPos.toImmutable().down();
					if (returnPos.getY() - pos.getY() >= getEnergy(world, returnPos)/1.5f)
						return returnPos;
					return null;
				}
				testPos.move(EnumFacing.UP);
			}
			while (!world.isOutsideBuildHeight(testPos));
			return null;
		}

		@Override
		public boolean generate(World world, BlockPos rootPos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
			BlockPos ceiling = findCeiling(world, rootPos.up());
			if (ceiling != null && world.isBlockLoaded(ceiling)){
				//generate sapling for now until i figure out how to do the goddamned jocodes
				plantSapling(world, ceiling);
				return true;
			}
			return false;
		}

//		@Override
//		public JoCode getJoCode(String joCodeString) {
//			return new JoCodeBloodwood(joCodeString);
//		}
//
//		@Override
//		public JoCode getJoCode(World world, BlockPos rootPos, EnumFacing facing) {
//			return new JoCodeBloodwood(world, rootPos, facing);
//		}
//
//		protected class JoCodeBloodwood extends JoCode {
//
//			public JoCodeBloodwood(String code) {
//				super(code);
//			}
//
//			public JoCodeBloodwood(World world, BlockPos rootPos, EnumFacing facing) {
//				super(world, rootPos, facing);
//				Optional<BlockBranch> branch = TreeHelper.getBranchOpt(world.getBlockState(rootPos.down()));
//
//				if(branch.isPresent()) {
//					NodeCoder coder = new NodeCoder();
//					//Warning!  This sends a RootyBlock BlockState into a branch for the kickstart of the analysis.
//					branch.get().analyse(world.getBlockState(rootPos), world, rootPos, EnumFacing.UP, new MapSignal(coder));
//					instructions = coder.compile(this);
//					rotate(facing);
//				}
//			}
//
//			public void generate(World world, Species species, BlockPos rootPosIn, Biome biome, EnumFacing facing, int radius, SafeChunkBounds safeBounds) {
//
//				boolean worldGen = safeBounds != SafeChunkBounds.ANY;
//
//				//A Tree generation boundary radius is at least 2 and at most 8
//				radius = MathHelper.clamp(radius, 2, 8);
//
//				setFacing(facing);
//				BlockPos rootPos = species.preGeneration(world, rootPosIn, radius, facing, safeBounds, this);
//
//				if(rootPos != BlockPos.ORIGIN) {
//					IBlockState initialDirtState = world.getBlockState(rootPos);//Save the initial state of the dirt in case this fails
//					species.placeRootyDirtBlock(world, rootPos, 0);//Set to unfertilized rooty dirt
//
//					//Make the tree branch structure
//					generateFork(world, species, 0, rootPos, false);
//
//					// Establish a position for the bottom block of the trunk
//					BlockPos treePos = rootPos.down();
//
//					// Fix branch thicknesses and map out leaf locations
//					IBlockState treeState = world.getBlockState(treePos);
//					BlockBranch branch = TreeHelper.getBranch(treeState);
//					if(branch != null) {// If a branch exists then the growth was successful
//						ILeavesProperties leavesProperties = species.getLeavesProperties();
//						SimpleVoxmap leafMap = new SimpleVoxmap(radius * 2 + 1, species.getWorldGenLeafMapHeight(), radius * 2 + 1).setMapAndCenter(treePos, new BlockPos(radius, -species.getWorldGenLeafMapHeight(), radius));
//						INodeInspector inflator = species.getNodeInflator(leafMap);// This is responsible for thickening the branches
//						NodeFindEnds endFinder = new NodeFindEnds();// This is responsible for gathering a list of branch end points
//						MapSignal signal = new MapSignal(inflator, endFinder);// The inflator signal will "paint" a temporary voxmap of all of the leaves and branches.
//						signal.destroyLoopedNodes = careful;// During worldgen we will not destroy looped nodes
//						branch.analyse(treeState, world, treePos, EnumFacing.UP, signal);
//						if(signal.found || signal.overflow) {// Something went terribly wrong.
//							DynamicTrees.log.debug("Non-viable branch network detected during world generation @ " + treePos);
//							DynamicTrees.log.debug("Species: " + species);
//							DynamicTrees.log.debug("Radius: " + radius);
//							DynamicTrees.log.debug("JoCode: " + this);
//
//							// Completely blow away any improperly defined network nodes
//							cleanupFrankentree(world, treePos, treeState, endFinder.getEnds(), safeBounds);
//							// Now that everything is clear we may as well regenerate the tree that screwed everything up.
//							if(!secondChanceRegen) {
//								secondChanceRegen = true;
//								generate(world, species, rootPosIn, biome, facing, radius, safeBounds);
//							}
//							secondChanceRegen = false;
//							return;
//						}
//						List<BlockPos> endPoints = endFinder.getEnds();
//
//						smother(leafMap, leavesProperties);//Use the voxmap to precompute leaf smothering so we don't have to age it as many times.
//
//						//Place Growing Leaves Blocks from voxmap
//						for(SimpleVoxmap.Cell cell: leafMap.getAllNonZeroCells((byte) 0x0F)) {//Iterate through all of the cells that are leaves(not air or branches)
//							BlockPos.MutableBlockPos cellPos = cell.getPos();
//							if(safeBounds.inBounds(cellPos, false)) {
//								IBlockState testBlockState = world.getBlockState(cellPos);
//								Block testBlock = testBlockState.getBlock();
//								if(testBlock.isReplaceable(world, cellPos)) {
//									world.setBlockState(cellPos, leavesProperties.getDynamicLeavesState(cell.getValue()), worldGen ? 16 : 2);//Flag 16 to prevent observers from causing cascading lag
//								}
//							} else {
//								leafMap.setVoxel(cellPos, (byte) 0);
//							}
//						}
//
//						//Shrink the leafMap down by the safeBounds object so that the aging process won't look for neighbors outside of the bounds.
//						for(SimpleVoxmap.Cell cell: leafMap.getAllNonZeroCells()) {
//							BlockPos.MutableBlockPos cellPos = cell.getPos();
//							if(!safeBounds.inBounds(cellPos, true)) {
//								leafMap.setVoxel(cellPos, (byte) 0);
//							}
//						}
//
//						//Age volume for 3 cycles using a leafmap
//						TreeHelper.ageVolume(world, leafMap, species.getWorldGenAgeIterations(), safeBounds);
//
//						//Rot the unsupported branches
//						if(species.handleRot(world, endPoints, rootPos, treePos, 0, safeBounds)) {
//							return;//The entire tree rotted away before it had a chance
//						}
//
//						//Allow for special decorations by the tree itself
//						species.postGeneration(world, rootPos, biome, radius, endPoints, safeBounds, initialDirtState);
//						MinecraftForge.EVENT_BUS.post(new SpeciesPostGenerationEvent(world, species, rootPos, endPoints, safeBounds, initialDirtState));
//
//						//Add snow to parts of the tree in chunks where snow was already placed
//						addSnow(leafMap, world, rootPos, biome);
//
//					} else { //The growth failed.. turn the soil back to what it was
//						world.setBlockState(rootPos, initialDirtState, careful ? 3 : 2);
//					}
//				}
//			}
//
//			protected void smother(SimpleVoxmap leafMap, ILeavesProperties leavesProperties) {
//
//				int smotherMax = leavesProperties.getSmotherLeavesMax();
//
//				if(smotherMax != 0) { //Smothering is disabled if set to 0
//
//					BlockPos saveCenter = leafMap.getCenter();
//					leafMap.setCenter(new BlockPos(0, -leafMap.getLenY(), 0));
//
//					int startY;
//
//					//Find topmost block in build volume
//					for(startY = leafMap.getLenY() - 1; startY >= 0; startY--) {
//						if(leafMap.isYTouched(startY)) {
//							break;
//						}
//					}
//
//					//Precompute smothering
//					for(int iz = 0; iz < leafMap.getLenZ(); iz++) {
//						for(int ix = 0; ix < leafMap.getLenX(); ix++) {
//							int count = 0;
//							for(int iy = startY; iy >= 0; iy--) {
//								int v = leafMap.getVoxel(new BlockPos(ix, iy, iz));
//								if(v == 0) {//Air
//									count = 0;//Reset the count
//								} else
//								if((v & 0x0F) != 0) {//Leaves
//									count++;
//									if(count > smotherMax){//Smother value
//										leafMap.setVoxel(new BlockPos(ix, iy, iz), (byte)0);
//									}
//								} else
//								if((v & 0x10) != 0) {//Twig
//									count++;
//									leafMap.setVoxel(new BlockPos(ix, iy + 1, iz), (byte)4);
//								}
//							}
//						}
//					}
//
//					leafMap.setCenter(saveCenter);
//				}
//
//			}
//		}

	}

	public TreeBloodwood() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "bloodwood"));

		setDynamicBranch(ModContent.bloodwoodBranch);

		setPrimitiveLog(logBlock.getDefaultState());

		ModContent.bloodwoodLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 0);//15);
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
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(ModContent.bloodwoodSapling);
		return super.getRegisterableBlocks(blockList);
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
