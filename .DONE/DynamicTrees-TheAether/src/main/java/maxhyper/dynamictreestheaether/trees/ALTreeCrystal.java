package maxhyper.dynamictreestheaether.trees;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLog;
import com.gildedgames.the_aether.blocks.util.EnumLogType;
import com.gildedgames.the_aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.DynamicTreesTheAether;
import maxhyper.dynamictreestheaether.ModConfigs;
import maxhyper.dynamictreestheaether.ModContent;
import maxhyper.dynamictreestheaether.genfeatures.FeatureGenRandomLeaves;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ALTreeCrystal extends TreeFamily {

	public static Block leavesBlock = BlocksAether.crystal_leaves;
	public static Block logBlock = BlocksAether.aether_log;

	public class SpeciesCrystal extends Species {

		SpeciesCrystal(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.crystalLeavesProperties);

			setBasicGrowingParameters(0.2f, 8.0f, 3, 3, 0.6f);
			setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.CONIFER));

			addValidLeavesBlocks(ModContent.crystalFruitLeavesProperties);

			envFactor(Type.COLD, 1.4f);
			envFactor(Type.HOT, 1.2f);

			addDropCreator(new DropCreatorSeed() {
				@Override public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
					float rarity = getHarvestRarity();
					rarity *= (fortune + 1) / 128f; //Extra rare so players are incentivized to get fruits from growing instead of chopping
					rarity *= Math.min(species.seasonalSeedDropFactor(world, leafPos) + 0.15f, 1.0);
					if(rarity > random.nextFloat()) dropList.add(getFruit()); //1 in 128 chance to drop a fruit on destruction..
					return dropList;
				}

				private ItemStack getFruit (){
					return new ItemStack(ItemsAether.white_apple);
				}

				@Override public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
					int chance = 40;
					//Hokey fortune stuff here to match Vanilla logic.
					if (fortune > 0) {
						chance -= 2 << fortune;
						if (chance < 10) chance = 10;
					}
					float seasonFactor = 1.0f;
					if(access instanceof World) {
						World world = (World) access;
						if(!world.isRemote) seasonFactor = species.seasonalSeedDropFactor(world, breakPos);
					}
					if(random.nextInt((int) (chance / getLeavesRarity())) == 0)
						if(seasonFactor > random.nextFloat())
							dropList.add(getFruit());
					return dropList;
				}
			});

			if (ModConfigs.fruityLeaves)
				addGenFeature(new FeatureGenRandomLeaves(4, 12, ModContent.crystalLeaves, 0, 1, 0.3f, ModConfigs.crystalTreesGrow||ModContent.lostAetherLoaded).setFruitingRadius(2));

			ModContent.blockWhiteApple.setSpecies(this);
			addGenFeature(new FeatureGenFruit(ModContent.blockWhiteApple).setFruitingRadius((ModConfigs.crystalTreesGrow||ModContent.lostAetherLoaded)?2:9));
			generateSeed();
			clearAcceptableSoils();
			addAcceptableSoils(ModContent.AETHERLIKE);

			addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));
		}

		@Override
		public float seasonalFruitProductionFactor(World world, BlockPos pos) {
			return 1;
		}

	}

	public ALTreeCrystal() {
		super(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "crystal"));
		hasConiferVariants = true;
		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockAetherLog.wood_type, EnumLogType.Skyroot), new ItemStack(logBlock, 1, 0));

		ModContent.crystalLeavesProperties.setTree(this);
		ModContent.crystalFruitLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, 0);
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public ItemStack getStick(int qty) {
		return new ItemStack(ItemsAether.skyroot_stick, qty);
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesCrystal(this));
	}

	@Override
	public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
		super.registerSpecies(speciesRegistry);
	}

	@Override
	public List<Item> getRegisterableItems(List<Item> itemList) {
		return super.getRegisterableItems(itemList);
	}

}
