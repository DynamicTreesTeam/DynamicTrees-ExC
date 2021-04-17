package maxhyper.dynamictreestbl.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
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

			setBasicGrowingParameters(0.2f, 18.0f, 10, 12, 1.2f); //
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("darkoak"));
			envFactor(Type.COLD, 0.75f);
			envFactor(Type.WET, 1.5f);
			envFactor(Type.DRY, 0.5f);
			envFactor(Type.FOREST, 1.1f);

			generateSeed();
			setupStandardSeedDropping();

			this.addGenFeature(new FeatureGenClearVolume(12));
			addAcceptableSoils(DirtHelper.MUDLIKE);
		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}

		@Override
		public LogsAndSticks getLogsAndSticks(float volume) {
			return super.getLogsAndSticks(volume*4);
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
			if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.SANDLIKE))){
				return ModBlocks.blockRootySand;
			} else if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.MUDLIKE))){
				return ModContent.blockRootyMud;
			}else {
				return ModBlocks.blockRootyDirt;
			}
		}
	}

	public TreeRubber() {
		super(new ResourceLocation(DynamicTreesTBL.MODID, "rubber"));

		setPrimitiveLog(logBlock.getDefaultState());
		setDynamicBranch(ModContent.rubberBranch);

		ModContent.rubberLeavesProperties.setTree(this);
		
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
		setCommonSpecies(new SpeciesRubber(this));
	}

	@Override
	public BlockBranch createBranch() {
		return ModContent.rubberBranch;
	}
}
