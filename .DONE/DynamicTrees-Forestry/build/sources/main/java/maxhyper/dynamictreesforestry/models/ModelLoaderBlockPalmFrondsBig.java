package maxhyper.dynamictreesforestry.models;

import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ModelLoaderBlockPalmFrondsBig extends ModelLoaderBlockPalmFronds {

    public ModelLoaderBlockPalmFrondsBig() {
        super("dynamicpalmfrondsdate");
    }
    @Override
    protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
        return new ModelBlockPalmFronds(baseModelBlock, true);
    }
}