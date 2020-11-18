package maxhyper.dynamictreestheaether.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.blocks.natural.BlockAetherGrass;
import com.gildedgames.the_aether.blocks.natural.BlockAetherLog;
import com.gildedgames.the_aether.blocks.util.EnumLogType;
import com.gildedgames.the_aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.DynamicTreesTheAether;
import maxhyper.dynamictreestheaether.ModConfigs;
import maxhyper.dynamictreestheaether.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ALTreeSkyroot extends TreeFamily {

	public static Block leavesBlock = BlocksAether.aether_leaves;
	public static Block logBlock = BlocksAether.aether_log;
	public static Block saplingBlock = BlocksAether.skyroot_sapling;

	public class SpeciesSkyroot extends Species {

		SpeciesSkyroot(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.skyrootLeavesProperties);

			setBasicGrowingParameters(tapering, signalEnergy / 2, upProbability, lowestBranchHeight, growthRate);

			envFactor(Type.COLD, 1.2f);
			envFactor(Type.HOT, 0.9f);

			generateSeed();
			setupStandardSeedDropping();
			clearAcceptableSoils();
			addAcceptableSoils(ModContent.AETHERLIKE);
		}

		@Override
		public boolean generate(World world, BlockPos rootPos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
			//Chance for a skyroot tree to be a christmas tree when the config is enabled
			if (AetherConfig.world_gen.christmas_time && world.rand.nextFloat() < ModConfigs.holidayTreeChance){
				Species holiday = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether.MODID,"holiday"));
				return holiday.generate(world, rootPos, biome, random, radius, safeBounds);
			}
			//When above 100, change trees to golden oak if the grass is dungeon, cancel otherwise.
			if (rootPos.getY() >= 100){
				if (world.getBlockState(rootPos).getBlock() == BlocksAether.aether_grass && world.getBlockState(rootPos).getValue(BlockAetherGrass.dungeon_block)){
					Species goldenOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether.MODID,"goldenoak"));
					return goldenOak.generate(world, rootPos, biome, random, radius, safeBounds);
				} else return false;
			}
			return super.generate(world, rootPos, biome, random, radius, safeBounds);
		}
	}

	public ALTreeSkyroot() {
		super(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "skyroot"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockAetherLog.wood_type, EnumLogType.Skyroot), new ItemStack(logBlock, 1, 0));

		ModContent.skyrootLeavesProperties.setTree(this);

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
		setCommonSpecies(new SpeciesSkyroot(this));
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
