package maxhyper.dynamictreesnatura.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import maxhyper.dynamictreesnatura.ModContent;
import maxhyper.dynamictreesnatura.event.EventListenerNatura;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedBloodwood;
import maxhyper.dynamictreesnatura.items.ItemDynamicSeedMaple;
import maxhyper.dynamictreesnatura.model.ModelLoaderBlockBranchBloodwood;
import maxhyper.dynamictreesnatura.renderer.RenderBloodwoodSeed;
import maxhyper.dynamictreesnatura.renderer.RenderMapleSeed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.awt.*;

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
			ModelHelper.regColorHandler(leaves, (state, worldIn, pos, tintIndex) -> {
				//boolean inWorld = worldIn != null && pos != null;

				Block block = state.getBlock();

				if (TreeHelper.isLeaves(block)) {
					return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
				}
				return 0x00FF00FF; //Magenta
			});
		} // All leaves

		final int white = 0xFFFFFFFF;
		final BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
					if (state.getBlock() instanceof BlockRooty) {
						BlockRooty blockRooty = (BlockRooty) state.getBlock();
						switch(tintIndex) {
							case 0: { //Layer Zero is the green color of grass
								IBlockState muse = blockRooty.getMimic(world, pos);
								if(!(muse instanceof MimicProperty.IMimic)) { //Ensure we don't recurse endlessly
									return blockColors.colorMultiplier(muse, world, pos, tintIndex);
								}
							}
							case 1: return blockRooty.rootColor(state, world, pos); //Layer One is the root color
							default: return white; //All other color process unmultiplied
						}
					}
					return white;
				},
				ModContent.rootyNetherDirt, ModContent.rootyNetherUpsidedownDirt, ModContent.rootyUpsidedownDirt);
	}

	public static int addHexColor (Color color, int add){
		int R,G,B;
		R = Math.min(255, color.getRed() + add);
		G = Math.min(255, color.getGreen() + add);
		B = Math.min(255, color.getBlue() + add);
		return new Color(R,G,B).getRGB();
	}

	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(ItemDynamicSeedBloodwood.EntityItemBloodwoodSeed.class, new RenderBloodwoodSeed.Factory());
		RenderingRegistry.registerEntityRenderingHandler(ItemDynamicSeedMaple.EntityItemMapleSeed.class, new RenderMapleSeed.Factory());
	}
}
