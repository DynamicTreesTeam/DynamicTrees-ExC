package maxhyper.dynamictreesatum.models;

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

public class ModelLoaderBlockPalmFronds extends ModelLoaderGeneric {

    public ModelLoaderBlockPalmFronds() {
        this("dynamicpalmfrondsatum");
    }
    public ModelLoaderBlockPalmFronds(String code) {
        super(code, new ResourceLocation("dynamictreesatum", "block/smartmodel/dynamicpalmfronds"));
    }

    @Override
    protected ModelBlock getBaseModelBlock(ResourceLocation virtualLocation) {
        if (!accepts(virtualLocation)) {
            return null;
        }

        String path = virtualLocation.getResourcePath(); // Extract the path portion of the ResourceLocation
        path = path.substring(0, path.length() - code.length()); // Remove the ending code from the location
        ResourceLocation location = new ResourceLocation(virtualLocation.getResourceDomain(), path); // Recreate the resource location without the code

        ModelBlock modelBlock = null;
        Reader reader = null;
        IResource iresource = null;

        System.out.println(getModelLocation(location));

        try {
            iresource = resourceManager.getResource(getModelLocation(location));
            reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
            modelBlock = ModelBlock.deserialize(reader);
            modelBlock.name = location.toString();

            ModelBlock rootParent = modelBlock;

            //Climb the hierarchy to discover the name of the root parent model
            while (rootParent.parent != null) {
                rootParent = rootParent.parent;
            }

            // If the name of the parent node is our model then we're good to go.
            if (rootParent.getParentLocation() != null && rootParent.getParentLocation().equals(resourceName)) {
                return modelBlock;
            }

            return null;

        } catch (IOException e) {
            System.out.println(getModelLocation(location));
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(iresource);
        }

        return null;
    }

    @Override
    protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
        return new ModelBlockPalmFronds(baseModelBlock);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}