package maxhyper.dynamictreestbl.trees;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import thebetweenlands.common.block.terrain.*;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.List;
import java.util.Objects;

public class TreeRubber extends TreeFamily {

	public static Block leavesBlock = BlockRegistry.LEAVES_RUBBER_TREE;
	public static Block logBlock = BlockRegistry.LOG_RUBBER;
	public static Block saplingBlock = BlockRegistry.SAPLING_RUBBER;

	public class SpeciesRubber extends Species {

		SpeciesRubber(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.rubberLeavesProperties);

			setBasicGrowingParameters(0f, 18.0f, 10, 12, 1.25f);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("darkoak"));
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.WET, 1.5f);
			envFactor(Type.DRY, 0.5f);
			envFactor(Type.FOREST, 1.1f);

			generateSeed();
			addDropCreator(new DropCreatorSeed(2f));

			this.addGenFeature(new FeatureGenClearVolume(12));
		}

		@Override
		public boolean isAcceptableSoil(World world, BlockPos pos, IBlockState soilBlockState) {
			return super.isAcceptableSoil(world, pos, soilBlockState)
					|| soilBlockState.getBlock() instanceof BlockSwampDirt
					|| soilBlockState.getBlock() instanceof BlockDeadGrass
					|| soilBlockState.getBlock() instanceof BlockSwampGrass
					|| soilBlockState.getBlock() instanceof BlockSludgyDirt
					|| soilBlockState.getBlock() instanceof BlockSpreadingSludgyDirt
					|| soilBlockState.getBlock() instanceof BlockMud
					|| soilBlockState.getBlock() instanceof BlockSilt
					|| (soilBlockState.getBlock() instanceof BlockCragrock && soilBlockState.getValue(BlockCragrock.VARIANT) != BlockCragrock.EnumCragrockType.DEFAULT)
					|| soilBlockState.getBlock() instanceof BlockPeat;
		}
	}

	public TreeRubber() {
		super(new ResourceLocation(DynamicTreesTBL.MODID, "rubber"));

		setDynamicBranch(ModContent.rubberBranch);
		
		ModContent.rubberLeavesProperties.setTree(this);
		
		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 4, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesRubber(this));
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		blockList.add(ModContent.rubberBranch);
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public BlockBranch createBranch() {
		return ModContent.rubberBranch;
	}
}
