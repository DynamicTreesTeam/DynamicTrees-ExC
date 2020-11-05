package maxhyper.dynamictreesatum.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.cells.CellMetadata;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeShrinker;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import maxhyper.dynamictreesatum.DynamicTreesAtum;
import maxhyper.dynamictreesatum.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Biomes;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeDeadTree extends TreeFamily {

	public static Block logBlock =  AtumBlocks.DEADWOOD_LOG;

	public class SpeciesDeadTree extends Species {

		SpeciesDeadTree(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.nullLeavesProperties);

			setBasicGrowingParameters(0f, 10.0f, upProbability, lowestBranchHeight, 100);

			envFactor(Type.COLD, 0.25f);

		}

		@Override
		protected void setStandardSoils() {
			addAcceptableSoils(DirtHelper.SANDLIKE, DirtHelper.GRAVELLIKE);
		}

		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.MUTATED_DESERT);
		}

		@Override
		public boolean handleRot(World world, List<BlockPos> ends, BlockPos rootPos, BlockPos treePos, int soilLife, SafeChunkBounds safeBounds) {
			return false;
		}

		@Override
		public boolean canBoneMeal() {
			return ModConfigs.worldGenDebug;
		}

		@Override
		public boolean grow(World world, BlockRooty rootyDirt, BlockPos rootPos, int soilLife, ITreePart treeBase, BlockPos treePos, Random random, boolean natural) {
			if (ModConfigs.worldGenDebug){
				float growthRate = getGrowthRate(world, rootPos) * ModConfigs.treeGrowthMultiplier * ModConfigs.treeGrowthFolding;
				do {
					if(soilLife > 0){
						if(growthRate > random.nextFloat()) {
							GrowSignal signal = new GrowSignal(this, rootPos, getEnergy(world, rootPos));
							boolean success = treeBase.growSignal(world, treePos, signal).success;

							int soilLongevity = getSoilLongevity(world, rootPos) * (success ? 1 : 16);//Don't deplete the soil as much if the grow operation failed

							if(soilLongevity <= 0 || random.nextInt(soilLongevity) == 0) {//1 in X(soilLongevity) chance to draw nutrients from soil
								rootyDirt.setSoilLife(world, rootPos, soilLife - 1);//decrement soil life
							}

							if(signal.choked) {
								soilLife = 0;
								rootyDirt.setSoilLife(world, rootPos, soilLife);
								TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(new NodeShrinker(signal.getSpecies())));
							}
						}
					}
				} while(--growthRate > 0.0f);

				return postGrow(world, rootPos, treePos, soilLife, natural);
			} else return false;
		}

		public SoundType getSaplingSound() {
			return SoundType.WOOD;
		}

		@Override
		public boolean transitionToTree(World world, BlockPos pos) {
			//Ensure planting conditions are right
			TreeFamily tree = getFamily();
			if(world.isAirBlock(pos.up()) && isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()))) {
				IBlockState branchState = tree.getDynamicBranch().getDefaultState();
				BlockBranch branch = TreeHelper.getBranch(branchState);
				world.setBlockState(pos, branchState);//set to a single branch
				branch.setRadius(world, pos, 4, EnumFacing.DOWN);

				placeRootyDirtBlock(world, pos.down(), 15);//Set to fully fertilized rooty sand underneath
				return true;
			}

			return false;
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos pos) {
			if (DirtHelper.isSoilAcceptable(world.getBlockState(pos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.SANDLIKE))){
				return ModBlocks.blockRootySand;
			} else {
				return ModBlocks.blockRootyDirt;
			}
		}

		//CODE FROM DYNAMICTREES-BOP
		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			EnumFacing originDir = signal.dir.getOpposite();

			// Alter probability map for direction change
			probMap[0] = probMap[2] = probMap[3] = probMap[4] = probMap[5] = probMap[1] = 10;
			probMap[originDir.ordinal()] = 0; // Disable the direction we came from and front
			if (!signal.isInTrunk()){
				probMap[originDir.getOpposite().ordinal()] = 0;
			}

			return probMap;
		}
	}

	public TreeDeadTree() {
		super(new ResourceLocation(DynamicTreesAtum.MODID, "deadtree"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.nullLeavesProperties.setTree(this);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock));
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public ItemStack getStick(int qty) {
		return new ItemStack(Objects.requireNonNull(Item.getByNameOrId("atum:deadwood_stick")));
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesDeadTree(this));
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
	public ILeavesProperties getCommonLeaves() {
		return ModBlocks.leaves.get(getName().getResourcePath());
	}

	@Override
	public float getPrimaryThickness() {
		return 3f;
	}

	@Override
	public float getSecondaryThickness() {
		return 3f;
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = this.getName() + "branch";
		return new BlockBranchBasic(branchName){

			@Override
			public void rot(World world, BlockPos pos) {}

			@Override
			public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
				return false;
			}

			@Override
			public GrowSignal growIntoAir(World world, BlockPos pos, GrowSignal signal, int fromRadius) {
				Species species = signal.getSpecies();
				BlockDynamicLeaves leaves = TreeHelper.getLeaves(species.getLeavesProperties().getDynamicLeavesState());
				return leaves.branchOut(world, pos, signal);
			}
			@Override
			public GrowSignal growSignal(World world, BlockPos pos, GrowSignal signal) {

				if (signal.step()) {// This is always placed at the beginning of every growSignal function

					IBlockState currBlockState = world.getBlockState(pos);
					Species species = signal.getSpecies();
					boolean inTrunk = signal.isInTrunk();

					EnumFacing originDir = signal.dir.getOpposite();// Direction this signal originated from
					EnumFacing targetDir = species.selectNewDirection(world, pos, this, signal);// This must be cached on the stack for proper recursion
					signal.doTurn(targetDir);

					{
						BlockPos deltaPos = pos.offset(targetDir);
						IBlockState deltaState = world.getBlockState(deltaPos);

						// Pass grow signal to next block in path
						ITreePart treepart = TreeHelper.getTreePart(deltaState);
						if (treepart != TreeHelper.nullTreePart) {
							signal = treepart.growSignal(world, deltaPos, signal);// Recurse
						} else if (world.isAirBlock(deltaPos) || deltaState.getBlock() == ModBlocks.blockTrunkShell) {
							signal = growIntoAir(world, deltaPos, signal, getRadius(currBlockState));
						}
					}

					// Calculate Branch Thickness based on neighboring branches
					float areaAccum = signal.radius * signal.radius;// Start by accumulating the branch we just came from

					for (EnumFacing dir : EnumFacing.VALUES) {
						if (!dir.equals(originDir) && !dir.equals(targetDir)) {// Don't count where the signal originated from or the branch we just came back from
							BlockPos deltaPos = pos.offset(dir);

							// If it is decided to implement a special block(like a squirrel hole, tree
							// swing, rotting, burned or infested branch, etc) then this new block could be
							// derived from BlockBranch and this works perfectly. Should even work with
							// tileEntity blocks derived from BlockBranch.
							IBlockState blockState = world.getBlockState(deltaPos);
							ITreePart treepart = TreeHelper.getTreePart(blockState);
							if (isSameTree(treepart)) {
								int branchRadius = treepart.getRadius(blockState);
								areaAccum += branchRadius * branchRadius;
							}
						}
					}

					//Only continue to set radii if the tree growth isn't choked out
					if(!signal.choked) {
						// Ensure that side branches are not thicker than the size of a block.  Also enforce species max thickness
						int maxRadius = inTrunk ? 8 : 4;

						// The new branch should be the square root of all of the sums of the areas of the branches coming into it.
						// But it shouldn't be smaller than it's current size(prevents the instant slimming effect when chopping off branches)
						signal.radius = MathHelper.clamp(getRadius(currBlockState)+1, getRadius(currBlockState), maxRadius);// WOW!
						int targetRadius = (int) Math.floor(signal.radius);
						int setRad = setRadius(world, pos, targetRadius, originDir);
						if(setRad < targetRadius) { //We tried to set a radius but it didn't comply because something is in the way.
							signal.choked = true; //If something is in the way then it means that the tree growth is choked
						}
					}
				}

				return signal;
			}
		};
	}
}
