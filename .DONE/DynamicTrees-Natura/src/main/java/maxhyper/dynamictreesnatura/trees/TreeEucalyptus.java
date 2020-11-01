package maxhyper.dynamictreesnatura.trees;

import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.progwml6.natura.overworld.NaturaOverworld;
import com.progwml6.natura.overworld.block.logs.BlockOverworldLog2;
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

public class TreeEucalyptus extends TreeFamily {

	public static Block leavesBlock = NaturaOverworld.overworldLeaves2;
    public static Block logBlock = NaturaOverworld.overworldLog2;
    public static Block saplingBlock = NaturaOverworld.overworldSapling2;
	public static IBlockState leavesState = leavesBlock.getDefaultState().withProperty(BlockOverworldLog2.TYPE, BlockOverworldLog2.LogType.EUCALYPTUS);

	public class SpeciesEucalyptus extends Species {

		SpeciesEucalyptus(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.eucalyptusLeavesProperties);

			this.setBasicGrowingParameters(0.15F, 24.0F, 2, 3, 0.7F);

			generateSeed();
			setupStandardSeedDropping();
		}
		public Species generateSeed() {
			Seed seed = new Seed(getRegistryName().getResourcePath() + "seed"){
				@Override
				public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
					tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(LocUtils.translateRecursive("tile.natura.overworld_sapling2.eucalyptus.tooltip"))));
					super.addInformation(stack, worldIn, tooltip, flagIn);
				}
			};
			setSeedStack(new ItemStack(seed));
			return this;
		}
	}

	public TreeEucalyptus() {
		super(new ResourceLocation(DynamicTreesNatura.MODID, "eucalyptus"));

		setPrimitiveLog(logBlock.getDefaultState().withProperty(BlockOverworldLog2.TYPE, BlockOverworldLog2.LogType.EUCALYPTUS));

		ModContent.eucalyptusLeavesProperties.setTree(this);

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
		ItemStack stick = NaturaCommons.eucalyptus_stick;
		stick.setCount(qty);
		return stick;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesEucalyptus(this));
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
