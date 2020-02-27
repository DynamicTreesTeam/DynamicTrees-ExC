package maxhyper.dynamictreesexc.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.List;
import java.util.Objects;

public class TreeSlimeBlue extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("tconstruct:slime_leaves");
	public static Block logBlock = Block.getBlockFromName("tconstruct:slime_congealed");
	public static Block saplingBlock = Block.getBlockFromName("tconstruct:slime_sapling");

	public static float taperingDefaultSlime = 0.25f;
	public static float energyDefaultSlime = 12.0f;
	public static int upProbabilityDefaultSlime = 1;
	public static int lowestBranchHeightDefaultSlime = 5;
	public static float growthRateDefaultSlime = 0.9f;

	public class SpeciesBlueSlime extends Species {
		SpeciesBlueSlime(TreeFamily treeFamily) {
			super(new ResourceLocation(DynamicTreesExC.MODID, "slimeBlue"), treeFamily, ModContent.blueSlimeLeavesProperties);
			this.setBasicGrowingParameters(taperingDefaultSlime, energyDefaultSlime, upProbabilityDefaultSlime, lowestBranchHeightDefaultSlime, growthRateDefaultSlime);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("slime"));

			this.envFactor(Type.HOT, 0.5F);
			this.envFactor(Type.DRY, 0.10F);
			this.envFactor(Type.WET, 1.75F);

			generateSeed();
			this.setupStandardSeedDropping();

			setStick(ItemStack.EMPTY);

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

	public TreeSlimeBlue() {
		super(new ResourceLocation(DynamicTreesExC.MODID, "slimeBlue"));

		setPrimitiveLog(logBlock.getStateFromMeta(0), new ItemStack(logBlock, 1, 0));
		setDynamicBranch(ModContent.slimeBlueBranch);

		ModContent.blueSlimeLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);

	}
	public TreeSlimeBlue(ResourceLocation resourceLocation) {
		super(resourceLocation);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override public void createSpecies() {
			setCommonSpecies(new SpeciesBlueSlime(this));
	}
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}


	@Override
	public boolean isThick() {
		return false;
	}

	@Override
	public boolean autoCreateBranch() {
		return true;
	}

}
