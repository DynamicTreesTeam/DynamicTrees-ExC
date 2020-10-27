package maxhyper.dynamictreesttf.models;

import com.ferreusveritas.dynamictrees.models.blockmodels.ModelBlockBranchBasic;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

public class ModelBlockBranchMangrove extends ModelBlockBranchBasic {
	
	public ResourceLocation rootsTexture;
	
	public ModelBlockBranchMangrove(ModelBlock modelBlock) {
		super(modelBlock);
		rootsTexture = new ResourceLocation(modelBlock.resolveTextureName("roots"));
	}
	
	// return all other resources used by this model
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.copyOf(new ResourceLocation[]{});
	}

	// return all the textures used by this model
	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableList.copyOf(new ResourceLocation[]{barkTexture, ringsTexture, rootsTexture});
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {
			return new BakedModelBlockBranchMangrove(barkTexture, ringsTexture, rootsTexture, bakedTextureGetter);
		} catch (Exception exception) {
			System.err.println("MangroveModel.bake() failed due to exception:" + exception);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}
}
