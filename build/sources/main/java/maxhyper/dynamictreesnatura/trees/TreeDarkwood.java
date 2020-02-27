package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import com.progwml6.natura.nether.NaturaNether;
import com.progwml6.natura.shared.NaturaCommons;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreeDarkwood extends TreeFamily {

	public static Block leavesBlock = NaturaNether.netherLeaves2;
    public static Block logBlock = NaturaNether.netherLog;
    public static Block saplingBlock = NaturaNether.netherSapling2;

	public class SpeciesDarkwood extends Species {

		SpeciesDarkwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.darkwoodLeavesProperties);

			setBasicGrowingParameters(0.3f, 10.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.COLD, 0.75f);
			envFactor(Type.HOT, 0.50f);
			envFactor(Type.DRY, 0.50f);
			envFactor(Type.FOREST, 1.05f);

			this.addGenFeature((new FeatureGenFruit(ModContent.blockPotashApple)).setRayDistance(4.0F));

			generateSeed();
			setupStandardSeedDropping();
			this.addAcceptableSoil(Blocks.NETHERRACK, Blocks.SOUL_SAND);
		}
		@Override
		public BlockRooty getRootyBlock() {
			return ModContent.rootyNetherDirt;
		}
	}

	public TreeDarkwood() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "darkwood"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 1));

		ModContent.darkwoodLeavesProperties.setTree(this);
		ModContent.darkwoodFloweringLeavesProperties.setTree(this);
		ModContent.darkwoodFruitLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 1);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}
	@Override
	public ItemStack getStick(int qty) {
		ItemStack stick = NaturaCommons.darkwood_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesDarkwood(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override public BlockBranch createBranch() {
		String branchName = getName() + "branch";
		return new BlockBranchBasic(branchName){
			@Override public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
				return 0;
			}
			@Override public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
				return 0;
			}
		};
	}

}
