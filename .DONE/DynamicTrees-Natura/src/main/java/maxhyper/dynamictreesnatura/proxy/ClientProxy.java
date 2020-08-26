package maxhyper.dynamictreesnatura.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.event.EventListenerNatura;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedBloodwood;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedMaple;
import maxhyper.dynamictreesnatura.model.ModelLoaderBlockBranchBloodwood;
import maxhyper.dynamictreesnatura.model.ModelLoaderBlockBranchSaguaro;
import maxhyper.dynamictreesnatura.renderer.RenderBloodwoodSeed;
import maxhyper.dynamictreesnatura.renderer.RenderMapleSeed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		registerEntityRenderers();

		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockBranchBloodwood());
		//ModelLoaderRegistry.registerLoader(new ModelLoaderBlockBranchSaguaro());

		MinecraftForge.EVENT_BUS.register(new EventListenerNatura());
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

		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesNatura.MODID).values()) {
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
		} // All leaves

		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> { // Rooty Dirt
					switch(tintIndex) {
						case 0: return blockColors.colorMultiplier(Blocks.GRASS.getDefaultState(), world, pos, tintIndex);
						case 1: return state.getBlock() instanceof BlockRooty ? ((BlockRooty) state.getBlock()).rootColor(state, world, pos) : 0xFFFFFFFF;
						default: return 0xFFFFFFFF;
					}
				},
				ModContent.rootyNetherDirt, ModContent.rootyUpsidedownDirt);
	}

	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(ItemDynamicSeedBloodwood.EntityItemBloodwoodSeed.class, new RenderBloodwoodSeed.Factory());
		RenderingRegistry.registerEntityRenderingHandler(ItemDynamicSeedMaple.EntityItemMapleSeed.class, new RenderMapleSeed.Factory());
	}
}
