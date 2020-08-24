package maxhyper.dynamictreesatum.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

public class ModelBlockPalmFronds implements IModel {

    public ResourceLocation frondTexture;

    public ModelBlockPalmFronds(ModelBlock modelBlock) {
        frondTexture = new ResourceLocation(modelBlock.resolveTextureName("frond"));
    }

    // return all other resources used by this model
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.copyOf(new ResourceLocation[]{});
    }

    // return all the textures used by this model
    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.copyOf(new ResourceLocation[]{frondTexture});
    }

    // Bake the subcomponents into a CompositeModel
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        try {
            return new BakedModelBlockPalmFronds(frondTexture, bakedTextureGetter);

        } catch (Exception exception) {
            System.err.println("PalmFrondsModel.bake() failed due to exception:" + exception);
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }
}