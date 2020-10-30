package maxhyper.dynamictreestbl.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderDelegated;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModContent;
import maxhyper.dynamictreestbl.event.EventListenerTBL;
import maxhyper.dynamictreestbl.models.ModelBlockBranchHearthgrove;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import thebetweenlands.common.block.terrain.BlockSwampWater;
import thebetweenlands.common.registries.BlockRegistry;

import static maxhyper.dynamictreestbl.ModContent.blockRootyMud;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new EventListenerTBL());
		ModelLoaderRegistry.registerLoader(
				new ModelLoaderDelegated(
						"dynamicmangrovetbl", new ResourceLocation("dynamictrees", "block/smartmodel/branch"),
						(resloc, baseModelBlock) -> new ModelBlockBranchHearthgrove(baseModelBlock)
				)
		);
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

		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesTBL.MODID).values()) {
			ModelHelper.regColorHandler(leaves, (state, worldIn, pos, tintIndex) -> {
				//boolean inWorld = worldIn != null && pos != null;
				Block block = state.getBlock();
				if (TreeHelper.isLeaves(block)) {
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
				}
				return 0x00FF00FF; //Magenta
			});
		}

		ModelHelper.regColorHandler(ModContent.blockRootyWater, (state, world, pos, tintIndex) -> {
			int color = 0xFFFFFF;
			if(tintIndex != 2) {
				color = Minecraft.getMinecraft().getBlockColors().colorMultiplier(Blocks.WATER.getDefaultState(), world, pos, tintIndex);
			}
			return color;
		});
		ModelHelper.regColorHandler(ModContent.blockRootyWaterSwamp, (state, world, pos, tintIndex) -> {
			int color = 0xFFFFFF;
			if(tintIndex != 2) {
				color = ((BlockSwampWater)BlockRegistry.SWAMP_WATER).getColorMultiplier(BlockRegistry.SWAMP_WATER.getDefaultState(), world, pos, tintIndex);
			}
			return color;
		});
		ModelHelper.regColorHandler(ModContent.blockRootyWaterStagnant, (state, world, pos, tintIndex) -> {
			int color = 0xFFFFFF;
			if(tintIndex != 2) {
				color = Minecraft.getMinecraft().getBlockColors().colorMultiplier(BlockRegistry.STAGNANT_WATER.getDefaultState(), world, pos, tintIndex);
			}
			return color;
		});

		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
					switch(tintIndex) {
						case 0: return blockColors.colorMultiplier(blockRootyMud.getMimic(world, pos), world, pos, tintIndex);
						case 1: return state.getBlock() instanceof BlockRooty ? ((BlockRooty) state.getBlock()).rootColor(state, world, pos) : 0xFFFFFFFF;
						default: return 0xFFFFFFFF;
					}
				}, // Rooty Dirt
				blockRootyMud);
	}
}
