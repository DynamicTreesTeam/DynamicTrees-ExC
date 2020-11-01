package maxhyper.dynamictreestheaether.trees;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLog;
import com.gildedgames.the_aether.blocks.util.EnumLogType;
import com.gildedgames.the_aether.items.ItemsAether;
import com.gildedgames.the_aether.items.tools.ItemAetherTool;
import com.gildedgames.the_aether.items.tools.ItemGravititeTool;
import com.gildedgames.the_aether.items.tools.ItemValkyrieTool;
import com.gildedgames.the_aether.items.tools.ItemZaniteTool;
import com.gildedgames.the_aether.items.util.EnumAetherToolType;
import maxhyper.dynamictreestheaether.ModContent;
import maxhyper.dynamictreestheaether.blocks.BlockBranchGoldenOak;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ALTreeGoldenOak extends TreeFamily {

	public static Block leavesBlock = BlocksAether.aether_leaves;
	public static Block logBlock = BlocksAether.aether_log;
	public static Block saplingBlock = BlocksAether.golden_oak_sapling;

	public class SpeciesGoldenOak extends Species {

		SpeciesGoldenOak(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.goldenOakLeavesProperties);

			setBasicGrowingParameters(0.6f, 20f, upProbability + 2, lowestBranchHeight + 5, growthRate /2);

			envFactor(Type.COLD, 0.8f);
			envFactor(Type.HOT, 0.7f);

			generateSeed();

			setupStandardSeedDropping();
			clearAcceptableSoils();
			addAcceptableSoils(ModContent.AETHERLIKE);
		}

		@Override
		public boolean useDefaultWailaBody() {
			return false;
		}
	}

	public ALTreeGoldenOak() {
		super(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "goldenoak"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockAetherLog.wood_type, EnumLogType.Oak), new ItemStack(logBlock, 1, 1));

		ModContent.goldenOakLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
//	@Override
//	public ItemStack getPrimitiveLogItemStack(int qty) {
//		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, 1);
//		stack.setCount(MathHelper.clamp(qty, 0, 64));
//		return stack;
//	}
//
	@Override
	public ItemStack getStick(int qty) {
		return new ItemStack(ItemsAether.skyroot_stick, qty);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesGoldenOak(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

	@Override
	public BlockBranch createBranch() {
		return new BlockBranchGoldenOak("goldenOakBranch");
	}
}
