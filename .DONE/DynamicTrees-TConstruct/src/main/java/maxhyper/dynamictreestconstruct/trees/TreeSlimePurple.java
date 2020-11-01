package maxhyper.dynamictreestconstruct.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import maxhyper.dynamictreestconstruct.ModConfigs;
import maxhyper.dynamictreestconstruct.ModContent;
import maxhyper.dynamictreestconstruct.genfeatures.FeatureGenSlimeVines;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Objects;

public class TreeSlimePurple extends TreeSlimeBlue {

	public class SpeciesPurpleSlime extends Species {
		SpeciesPurpleSlime(TreeFamily treeFamily) {
			super(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimePurple"), treeFamily, ModContent.purpleSlimeLeavesProperties);
			this.setBasicGrowingParameters(taperingDefaultSlime, energyDefaultSlime, upProbabilityDefaultSlime, lowestBranchHeightDefaultSlime, growthRateDefaultSlime);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("slime"));

			this.envFactor(Type.HOT, 0.5F);
			this.envFactor(Type.DRY, 0.10F);
			this.envFactor(Type.WET, 1.75F);

			generateSeed();
			this.setupStandardSeedDropping();
			setStick(ItemStack.EMPTY);


			this.addGenFeature(new FeatureGenSlimeVines(TinkerWorld.slimeVinePurple3));
			if (ModConfigs.purpleSlimeBallsInPurpleTrees) {
				ModContent.blockPurpleSlime.setSpecies(this);
				this.addGenFeature((new FeatureGenFruit(ModContent.blockPurpleSlime)).setRayDistance(4.0F));
			}
			this.clearAcceptableSoils();
			this.addAcceptableSoils(DirtHelper.SLIMELIKE);
		}

		@Override
		public float seasonalFruitProductionFactor(World world, BlockPos pos) {
			return 1;
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos pos) {
			return ModContent.rootySlimyDirt;
		}
	}

	public TreeSlimePurple() {
		super(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimePurple"));

		setDynamicBranch(ModContent.slimePurpleBranch);
		ModContent.purpleSlimeLeavesProperties.setTree(this);
	}

	@Override public void createSpecies() {
			setCommonSpecies(new SpeciesPurpleSlime(this));
	}
}
