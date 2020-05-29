package maxhyper.dynamictreestconstruct.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import maxhyper.dynamictreestconstruct.ModContent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Objects;

public class TCTreeSlimeMagma extends TCTreeSlimeBlue {

	public class SpeciesMagmaSlime extends Species {
		SpeciesMagmaSlime(TreeFamily treeFamily) {
			super(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeMagma"), treeFamily, ModContent.magmaSlimeLeavesProperties);
			this.setBasicGrowingParameters(taperingDefaultSlime, energyDefaultSlime, upProbabilityDefaultSlime, lowestBranchHeightDefaultSlime, growthRateDefaultSlime);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("slime"));

			this.envFactor(Type.HOT, 0.5F);
			this.envFactor(Type.DRY, 0.10F);
			this.envFactor(Type.WET, 1.75F);

			generateSeed();
			this.setupStandardSeedDropping();

			setStick(ItemStack.EMPTY);

			//this.addGenFeature((new FeatureGenFruit(ModContent.blockMagmaSlime)).setRayDistance(4.0F));
			this.clearAcceptableSoils();
			this.addAcceptableSoil(TinkerWorld.slimeGrass, TinkerWorld.slimeDirt);
		}
		@Override
		public BlockRooty getRootyBlock() {
			return ModContent.rootySlimyDirt;
		}

		@Override
		public boolean isThick() {
			return false;
		}
	}

	public TCTreeSlimeMagma() {
		super(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeMagma"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 4));
		setDynamicBranch(ModContent.slimeMagmaBranch);

		ModContent.magmaSlimeLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 4);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override public void createSpecies() {
			setCommonSpecies(new SpeciesMagmaSlime(this));
	}
}
