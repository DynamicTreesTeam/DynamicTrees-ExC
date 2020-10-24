package maxhyper.dynamictreesintegrateddynamics.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import maxhyper.dynamictreesintegrateddynamics.DynamicTreesIntegratedDynamics;
import maxhyper.dynamictreesintegrateddynamics.ModConfigs;
import maxhyper.dynamictreesintegrateddynamics.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.cyclops.integrateddynamics.world.biome.BiomeMeneglin;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeMenril extends TreeFamily {

	public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("integrateddynamics","menril_leaves"));
	public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("integrateddynamics","menril_log"));
	public static Block saplingBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("integrateddynamics","menril_sapling"));

	public class SpeciesMenril extends Species {
		SpeciesMenril(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.menrilLeavesProperties);

			setBasicGrowingParameters(1.2f, 9.0f, 0, 5, 0.2f);
						
			envFactor(Type.COLD, 1.1f);
			envFactor(Type.HOT, 0.75f);
			envFactor(Type.DRY, 0.5f);
			envFactor(Type.FOREST, 1.05f);
			envFactor(Type.MAGICAL, 1.1f);
			
			generateSeed();
			addDropCreator(new DropCreatorSeed(1f));
			if (ModConfigs.menrilBerriesOnMenrilTrees)
				this.addGenFeature((new FeatureGenFruit(ModContent.blockMenrilBerries)));
			addGenFeature(new FeatureGenRoots(8).setScaler(getRootScaler()));//Finally Generate Roots
			addGenFeature(new FeatureGenMound(7));
		}

		@Override
		public boolean isBiomePerfect(Biome biome) {
			return biome.equals(BiomeMeneglin.getInstance());
		}

		@Override
		protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
			if (signal.isInTrunk() && newDir != EnumFacing.UP) { // Turned out of trunk
				signal.energy *= 0.3f;
				if (signal.energy > 3) signal.energy = 3;
			}
			return newDir;
		}
		
		protected BiFunction<Integer, Integer, Integer> getRootScaler() {
			return (inRadius, trunkRadius) -> {
				float scale = MathHelper.clamp(trunkRadius >= 8 ? (trunkRadius / 10f) : 0, 0, 1);
				return (int) (inRadius * scale);
			};
		}

		@Override
		public float getEnergy(World world, BlockPos pos) {
			long day = world.getWorldTime() / 24000L;
			int month = (int) day / 30; // Change the hashs every in-game month

			return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3); // Vary the height energy by a psuedorandom hash function
		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}
	}

	public TreeMenril() {
		super(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, "menril"));

		setPrimitiveLog(logBlock.getDefaultState());

		setDynamicBranch(ModContent.menrilBranch);
		ModContent.menrilBranchFilled.setFamily(this);
		
		ModContent.menrilLeavesProperties.setTree(this);
		
		ModContent.menrilRoot = new BlockSurfaceRoot(Material.WOOD, getName() + "root");
		
		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 5, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesMenril(this));
	}

	@Override
	public BlockSurfaceRoot getSurfaceRoots() {
		return ModContent.menrilRoot;
	}
	
	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(ModContent.menrilRoot);
		return super.getRegisterableBlocks(blockList);
	}
	
}
