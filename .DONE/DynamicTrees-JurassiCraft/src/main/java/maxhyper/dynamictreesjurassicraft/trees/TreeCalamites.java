package maxhyper.dynamictreesjurassicraft.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
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

public class TreeCalamites extends TreeFamily {

	public static Block leavesBlock = Block.getBlockFromName("jurassicraft:calamites_leaves");
	public static Block logBlock = Block.getBlockFromName("jurassicraft:calamites_log");
	public static Block saplingBlock = Block.getBlockFromName("jurassicraft:calamites_sapling");

	public class SpeciesCalamites extends Species {

		SpeciesCalamites(TreeFamily treeFamily) {
			super(treeFamily.getName(), treeFamily, ModContent.calamitesLeavesProperties);

			setBasicGrowingParameters(0.1f, 18, 6, 3, 1.2f);

			setSoilLongevity(12);

			generateSeed();

			setupStandardSeedDropping();
		}

		private boolean isNextToTrunk (BlockPos signalPos, GrowSignal signal){
			if (signal.numTurns != 1) return false;
			BlockPos rootPos = signal.rootPos;
			if (signalPos.getZ() < rootPos.getZ()){
				return  rootPos.getZ() - signalPos.getZ() == 1;
			} else if (signalPos.getZ() > rootPos.getZ()){
				return signalPos.getZ() - rootPos.getZ() == 1;
			}else if (signalPos.getX() > rootPos.getX()){
				return signalPos.getX() - rootPos.getX() == 1;
			}else if (signalPos.getX() < rootPos.getX()){
				return rootPos.getX() - signalPos.getX() == 1;
			}else {
				return false;
			}
		}
		private EnumFacing getRelativeFace (BlockPos signalPos, BlockPos rootPos){
			if (signalPos.getZ() < rootPos.getZ()){
				return EnumFacing.NORTH;
			} else if (signalPos.getZ() > rootPos.getZ()){
				return EnumFacing.SOUTH;
			}else if (signalPos.getX() > rootPos.getX()){
				return EnumFacing.EAST;
			}else if (signalPos.getX() < rootPos.getX()){
				return EnumFacing.WEST;
			}else {
				return EnumFacing.UP;
			}
		}

		@Override
		protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {

			if (signal.energy > 1){
				if (!signal.isInTrunk()){
					EnumFacing relativePosToRoot = getRelativeFace(pos, signal.rootPos);

					for (EnumFacing dir: EnumFacing.values()){
						if (dir != EnumFacing.DOWN){
							probMap[dir.getIndex()] = TreeHelper.isBranch(world.getBlockState(pos.offset(dir)))?2:0;
						}
					}

					boolean isBranchSide = TreeHelper.isBranch(world.getBlockState(pos.offset(relativePosToRoot)));
					boolean isBranchUp = TreeHelper.isBranch(world.getBlockState(pos.up()));
					probMap[EnumFacing.UP.getIndex()] = isBranchSide && !isBranchUp? 0:2;
					probMap[relativePosToRoot.getIndex()] = isBranchUp && !isBranchSide? 0:2;

					if (isNextToTrunk(pos, signal)){
						signal.energy /= 3;
						probMap[EnumFacing.UP.getIndex()] = 0;
					}

					probMap[EnumFacing.DOWN.getIndex()] = 0;

				} else {

					for (EnumFacing dir: EnumFacing.HORIZONTALS){
						if ( TreeHelper.isBranch(world.getBlockState(pos.offset(dir).up(2))) || TreeHelper.isBranch(world.getBlockState(pos.offset(dir).down(2))) ){
							probMap[dir.getIndex()] = 0;
						}
					}
					probMap[EnumFacing.UP.getIndex()] += 5;
				}
			}
			probMap[signal.dir.getOpposite().ordinal()] = 0;

			return probMap;
		}
	}

	public TreeCalamites() {
		super(new ResourceLocation(DynamicTreesJurassiCraft.MODID, "calamites"));

		setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

		ModContent.calamitesLeavesProperties.setTree(this);

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
