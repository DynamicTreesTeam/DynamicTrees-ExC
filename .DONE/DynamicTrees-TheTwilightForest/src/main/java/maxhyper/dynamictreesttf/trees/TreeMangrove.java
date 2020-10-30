package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRoots;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenLogCritter;
import maxhyper.dynamictreesttf.genfeatures.FeatureGenUndergroundRoots;
import maxhyper.dynamictreesttf.models.BakedModelBlockBranchMangrove;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.biomes.TFBiomes;

import java.util.List;
import java.util.Objects;

public class TreeMangrove extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getStateFromMeta(2);
	public static int logsMeta = 2;
	public static int saplingMeta = 2;

	public class SpeciesMangrove extends Species {

		protected int deepSoilTypeFlags;

		SpeciesMangrove(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.mangroveLeavesProperties);
			setSoilLongevity(4);
			setBasicGrowingParameters(0.2f, 10, upProbability, 4, 0.6f);

			setSeedStack(new ItemStack(ModContent.mangroveSeed));
			setupStandardSeedDropping();
			addGenFeature(new FeatureGenLogCritter(getLowestBranchHeight(), ModContent.dynamicFirefly, 80, 2));
			addGenFeature(new FeatureGenUndergroundRoots(ModContent.undergroundRoot, ModContent.undergroundRootExposed,  6, 5, 1));
			addGenFeature(new FeatureGenMangrovelings());
		}

		@Override
		public int maxBranchRadius() { return BakedModelBlockBranchMangrove.maxRad; }

		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (newDir != EnumFacing.UP) {
				signal.energy += 0.75f;
			}
			if (newDir == EnumFacing.UP && signal.dir != EnumFacing.UP) {
				signal.energy += (Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ())) - 2f) * 1.5f;
			}
			return newDir;
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
			if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.WATERLIKE))){
				return ModContent.blockRootyWater;
			} else {
				return ModBlocks.blockRootyDirt;
			}
		}

//		@Override
//		public LogsAndSticks getLogsAndSticks(float volume) {
//			return super.getLogsAndSticks(volume * 1);
//		}

		@Override
		public boolean isBiomePerfect(Biome biome) {
			return isOneOfBiomes(biome, TFBiomes.tfSwamp, TFBiomes.fireSwamp);
		};

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
			return super.customDirectionManipulation(world, pos, radius, signal, probMap);
		}

		@Override
		protected void setStandardSoils() {
			addAcceptableSoils(DirtHelper.WATERLIKE, DirtHelper.DIRTLIKE);
			deepSoilTypeFlags = DirtHelper.getSoilFlags(DirtHelper.DIRTLIKE, DirtHelper.SANDLIKE, DirtHelper.GRAVELLIKE);
		}

		@Override
		public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
			boolean requiresBottom = DirtHelper.isSoilAcceptable(world.getBlockState(pos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.WATERLIKE));
			return super.isAcceptableSoil(world, pos, soilBlockState) && (!requiresBottom || DirtHelper.isSoilAcceptable(world.getBlockState(pos.down()).getBlock(), deepSoilTypeFlags));
		}
	}

	public TreeMangrove() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "mangrove"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		ModContent.mangroveLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesMangrove(this));
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
	public BlockBranch createBranch() {
		return new BlockBranchTwilight("mangroveBranch") {
			@Override
			public int getMaxRadius() {
				return BakedModelBlockBranchMangrove.maxRad;
			}

			@Override
			public BlockRenderLayer getBlockLayer() {
				return BlockRenderLayer.CUTOUT_MIPPED;
			}
		};
	}

	public static class FeatureGenMangrovelings implements IPostGenFeature {

		@Override
		public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {

			int[] angles = new int[2];
			angles[0] = angles[1] = world.rand.nextInt(6);
			while(angles[0] == angles[1]) {
				angles[1] = world.rand.nextInt(6);
			}

			anglesLoop:
			for(int a : angles) {
				double angle = Math.toRadians(a * 60.0f);
				float distance = 3.0f + world.rand.nextFloat() * 2.0f;
				BlockPos offPos = rootPos.add(new Vec3i(Math.sin(angle) * distance, 0, Math.cos(angle) * distance));

				if(safeBounds.inBounds(offPos, true)) {
					if(species.isAcceptableSoil(world, offPos, world.getBlockState(offPos))) {
						if( !(world.isAirBlock(offPos.up(1)) && world.isAirBlock(offPos.up(2))) ) {
							continue anglesLoop;
						}
						for(EnumFacing hor : EnumFacing.HORIZONTALS) {
							BlockPos offPos2 = offPos.offset(hor);
							if( !(world.isAirBlock(offPos2.up(1)) && world.isAirBlock(offPos2.up(2))) ) {
								continue anglesLoop;
							}
						}

						world.setBlockState(offPos, species.getRootyBlock(world, offPos).getDefaultState().withProperty(BlockRooty.LIFE, 0));
						species.getFamily().getDynamicBranch().setRadius(world, offPos.up(1), 1, EnumFacing.DOWN, 0);
						if(world.rand.nextInt(2) == 0) {
							world.setBlockState(offPos.up(2), species.getLeavesProperties().getDynamicLeavesState(1));
						} else {
							species.getFamily().getDynamicBranch().setRadius(world, offPos.up(2), 1, EnumFacing.DOWN, 0);
							if(world.isAirBlock(offPos.up(3))) {
								world.setBlockState(offPos.up(3), species.getLeavesProperties().getDynamicLeavesState(1));
							}
						}
					}
				}
			}

			return false;
		}

	}

}
