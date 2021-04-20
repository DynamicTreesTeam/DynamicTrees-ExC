package maxhyper.dynamictreestconstruct.event;

import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockRooty;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.TConstruct;

import java.util.ArrayList;
import java.util.List;

import static maxhyper.dynamictreestconstruct.ModContent.rootySlimyDirt;

public class EventListenerTConstruct {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        Block[] rootyBlocks = new Block[] {rootySlimyDirt};
        for(Block block: rootyBlocks) {
            IBakedModel rootsObject = event.getModelRegistry().getObject(new ModelResourceLocation(block.getRegistryName(), "normal"));
            if (rootsObject != null) {
                BakedModelBlockRooty rootyModel = new BakedModelBlockRooty(rootsObject){
                    @Override
                    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
                        List<BakedQuad> quads = new ArrayList<>(16);

                        if (state != null && state.getBlock() instanceof MimicProperty.IMimic && state instanceof IExtendedBlockState) {
                            IExtendedBlockState extendedState = ((IExtendedBlockState) state);
                            IBlockState mimicState = extendedState.getValue(MimicProperty.MIMIC);

                            Minecraft mc = Minecraft.getMinecraft();
                            BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
                            BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
                            IBakedModel mimicModel = blockModelShapes.getModelForState(mimicState);

                            boolean isMimicTranslucent = mimicState.getBlock().canRenderInLayer(mimicState, BlockRenderLayer.TRANSLUCENT);
                            boolean transluscentLayer = MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT;
                            if (isMimicTranslucent == transluscentLayer){
                                quads.addAll(mimicModel.getQuads(mimicState, side, rand));
                            }

                            if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.CUTOUT_MIPPED) {
                                quads.addAll(rootsModel.getQuads(state, side, rand));
                            }
                        }

                        return quads;
                    }
                };
                event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "normal"), rootyModel);
            }
        }
    }

}