package maxhyper.dynamictreesttf.models;

import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchBasic;

import com.ferreusveritas.dynamictrees.models.loaders.ModelLoaderGeneric;
import maxhyper.dynamictreesttf.DynamicTreesTTF;
import maxhyper.dynamictreesttf.blocks.BlockDynamicTwilightRootsExposed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.function.Function;

import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockBranchBasic;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

@SideOnly(Side.CLIENT)
public class ModelLoaderBlockUndergroundRoot extends ModelLoaderGeneric {

    public ModelLoaderBlockUndergroundRoot() {
        super("undergroundroot", new ResourceLocation("dynamictrees", "block/smartmodel/branch"));
    }

    @Override
    protected IModel loadModel(ResourceLocation resourceLocation, ModelBlock baseModelBlock) {
        return new ModelBlockBranchBasic(baseModelBlock){
            @Override
            public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
                try {
                    return new BakedModelBlockBranchBasic(barkTexture, ringsTexture, bakedTextureGetter){
                        @Override
                        protected int getRadius(IBlockState blockState) {
                            return ((BlockDynamicTwilightRootsExposed) blockState.getBlock()).getRadius(blockState);
                        }
                        @Override
                        protected int[] pollConnections(int coreRadius, IExtendedBlockState extendedBlockState) {
                            int[] connections = new int[6];
                            for(EnumFacing dir: EnumFacing.VALUES) {
                                int connection = getConnectionRadius(extendedBlockState, BlockDynamicTwilightRootsExposed.CONNECTIONS[dir.getIndex()]);
                                connections[dir.getIndex()] = MathHelper.clamp(connection, 0, coreRadius);//Do not allow connections to exceed core radius
                            }
                            return connections;
                        }
                    };
                } catch (Exception exception) {
                    System.err.println("BranchModel.bake() failed due to exception:" + exception);
                    return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
                }
            }
        };
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
