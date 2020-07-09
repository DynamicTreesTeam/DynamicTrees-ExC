package maxhyper.dynamictreesttf.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.ModContent;
import maxhyper.dynamictreesttf.models.ModelLoaderBlockBranchThickCore;
import maxhyper.dynamictreesttf.models.ModelLoaderBlockUndergroundRoot;
import maxhyper.dynamictreesttf.trees.TreeDarkwood;
import maxhyper.dynamictreesttf.trees.TreeMagicTransformation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.Collection;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		//ModelLoaderRegistry.registerLoader(new CustomModelLoaderWrapped());
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockUndergroundRoot());
		ModelLoaderRegistry.registerLoader(new ModelLoaderBlockBranchThickCore());
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

		Block[] grassyBlocks = {ModContent.undergroundRoot, ModContent.undergroundMangroveRoot};
		for (Block block: grassyBlocks) {
			ModelHelper.regColorHandler(block, new IBlockColor() {
				@Override public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
					return worldIn.getBiome(pos).getGrassColorAtPos(pos);
				}
			});
		}
		BlockDynamicLeaves[] specialLeaves = {TreeDarkwood.darkwoodLeaves, TreeMagicTransformation.transformationLeaves};
		for (BlockDynamicLeaves leaves: specialLeaves) {
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
		for (BlockDynamicLeaves leaves: LeavesPaging.getLeavesMapForModId(DynamicTreesTTF.MODID).values()) {
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
		} // All other leaves

		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
					switch(tintIndex) {
						case 0: return blockColors.colorMultiplier(ModContent.rootyDirtMangrove.getMimic(world, pos), world, pos, tintIndex);
						case 1: return state.getBlock() instanceof BlockRooty ? ((BlockRooty) state.getBlock()).rootColor(state, world, pos) : 0xFFFFFFFF;
						default: return 0xFFFFFFFF;
					}
				}, // Rooty Dirt
				ModContent.rootyDirtMangrove);
	}
}
