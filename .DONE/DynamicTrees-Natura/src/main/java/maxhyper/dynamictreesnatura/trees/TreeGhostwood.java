package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.nether.NaturaNether;
import com.progwml6.natura.nether.block.leaves.BlockNetherLeaves;
import com.progwml6.natura.nether.block.logs.BlockNetherLog;
import com.progwml6.natura.shared.NaturaCommons;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.util.LocUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class TreeGhostwood extends TreeFamily {

	public static Block leavesBlock = NaturaNether.netherLeaves;
    public static Block logBlock = NaturaNether.netherLog;
    public static Block saplingBlock = NaturaNether.netherSapling;
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockNetherLeaves.TYPE, BlockNetherLeaves.LeavesType.GHOSTWOOD);

	public class SpeciesGhostwood extends Species {

		SpeciesGhostwood(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.ghostwoodLeavesProperties);

			setBasicGrowingParameters(0.3f, 10.0f, upProbability, lowestBranchHeight, 0.8f);

			envFactor(Type.COLD, 0.75f);

			generateSeed();
			setupStandardSeedDropping();
			addAcceptableSoils(DirtHelper.NETHERLIKE);
		}

		public Species generateSeed() {
			Seed seed = new Seed(getRegistryName().getResourcePath() + "seed"){
				@Override
				public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
					tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(LocUtils.translateRecursive("tile.natura.nether_sapling.ghostwood.tooltip"))));
					super.addInformation(stack, worldIn, tooltip, flagIn);
				}
			};
			setSeedStack(new ItemStack(seed));
			return this;
		}

		@Override
		public BlockRooty getRootyBlock(World world, BlockPos pos) {
			if (DirtHelper.isSoilAcceptable(world.getBlockState(pos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.NETHERLIKE))){
				return ModContent.rootyNetherDirt;
			} else {
				return ModBlocks.blockRootyDirt;
			}
		}
	}

	public TreeGhostwood() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "ghostwood"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockNetherLog.TYPE, BlockNetherLog.LogType.GHOSTWOOD));

		ModContent.ghostwoodLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}
	@Override
	public ItemStack getStick(int qty) {
		ItemStack stick = NaturaCommons.ghostwood_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesGhostwood(this));
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
