package maxhyper.dynamictreesforestry.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import forestry.core.models.BlockModelEntry;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesFruit;
import maxhyper.dynamictreesforestry.event.EventListenerForestry;
import maxhyper.dynamictreesforestry.items.ItemDynamicSeedMaple;
import maxhyper.dynamictreesforestry.models.ModelLoaderBlockPalmFronds;
import maxhyper.dynamictreesforestry.models.ModelLoaderBlockPalmFrondsBig;
import maxhyper.dynamictreesforestry.renderer.RenderMapleSeed;
import maxhyper.dynamictreesforestry.trees.TreeWillow;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
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

		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFronds());
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockPalmFrondsBig());
		MinecraftForge.EVENT_BUS.register(new EventListenerForestry());
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

		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> { // Rooty Dirt
					switch(tintIndex) {
						case 0: return TreeHelper.isLeaves(state.getBlock())?((BlockDynamicLeaves) state.getBlock()).getProperties(state).foliageColorMultiplier(state, world, pos) : 0xFFFFFFFF;
						case 1: return state.getBlock() instanceof BlockDynamicLeavesFruit?((BlockDynamicLeavesFruit) state.getBlock()).fruitColor(state) : 0xFFFFFFFF;
						default: return 0xFFFFFFFF;
					}
				},
				ModContent.appleLeaves, ModContent.walnutLeaves, ModContent.chestnutLeaves,
				ModContent.cherryLeaves, ModContent.lemonLeaves, ModContent.plumLeaves);

		BlockDynamicLeaves[] specialLeaves = {TreeWillow.willowLeaves, ModContent.palmFrondLeaves, ModContent.papayaFrondLeaves};
		for (BlockDynamicLeaves leaves: specialLeaves) {
			ModelHelper.regColorHandler(leaves, (state, worldIn, pos, tintIndex) -> {

				Block block = state.getBlock();
				if (TreeHelper.isLeaves(block))
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
				return 0x00FF00FF; //Magenta
			});
		}
		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesForestry.MODID).values()) {
			ModelHelper.regColorHandler(leaves, (state, worldIn, pos, tintIndex) -> {

				Block block = state.getBlock();
				if (TreeHelper.isLeaves(block))
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
				return 0x00FF00FF; //Magenta
			});
		} // All leaves
	}

	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(ItemDynamicSeedMaple.EntityItemMapleSeed.class, new RenderMapleSeed.Factory());
	}
}
