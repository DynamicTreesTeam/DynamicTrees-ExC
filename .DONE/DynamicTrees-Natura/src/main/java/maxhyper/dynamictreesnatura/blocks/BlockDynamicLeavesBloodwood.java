package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.api.treedata.ITreePart;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;

import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class BlockDynamicLeavesBloodwood extends BlockDynamicLeaves {

	public BlockDynamicLeavesBloodwood() {
		super();
		setRegistryName(DynamicTreesNatura.MODID, "leaves_bloodwood");
		setUnlocalizedName("leaves_bloodwood");
	}
	@Override public boolean hasAdequateLight(IBlockState blockState, World world, ILeavesProperties leavesProperties, BlockPos pos) {

		//If clear sky is above the block then we needn't go any further
		if(world.canBlockSeeSky(pos)) {
			return true;
		}

		int smother = leavesProperties.getSmotherLeavesMax();

		//Check to make sure there isn't too many leaves above this block.  Encourages forest canopy development.
		if(smother != 0){
			if(isBottom(world, pos)) {//Only act on the bottom block of the Growable stack
				//Prevent leaves from growing where they would be "smothered" from too much above foliage
				int smotherLeaves = 0;
				for(int i = 0; i < smother; i++) {
					smotherLeaves += TreeHelper.isTreePart(world, pos.down(i + 1)) ? 1 : 0;
				}
				if(smotherLeaves >= smother) {
					return false;
				}
			}
		}

		//Ensure the leaves don't grow in dark locations..  This creates a realistic canopy effect in forests and other nice stuff.
		//If there's already leaves here then don't kill them if it's a little dark
		//If it's empty space then don't create leaves unless it's sufficiently bright
		//The range allows for adaptation to the hysteretic effect that could cause blocks to rapidly appear and disappear
		if(world.getLightFor(EnumSkyBlock.SKY, pos) >= (TreeHelper.isLeaves(blockState) ? leavesProperties.getLightRequirement() - 2 : leavesProperties.getLightRequirement())) {
			return true;
		}

		return false;
	}
	@Override public boolean isLocationSuitableForNewLeaves(World world, ILeavesProperties leavesProperties, BlockPos pos) {
		IBlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();

		if(block instanceof BlockDynamicLeaves) {
			return false;
		}

		IBlockState belowBlockState = world.getBlockState(pos.up());

		//Prevent leaves from growing on the ground or above liquids
		if((belowBlockState.isFullCube() && (!(belowBlockState.getBlock() instanceof BlockLeaves)) ) || belowBlockState.getBlock() instanceof BlockLiquid) {
			return false;
		}

		//Help to grow into double tall grass and ferns in a more natural way
		if(block == Blocks.DOUBLE_PLANT){
			IBlockState bs = world.getBlockState(pos);
			EnumBlockHalf half = bs.getValue(BlockDoublePlant.HALF);
			if(half == EnumBlockHalf.UPPER) {//Top block of double plant
				if(belowBlockState.getBlock() == Blocks.DOUBLE_PLANT) {
					EnumPlantType type = belowBlockState.getValue(BlockDoublePlant.VARIANT);
					if(type == EnumPlantType.GRASS || type == EnumPlantType.FERN) {//tall grass or fern
						world.setBlockToAir(pos);
						world.setBlockState(pos.up(), Blocks.TALLGRASS.getDefaultState()
								.withProperty(BlockTallGrass.TYPE, type == EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN), 3);
					}
				}
			}
		}

		return world.isAirBlock(pos) && hasAdequateLight(blockState, world, leavesProperties, pos);
	}
	public static boolean isBottom(World world, BlockPos pos) {
		IBlockState belowBlockState = world.getBlockState(pos.up());
		ITreePart belowTreepart = TreeHelper.getTreePart(belowBlockState);
		if(belowTreepart != TreeHelper.nullTreePart) {
			return belowTreepart.getRadius(belowBlockState) > 1;//False for leaves, twigs, and dirt.  True for stocky branches
		}
		return true;//Non-Tree parts below indicate the bottom of stack
	}
}
