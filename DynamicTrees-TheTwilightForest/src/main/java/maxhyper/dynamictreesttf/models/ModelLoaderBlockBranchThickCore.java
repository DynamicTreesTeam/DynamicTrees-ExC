package maxhyper.dynamictreesttf.models;

import com.ferreusveritas.dynamictrees.models.ICustomDamageModel;
import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockBranchBasic;
import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockBranchThick;
import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchThick;
import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;
import com.google.common.collect.ImmutableList;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import com.ferreusveritas.dynamictrees.client.ModelUtils;
import com.ferreusveritas.dynamictrees.util.CoordUtils.Surround;
import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.resources.IResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.util.LinkedList;
import java.util.List;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.property.IExtendedBlockState;

import com.ferreusveritas.dynamictrees.ModConfigs;

@SideOnly(Side.CLIENT)
public class ModelLoaderBlockBranchThickCore extends ModelLoaderGeneric {

    public ModelLoaderBlockBranchThickCore() {
        super("dynamicthickcore", new ResourceLocation("dynamictreesttf", "block/smartmodel/branch"));
    }

    protected ModelBlock getBaseModelBlock(ResourceLocation virtualLocation) {

        String path = virtualLocation.getResourcePath(); // Extract the path portion of the ResourceLocation
        path = path.substring(0, path.length() - code.length()); // Remove the ending code from the location
        ResourceLocation location = new ResourceLocation(virtualLocation.getResourceDomain(), path); // Recreate the resource location without the code

        ModelBlock modelBlock = null;
        Reader reader = null;
        IResource iresource = null;

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
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(iresource);
        }

        return null;
    }

    @Override
    protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
        return new ModelBlockBranchThickCore(baseModelBlock);
    }

    public class ModelBlockBranchThickCore extends ModelBlockBranchThick {

        public ResourceLocation barkTextureFace;

        public ModelBlockBranchThickCore(ModelBlock modelBlock) {
            super(modelBlock);
            this.barkTextureFace = new ResourceLocation(modelBlock.resolveTextureName("bark_icon"));
        }
        @Override
        public Collection<ResourceLocation> getTextures() {
            return ImmutableList.copyOf(new ResourceLocation[]{barkTexture, ringsTexture, thickRingsTexture, barkTextureFace});
        }
        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            try {
                return new BakedModelBlockBranchThickCore(barkTexture, ringsTexture, thickRingsTexture, barkTextureFace, bakedTextureGetter); //barkTextureFace,
            } catch (Exception exception) {
                System.err.println("BranchModel.bake() failed due to exception:" + exception);
                return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
            }
        }
    }

    protected ResourceLocation getModelLocation(ResourceLocation location) {
        return new ResourceLocation(DynamicTreesTTF.MODID, location.getResourcePath() + ".json");
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
