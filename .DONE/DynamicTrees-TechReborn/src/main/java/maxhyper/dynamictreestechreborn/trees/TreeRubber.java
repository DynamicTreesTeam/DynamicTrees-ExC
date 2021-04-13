package maxhyper.dynamictreestechreborn.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestechreborn.DynamicTreesTechReborn;
import maxhyper.dynamictreestechreborn.ModConfigs;
import maxhyper.dynamictreestechreborn.ModContent;
import maxhyper.dynamictreestechreborn.genfeatures.FeatureGenSapLog;
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

import java.util.List;
import java.util.Objects;

public class TreeRubber extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("techreborn:rubber_leaves");
	public static Block logBlock = Block.getBlockFromName("techreborn:rubber_log");
	public static Block saplingBlock = Block.getBlockFromName("techreborn:rubber_sapling");

	public class SpeciesRubber extends Species {

		SpeciesRubber(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.rubberLeavesProperties);

			setSoilLongevity(2);

			if (ModConfigs.classicLookingRubberTree) {
				setBasicGrowingParameters(0.9f, 10.0f, 6, 4, 0.8f);
				this.setGrowthLogicKit(new ConiferLogic(4f).setHeightVariation(2));
				this.addGenFeature(new FeatureGenConiferTopper(ModContent.rubberLeavesProperties));
			} else {
				setBasicGrowingParameters(0.2f, 15.0f, 10, 10, 1.25f);
				this.addGenFeature(new FeatureGenClearVolume(12));
			}
			this.addGenFeature(new FeatureGenSapLog(10, ModContent.rubberBranch, ModContent.rubberBranchFilled)
					.setFruitingRadius(5).setSapChance(ModConfigs.sapChance));

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.WET, 1.5f);
			envFactor(Type.DRY, 0.5f);
			envFactor(Type.FOREST, 1.1f);

			generateSeed();
			setupStandardSeedDropping();

		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}

		@Override
		public void addJoCodes() {
			if (ModConfigs.classicLookingRubberTree)
				joCodeStore.addCodesFromFile(this, "assets/" + DynamicTreesTechReborn.MODID + "/trees/rubber_classic.txt");
			else
				joCodeStore.addCodesFromFile(this, "assets/" + DynamicTreesTechReborn.MODID + "/trees/rubber.txt");
		}
	}

	public TreeRubber() {
		super(new ResourceLocation(DynamicTreesTechReborn.MODID, "rubber"));

		//Activates the conifer tops
		hasConiferVariants = true;

		addValidBranches(ModContent.rubberBranchFilled);

		setDynamicBranch(ModContent.rubberBranch);
		ModContent.rubberBranchFilled.setFamily(this);

		setPrimitiveLog(logBlock.getDefaultState());

		ModContent.rubberLeavesProperties.setTree(this);
		
		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 0);
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
