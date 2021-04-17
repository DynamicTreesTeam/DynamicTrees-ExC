package maxhyper.dynamictreestbl.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelBlockBranchBLEvent implements IModel {

    public ResourceLocation barkTexture;
    public ResourceLocation ringsTexture;

    public ResourceLocation barkTextureFrosty;
    public ResourceLocation ringsTextureFrosty;

    public ResourceLocation barkTextureSpook;
    public ResourceLocation ringsTextureSpook;

    boolean hasFrosty = true, hasSpooky = true;

    public ModelBlockBranchBLEvent(ModelBlock modelBlock) {
        barkTexture = new ResourceLocation(modelBlock.resolveTextureName("bark"));
        ringsTexture = new ResourceLocation(modelBlock.resolveTextureName("rings"));

        barkTextureFrosty = new ResourceLocation(modelBlock.resolveTextureName("bark_frosty"));
        ringsTextureFrosty = new ResourceLocation(modelBlock.resolveTextureName("rings_frosty"));

        barkTextureSpook = new ResourceLocation(modelBlock.resolveTextureName("bark_spook"));
        ringsTextureSpook = new ResourceLocation(modelBlock.resolveTextureName("rings_spook"));

        if (barkTexture == barkTextureFrosty && ringsTexture == ringsTextureFrosty) hasFrosty = false;
        if (barkTexture == barkTextureSpook && ringsTexture == ringsTextureSpook) hasSpooky = false;
    }

    // return all other resources used by this model
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.copyOf(new ResourceLocation[]{});
    }

    // return all the textures used by this model
    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.copyOf(new HashSet<>(Arrays.asList(barkTexture, ringsTexture, barkTextureFrosty, ringsTextureFrosty, barkTextureSpook, ringsTextureSpook)));
    }

    // Bake the subcomponents into a CompositeModel
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        try {
            return new BakedModelBlockBranchBLEvent(barkTexture, ringsTexture,
                    hasFrosty?barkTextureFrosty:null,  hasFrosty?ringsTextureFrosty:null, hasSpooky?barkTextureSpook:null, hasSpooky?ringsTextureSpook:null,
                    bakedTextureGetter);
        } catch (Exception exception) {
            System.err.println("BranchModel.bake() failed due to exception:" + exception);
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }

}