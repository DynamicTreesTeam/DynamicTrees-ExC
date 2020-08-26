package maxhyper.dynamictreesnatura.model;

import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchThick;
import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ModelLoaderBlockBranchBloodwood extends ModelLoaderGeneric {

    public ModelLoaderBlockBranchBloodwood() {
        super("dynamicthickupsidedown", new ResourceLocation("dynamictrees", "block/smartmodel/branch"));
    }

    @Override
    protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
        return new ModelBlockBranchThickUpsideDown(baseModelBlock);
    }

    public class ModelBlockBranchThickUpsideDown extends ModelBlockBranchThick {

        public ModelBlockBranchThickUpsideDown(ModelBlock modelBlock) {
            super(modelBlock);
        }
        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            try {
                return new BakedModelBlockBranchBloodwood(barkTexture, ringsTexture, thickRingsTexture, bakedTextureGetter); //barkTextureFace,
            } catch (Exception exception) {
                System.err.println("BranchModel.bake() failed due to exception:" + exception);
                return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
            }
        }
    }

    protected ResourceLocation getModelLocation(ResourceLocation location) {
        return new ResourceLocation(DynamicTreesNatura.MODID, location.getResourcePath() + ".json");
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
