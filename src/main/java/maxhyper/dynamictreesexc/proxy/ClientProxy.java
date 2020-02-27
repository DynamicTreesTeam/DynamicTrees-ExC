package maxhyper.dynamictreesexc.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.ModContent;
import maxhyper.dynamictreesexc.compat.CompatEvents;
import maxhyper.dynamictreesexc.event.EventListenerExC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new EventListenerExC());
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
		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesExC.MODID).values()) {
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
		if (Loader.isModLoaded("extrautils2")) {
			CompatEvents.ColorHandlersExtraUtils2();
		}
		if (Loader.isModLoaded("tconstruct")) {
			CompatEvents.ColorHandlersTinkersConstructCompat();
		}
	}
}
