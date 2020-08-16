package maxhyper.dynamictreesnatura.renderer;

import maxhyper.dynamictreesnatura.items.ItemDynamicSeedBloodwood.EntityItemBloodwoodSeed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBloodwoodSeed extends Render<EntityItemBloodwoodSeed> {

    private final RenderItem itemRenderer;
    private Render<Entity> vanillaEntityItemRenderer;

    public RenderBloodwoodSeed(RenderManager renderManagerIn, RenderItem renderItem) {
        super(renderManagerIn);
        this.itemRenderer = renderItem;
        this.vanillaEntityItemRenderer = renderManagerIn.getEntityClassRenderObject(EntityItem.class);
        this.shadowSize = 0F;
        this.shadowOpaque = 0F;
    }

    @Override
    public void doRender(EntityItemBloodwoodSeed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        vanillaEntityItemRenderer.doRender(entity, x, y-0.25, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityItemBloodwoodSeed entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }


    public static class Factory implements IRenderFactory<EntityItemBloodwoodSeed> {

        @Override
        public Render<EntityItemBloodwoodSeed> createRenderFor(RenderManager manager) {
            return new RenderBloodwoodSeed(manager, Minecraft.getMinecraft().getRenderItem());
        }

    }

}
