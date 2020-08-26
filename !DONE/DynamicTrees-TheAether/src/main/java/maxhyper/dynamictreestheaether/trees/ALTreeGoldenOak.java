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
import com.gildedgames.the_aether.items.util.EnumAetherToolType;
import maxhyper.dynamictreestheaether.ModContent;
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
			addAcceptableSoil(BlocksAether.aether_grass, BlocksAether.enchanted_aether_grass, BlocksAether.aether_dirt);
		}

		@Override
		public BlockRooty getRootyBlock() {
			return ModContent.rootyDirtAether;
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
		return new BlockBranchBasic("goldenOakBranch"){
			@Override public void futureBreak(IBlockState state, World world, BlockPos cutPos, EntityLivingBase entity) {

				//Try to get the face being pounded on
				final double reachDistance = entity instanceof EntityPlayerMP ? entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() : 5.0D;
				RayTraceResult rtResult = playerRayTrace(entity, reachDistance, 1.0F);
				EnumFacing toolDir = rtResult != null ? (entity.isSneaking() ? rtResult.sideHit.getOpposite() : rtResult.sideHit) : EnumFacing.DOWN;

				//Do the actual destruction
				BranchDestructionData destroyData = destroyBranchFromNode(world, cutPos, toolDir, false);

				//Get all of the wood drops
				ItemStack heldItem = entity.getHeldItemMainhand();
				int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
				boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) != 0;
				float fortuneFactor = 1.0f + 0.25f * fortune;
				float woodVolume = destroyData.woodVolume;// The amount of wood calculated from the body of the tree network
				List<ItemStack> woodItems;
				if (silkTouch){
					woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor, true, heldItem);
				} else {
					woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor, false, heldItem);
				}

				if(entity.getActiveHand() == null) {//What the hell man? I trusted you!
					entity.setActiveHand(EnumHand.MAIN_HAND);//Players do things with hands.
				}

				float chance = 1.0f;
				//Fire the block harvesting event.  For An-Sar's PrimalCore mod :)
				if (entity instanceof EntityPlayer)
				{
					chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(woodItems, world, cutPos, state, fortune, chance, false, (EntityPlayer) entity);
				}
				final float finalChance = chance;

				//Build the final wood drop list taking chance into consideration
				List<ItemStack> woodDropList = woodItems.stream().filter(i -> world.rand.nextFloat() <= finalChance).collect(Collectors.toList());

				//This will drop the EntityFallingTree into the world
				EntityFallingTree.dropTree(world, destroyData, woodDropList, EntityFallingTree.DestroyType.HARVEST);

				//Damage the axe by a prescribed amount
				damageAxe(entity, heldItem, getRadius(state), woodVolume);
			}
			@Override protected void sloppyBreak(World world, BlockPos cutPos, EntityFallingTree.DestroyType destroyType) {
				//Do the actual destruction
				BranchDestructionData destroyData = destroyBranchFromNode(world, cutPos, EnumFacing.DOWN, false);
				float burntWoodVolume = 0;

				//Get all of the wood drops
				List<ItemStack> woodDropList = getLogDrops(world, cutPos, destroyData.species, destroyData.woodVolume, false, ItemStack.EMPTY);

				//This will drop the EntityFallingTree into the world
				EntityFallingTree.dropTree(world, destroyData, woodDropList, destroyType);
			}
			public List<ItemStack> getLogDrops(World world, BlockPos pos, Species species, float volume, boolean silkTouch, ItemStack heldItem) {
				List<ItemStack> ret = new java.util.ArrayList<ItemStack>();//A list for storing all the dead tree guts
				if (heldItem != null && heldItem.getItem() instanceof ItemAetherTool && ((ItemAetherTool) heldItem.getItem()).toolType == EnumAetherToolType.AXE) {
					if (heldItem.getItem() instanceof ItemGravititeTool || heldItem.getItem() instanceof ItemValkyrieTool) {
						ret.add(new ItemStack(ItemsAether.golden_amber, (int) (volume * (1 + world.rand.nextFloat())), 0));
					}
				}
				ret.add(new ItemStack(logBlock, (int) volume, 0));

				//ret.add(new ItemStack(ItemsAether.skyroot_stick, (int) ((volume - ((int)volume)) * 8))); //drops logs with silktouch
				volume *= ModConfigs.treeHarvestMultiplier;// For cheaters.. you know who you are.
				return species.getLogsDrops(world, pos, ret, volume);
			}
		};
	}
}
