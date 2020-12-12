package maxhyper.dynamictreesdefiledlands.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import maxhyper.dynamictreesdefiledlands.DynamicTreesDefiledLands;
import maxhyper.dynamictreesdefiledlands.ModContent;
import maxhyper.dynamictreesdefiledlands.event.EventListenerDefiledLands;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new EventListenerDefiledLands());
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
		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesDefiledLands.MODID).values()) {
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
		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> { // Rooty Dirt
					switch(tintIndex) {
						case 0: return blockColors.colorMultiplier(ModContent.rootyDefiledDirt.getMimic(world, pos), world, pos, tintIndex);
						case 1: return state.getBlock() instanceof BlockRooty ? ((BlockRooty) state.getBlock()).rootColor(state, world, pos) : 0xFFFFFFFF;
						default: return 0xFFFFFFFF;
					}
				},
				ModContent.rootyDefiledDirt);
	}
}
