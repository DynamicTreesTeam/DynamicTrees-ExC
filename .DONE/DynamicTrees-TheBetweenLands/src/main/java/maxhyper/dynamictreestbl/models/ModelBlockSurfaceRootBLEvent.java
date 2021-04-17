package maxhyper.dynamictreestbl.models;

import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockSurfaceRoot;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelBlockSurfaceRootBLEvent implements IModel {

    public ResourceLocation barkTexture;

    public ResourceLocation barkTextureFrosty;

    public ResourceLocation barkTextureSpook;

    boolean hasFrosty = true, hasSpooky = true;

    public ModelBlockSurfaceRootBLEvent(ModelBlock modelBlock) {
        barkTexture = new ResourceLocation(modelBlock.resolveTextureName("bark"));

        barkTextureFrosty = new ResourceLocation(modelBlock.resolveTextureName("bark_frosty"));

        barkTextureSpook = new ResourceLocation(modelBlock.resolveTextureName("bark_spook"));

        if (barkTexture == barkTextureFrosty) hasFrosty = false;
        if (barkTexture == barkTextureSpook) hasSpooky = false;
    }

    // return all other resources used by this model
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.copyOf(new ResourceLocation[]{});
    }

    // return all the textures used by this model
    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.copyOf(new HashSet<>(Arrays.asList(barkTexture, barkTextureFrosty, barkTextureSpook)));
    }

    // Bake the subcomponents into a CompositeModel
    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        try {
            return new BakedModelBlockSurfaceRootBLEvent(barkTexture,
                    hasFrosty?barkTextureFrosty:null, hasSpooky?barkTextureSpook:null,
                    bakedTextureGetter);
        } catch (Exception exception) {
            System.err.println("BakedModelBlockSurfaceRoot.bake() failed due to exception:" + exception);
            return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
        }
    }

}