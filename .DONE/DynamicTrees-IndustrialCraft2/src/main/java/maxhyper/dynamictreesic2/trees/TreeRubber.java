package maxhyper.dynamictreesic2.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import maxhyper.dynamictreesic2.ModConfigs;
import maxhyper.dynamictreesic2.ModContent;
import maxhyper.dynamictreesic2.compat.IC2Proxy;
import maxhyper.dynamictreesic2.genfeatures.FeatureGenSapLog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Objects;

import static maxhyper.dynamictreesic2.DynamicTreesIC2.proxyIC2;

public class TreeRubber extends TreeFamily {

	public static class SpeciesRubberIC extends Species {

		SpeciesRubberIC(TreeFamily treeFamily) {
			super(new ResourceLocation(DynamicTreesIC2.MODID, "rubber"), treeFamily, ModContent.rubberLeavesProperties);

			setSoilLongevity(2);

			if (ModConfigs.classicLookingRubberTree) {
				setBasicGrowingParameters(0.9f, 10.0f, 6, 4, 0.8f);
				this.setGrowthLogicKit(new ConiferLogic(4f).setHeightVariation(2));
				this.addGenFeature(new FeatureGenConiferTopper(ModContent.rubberLeavesProperties));
			} else {
				setBasicGrowingParameters(0.2f, 14.0f, 10, 8, 1.25f);
				this.addGenFeature(new FeatureGenClearVolume(12));
			}
			this.addGenFeature(new FeatureGenSapLog(10, ModContent.rubberBranch, ModContent.rubberBranchEmpty, ModContent.rubberBranchFilled)
					.setFruitingRadius(5).setEmptyToHoleChance(ModConfigs.holeChance).setHoleToFilledChance(ModConfigs.sapChance));

			envFactor(BiomeDictionary.Type.COLD, 0.75f);
			envFactor(BiomeDictionary.Type.WET, 1.5f);
			envFactor(BiomeDictionary.Type.DRY, 0.5f);
			envFactor(BiomeDictionary.Type.FOREST, 1.1f);

			generateSeed();
			setupStandardSeedDropping();
		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}

		@Override
		public Species generateSeed() {
			Seed seed = new Seed(proxyIC2.IC2GetTreeID() + "seed");
			setSeedStack(new ItemStack(seed));
			return this;
		}

		@Override
		public ResourceLocation getSaplingName() {
			return new ResourceLocation(DynamicTreesIC2.MODID, proxyIC2.IC2GetTreeID());
		}

		@Override
		public void addJoCodes() {
			if (ModConfigs.classicLookingRubberTree)
				joCodeStore.addCodesFromFile(this, "assets/" + DynamicTreesIC2.MODID + "/trees/rubber_classic.txt");
			else
				joCodeStore.addCodesFromFile(this, "assets/" + DynamicTreesIC2.MODID + "/trees/rubber.txt");
		}
	}

	public TreeRubber() {
		super(new ResourceLocation(DynamicTreesIC2.MODID, proxyIC2.IC2GetTreeID()));

		//Activates the conifer tops
		hasConiferVariants = true;

		//setPrimitiveLog(proxyIC2.IC2GetTreeBlocks(IC2Proxy.TreeBlock.LOG));

		addValidBranches(ModContent.rubberBranchEmpty, ModContent.rubberBranchFilled);

		setDynamicBranch(ModContent.rubberBranch);
		ModContent.rubberBranchEmpty.setFamily(this);
		ModContent.rubberBranchFilled.setFamily(this);
		
		ModContent.rubberLeavesProperties.setTree(this);
		
		addConnectableVanillaLeaves((state) -> state.getBlock() == proxyIC2.IC2GetTreeBlocks(IC2Proxy.TreeBlock.LEAVES));
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(proxyIC2.IC2GetTreeBlocks(IC2Proxy.TreeBlock.LOG)), 1, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesRubberIC(this));
	}

	@Override
	public List<Block> getRegisterableBlocks(List<Block> blockList) {
		return super.getRegisterableBlocks(blockList);
	}

	@Override
	public int getRootColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
		return 0x93702b;
	}

	@Override
	public BlockBranch createBranch() {
		return ModContent.rubberBranch;
	}

}
