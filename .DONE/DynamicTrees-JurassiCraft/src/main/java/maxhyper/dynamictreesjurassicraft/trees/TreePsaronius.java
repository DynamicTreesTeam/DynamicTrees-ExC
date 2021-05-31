package maxhyper.dynamictreesjurassicraft.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesjurassicraft.DynamicTreesJurassiCraft;
import maxhyper.dynamictreesjurassicraft.ModContent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;

public class TreePsaronius extends Palm {

	public static Block leavesBlock = Block.getBlockFromName("jurassicraft:psaronius_leaves");
	public static Block logBlock = Block.getBlockFromName("jurassicraft:psaronius_log");
	public static Block saplingBlock = Block.getBlockFromName("jurassicraft:psaronius_sapling");

	public class SpeciesCalamites extends Palm.SpeciesPalm {

		SpeciesCalamites(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.psaroniusLeavesProperties);

			setBasicGrowingParameters(0.5f, 6.0f, upProbability, lowestBranchHeight, 0.8f);

			addGenFeature(new FeatureGenFlareBottom(){
				public void flareBottom(World world, BlockPos rootPos, Species species) {
					TreeFamily family = species.getFamily();
					int radius3 = TreeHelper.getRadius(world, rootPos.up(3));
					if (radius3 > 2) {
						family.getDynamicBranch().setRadius(world, rootPos.up(2), radius3 + 1, EnumFacing.UP);
						family.getDynamicBranch().setRadius(world, rootPos.up(1), radius3 + 2, EnumFacing.UP);
					}

				}
			});
		}
	}

	public TreePsaronius() {
		super(new ResourceLocation(DynamicTreesJurassiCraft.MODID, "psaronius"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.psaroniusLeavesProperties.setTree(this);

		addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
	}
	@Override
	public ItemStack getPrimitiveLogItemStack(int qty) {
		ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock));
		stack.setCount(MathHelper.clamp(qty, 0, 64));
		return stack;
	}

	@Override
	public void createSpecies() {
		setCommonSpecies(new SpeciesCalamites(this));
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
