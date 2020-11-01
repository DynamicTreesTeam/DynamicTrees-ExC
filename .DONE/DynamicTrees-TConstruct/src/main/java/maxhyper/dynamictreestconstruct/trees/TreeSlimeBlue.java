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
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.tconstruct.shared.block.BlockSlime;
import slimeknights.tconstruct.shared.block.BlockSlimeCongealed;
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
			super(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeBlue"), treeFamily, ModContent.blueSlimeLeavesProperties);
			this.setBasicGrowingParameters(taperingDefaultSlime, energyDefaultSlime, upProbabilityDefaultSlime, lowestBranchHeightDefaultSlime, growthRateDefaultSlime);
			this.setGrowthLogicKit(TreeRegistry.findGrowthLogicKit("slime"));

			this.envFactor(Type.HOT, 0.5F);
			this.envFactor(Type.DRY, 0.10F);
			this.envFactor(Type.WET, 1.75F);

			generateSeed();
			this.setupStandardSeedDropping();
			setStick(ItemStack.EMPTY);

			this.addGenFeature(new FeatureGenSlimeVines(TinkerWorld.slimeVineBlue3));
			if (ModConfigs.greenSlimeBallsInBlueTrees) {
				ModContent.blockGreenSlime.setSpecies(this);
				this.addGenFeature((new FeatureGenFruit(ModContent.blockGreenSlime)).setRayDistance(4.0F));
			}
			if (ModConfigs.blueSlimeBallsInBlueTrees) {
				ModContent.blockBlueSlime.setSpecies(this);
				this.addGenFeature((new FeatureGenFruit(ModContent.blockBlueSlime)).setRayDistance(4.0F));
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

	public TreeSlimeBlue() {
		this(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeBlue"));

		setDynamicBranch(ModContent.slimeBlueBranch);
		ModContent.blueSlimeLeavesProperties.setTree(this);
	}
	public TreeSlimeBlue(ResourceLocation resourceLocation) {
		super(resourceLocation);

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockSlime.TYPE, BlockSlime.SlimeType.GREEN));
		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
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
}
