package maxhyper.dynamictreestbl.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
	}
	
	@Override
	public void init() {
		super.init();
		registerColorHandlers();
	}
	
	@Override public void postInit() {
		super.postInit();
	}
	
	public void registerColorHandlers() {

		Block[] grassyBlocks = {ModContent.undergroundHearthgroveRoot, ModContent.undergroundHearthgroveRootSwamp};
		for (Block block: grassyBlocks) {
			ModelHelper.regColorHandler(block, new IBlockColor() {
				@Override public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
					return worldIn.getBiome(pos).getGrassColorAtPos(pos);
				}
			});
		}

		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesTBL.MODID).values()) {
			if (leaves != ModContent.hearthgroveLeavesProperties.getDynamicLeavesState().getBlock()){
				ModelHelper.regColorHandler(leaves, new IBlockColor() {
					@Override
					public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
						//boolean inWorld = worldIn != null && pos != null;

						Block block = state.getBlock();

						if (TreeHelper.isLeaves(block)) {
							return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
						}
						return 0x00FF00FF; //Magenta
					}
				});
			}
		}
	}
}
