package maxhyper.dynamictreesttf.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.substances.ISubstanceEffect;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.substances.SubstanceTransform;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilight;
import maxhyper.dynamictreesttf.blocks.BlockBranchTwilightThick;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.block.BlockTFLeaves;
import twilightforest.enums.LeavesVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Deprecated
public class TreeSicklyTwilightOak extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("twilightforest:twilight_leaves");
	public static Block logBlock = Block.getBlockFromName("twilightforest:twilight_log");
	public static Block saplingBlock = Block.getBlockFromName("twilightforest:twilight_sapling");
	public static IBlockState leavesState = leavesBlock.getStateFromMeta(0);
	public static int logsMeta = 0;
	public static int saplingMeta = 0;

	public class SpeciesSicklyTwilightOak extends Species {

		SpeciesSicklyTwilightOak(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.sicklyTwilightOakLeavesProperties);

			setBasicGrowingParameters(tapering, 9, upProbability, lowestBranchHeight, growthRate);
		}

		@Override
		public ResourceLocation getSaplingName() {
			return new ResourceLocation(DynamicTreesTTF.MODID, "twilightoaksickly");
		}

		@Override
		public boolean onTreeActivated(World world, BlockPos rootPos, BlockPos hitPos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
			Species realSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakSickly"));
			ISubstanceEffect effect = new SubstanceTransform(realSpecies);
			effect.apply(world, rootPos);
			return super.onTreeActivated(world, rootPos, hitPos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
		}
	}

	public TreeSicklyTwilightOak() {
		super(new ResourceLocation(DynamicTreesTTF.MODID, "sicklyTwilightOak"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, logsMeta));

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}

	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, logsMeta);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSicklyTwilightOak(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return new ArrayList<>();
	}

	@Override
	public BlockBranch createBranch() {
		String branchName = "sicklyTwilightOakbranch";
		BlockBranchTwilight branch = new BlockBranchTwilight(branchName){
			@Override
			public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
				BlockPos rootPos = TreeHelper.findRootNode(state, world, pos);
				Species realSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTTF.MODID, "twilightOakSickly"));
				ISubstanceEffect effect = new SubstanceTransform(realSpecies);
				effect.apply(world, rootPos);
			}
		};
		branch.setTickRandomly(true);
		return branch;
	}

}
