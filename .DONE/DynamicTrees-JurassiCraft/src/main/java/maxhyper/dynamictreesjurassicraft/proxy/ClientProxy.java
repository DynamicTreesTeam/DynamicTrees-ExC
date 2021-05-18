package maxhyper.dynamictreesjurassicraft.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import maxhyper.dynamictreesjurassicraft.DynamicTreesJurassiCraft;
import maxhyper.dynamictreesjurassicraft.ModContent;
import maxhyper.dynamictreesjurassicraft.models.ModelLoaderBlockPalmFronds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFronds("dynamicpalmfrondsjcpsaronius",0));
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFronds("dynamicpalmfrondsjcphoenix",1));
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
		List<BlockDynamicLeaves> leavesList = new LinkedList<>(LeavesPaging.getLeavesMapForModId(DynamicTreesJurassiCraft.MODID).values());
		leavesList.add(ModContent.phoenixFrondLeaves);
		leavesList.add(ModContent.psaroniusFrondLeaves);
		for (BlockDynamicLeaves leaves: leavesList) {
			ModelHelper.regColorHandler(leaves, (state, worldIn, pos, tintIndex) -> {
				//boolean inWorld = worldIn != null && pos != null;

				Block block = state.getBlock();

				if (TreeHelper.isLeaves(block)) {
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
				}
				return 0x00FF00FF; //Magenta
			});
		}
	}
}
