package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.overworld.NaturaOverworld;
import com.progwml6.natura.overworld.block.logs.BlockOverworldLog;
import com.progwml6.natura.shared.NaturaCommons;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.util.LocUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class TreeSilverbell extends TreeFamily {

	public static Block leavesBlock = NaturaOverworld.overworldLeaves;
    public static Block logBlock = NaturaOverworld.overworldLog;
    public static Block saplingBlock = NaturaOverworld.overworldSapling;
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockOverworldLog.TYPE, BlockOverworldLog.LogType.SILVERBELL);


	public class SpeciesSilverbell extends Species {

		SpeciesSilverbell(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.silverbellLeavesProperties);

			setBasicGrowingParameters(0.3f, 8.0f, upProbability, lowestBranchHeight, 0.8f);

			setSoilLongevity(2);

			generateSeed();
			setupStandardSeedDropping();
		}

		public Species generateSeed() {
			Seed seed = new Seed(getRegistryName().getResourcePath() + "seed"){
				@Override
				public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
					tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(LocUtils.translateRecursive("tile.natura.overworld_sapling.silverbell.tooltip"))));
					super.addInformation(stack, worldIn, tooltip, flagIn);
				}
			};
			setSeedStack(new ItemStack(seed));
			return this;
		}
	}

	public TreeSilverbell() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "silverbell"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockOverworldLog.TYPE, BlockOverworldLog.LogType.SILVERBELL));

		ModContent.silverbellLeavesProperties.setTree(this);

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
		ItemStack stick = NaturaCommons.silverbell_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesSilverbell(this));
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
		String branchName = getName() + "branch";
		return new BlockBranchBasic(branchName){
			@Override
			public int getMaxRadius() {
				return 5;
			}
		};
	}

}
