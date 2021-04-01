package maxhyper.dynamictreesforestry.models;

import java.util.ArrayList;
import java.util.List;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty.IMimic;

import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesFruit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BakedModelBlockFruitLeaves implements IBakedModel  {

    protected IBakedModel fruitModel;
    protected IBlockState nonFruityLeaves;

    public BakedModelBlockFruitLeaves(IBakedModel rootsModel, IBlockState normalLeaves) {
        this.fruitModel = rootsModel;
        nonFruityLeaves = normalLeaves;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>(16);

        if (state != null && state.getBlock() instanceof BlockDynamicLeavesFruit) {
            BlockModelShapes blockModelShapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
            IBakedModel leafModel = blockModelShapes.getModelForState(nonFruityLeaves);

            quads.addAll(leafModel.getQuads(nonFruityLeaves, side, rand));
            quads.addAll(fruitModel.getQuads(state, side, rand));
        }

        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return fruitModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }

}
