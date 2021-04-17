package maxhyper.dynamictreestbl.models;

import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchBasic;
import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class ModelLoaderBlockSurfaceRootBLEvent extends ModelLoaderGeneric {

    public ModelLoaderBlockSurfaceRootBLEvent() {
        super("dynamicrootblevent", new ResourceLocation("dynamictreestbl", "block/smartmodel/branch"));
    }

    @Override
    protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
        return new ModelBlockSurfaceRootBLEvent(baseModelBlock);
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}