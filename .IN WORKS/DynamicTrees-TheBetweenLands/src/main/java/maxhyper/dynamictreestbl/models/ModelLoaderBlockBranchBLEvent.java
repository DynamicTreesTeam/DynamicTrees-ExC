package maxhyper.dynamictreestbl.models;

import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchBasic;
import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@SideOnly(Side.CLIENT)
public class ModelLoaderBlockBranchBLEvent extends ModelLoaderGeneric {

    public ModelLoaderBlockBranchBLEvent() {
        super("dynamictreeblevent", new ResourceLocation(DynamicTreesTBL.MODID, "block/smartmodel/branch"));
    }

    @Override
    protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
        return new ModelBlockBranchBLEvent(baseModelBlock);
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}