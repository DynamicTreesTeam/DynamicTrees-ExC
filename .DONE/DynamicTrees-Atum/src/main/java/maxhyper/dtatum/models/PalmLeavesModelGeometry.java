package maxhyper.dtatum.models;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class PalmLeavesModelGeometry implements IModelGeometry<PalmLeavesModelGeometry> {

    protected final ResourceLocation frondsResLoc;

    public PalmLeavesModelGeometry (@Nullable final ResourceLocation frondsResLoc){
        this.frondsResLoc = frondsResLoc;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return new PalmLeavesBakedModel(modelLocation, frondsResLoc);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        if (frondsResLoc == null) return new HashSet<>();
        return Collections.singleton(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, frondsResLoc));
    }
}
