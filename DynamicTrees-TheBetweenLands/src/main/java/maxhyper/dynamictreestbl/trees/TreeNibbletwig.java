package maxhyper.dynamictreestbl.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thebetweenlands.common.block.terrain.*;
import thebetweenlands.common.registries.BlockRegistry;

import java.util.List;
import java.util.Objects;

public class TreeNibbletwig extends TreeFamily {

	public static Block leavesBlock = BlockRegistry.LEAVES_NIBBLETWIG_TREE;
	public static Block logBlock = BlockRegistry.LOG_NIBBLETWIG;
	public static Block saplingBlock = BlockRegistry.SAPLING_NIBBLETWIG;

	public class SpeciesSap extends Species {

		SpeciesSap(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.nibbletwigLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy, upProbability, lowestBranchHeight, growthRate);

			generateSeed();

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

	public TreeNibbletwig() {
		super(new ResourceLocation(DynamicTreesTBL.MODID, "nibbletwig"));

		ModContent.nibbletwigLeavesProperties.setTree(this);
		
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
		setCommonSpecies(new SpeciesSap(this));
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		return super.getRegisterableBlocks(blockList);
	}
	
}
