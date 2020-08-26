package maxhyper.dynamictreesatum.models;

import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.google.common.primitives.Ints;
import maxhyper.dynamictreesatum.blocks.BlockDynamicLeavesPalm;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class BakedModelBlockPalmFronds implements IBakedModel {

    protected ModelBlock modelBlock;

    TextureAtlasSprite barkParticles;

    private IBakedModel bakedFronds[] = new IBakedModel[8]; // 8 = Number of surrounding blocks

    public BakedModelBlockPalmFronds(ResourceLocation frondRes, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        modelBlock = new ModelBlock(null, null, null, false, false, ItemCameraTransforms.DEFAULT, null);

        TextureAtlasSprite frondIcon = bakedTextureGetter.apply(frondRes);
        barkParticles = frondIcon;

        for (CoordUtils.Surround surr : CoordUtils.Surround.values()) {

            SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(modelBlock, ItemOverrideList.NONE).setTexture(frondIcon);

            BlockVertexData[] quadData = {
                    new BlockVertexData(0, 0, 3, 15, 4),
                    new BlockVertexData(0, 1, 3, 15, 0),
                    new BlockVertexData(0, 1, 0, 0, 0),
                    new BlockVertexData(0, 0, 0, 0, 4),
                    new BlockVertexData(0, 0, 3, 15, 4),
                    new BlockVertexData(0, 0, 0, 0, 4),
                    new BlockVertexData(0, 1, 0, 0, 0),
                    new BlockVertexData(0, 1, 3, 15, 0)
            };

            for (int pass = 0; pass < 4; pass++) {
                for (int half = 0; half < 2; half++) {

                    BlockVertexData[] outData = new BlockVertexData[8];

                    for (int v = 0; v < 8; v++) {

                        // Nab the vertex;
                        float x = quadData[v].x;
                        float z = quadData[v].z;
                        float y = quadData[v].y;

                        x *= (40f / 32f);
                        z *= (40f / 32f);

                        double len;
                        double angle;
                        double mult;

                        // Rotate the vertex around x0,y=0.75
                        // Rotate on z axis
                        len = 0.75 - y;
                        angle = Math.atan2(x, y);
                        angle += Math.PI * (half == 1 ? 0.8 : -0.8);
                        x = (float) (Math.sin(angle) * len);
                        y = (float) (Math.cos(angle) * len);


                        // Rotate the vertex around x0,z0
                        // Rotate on x axis
                        len = Math.sqrt(y * y + z * z);
                        angle = Math.atan2(y, z);
                        switch (pass){
                            case 0:
                                mult = -0.29;
                                break;
                            case 1:
                                mult = -0.06;
                                break;
                            case 2:
                                mult = 0.16;
                                break;
                            case 3:
                                mult = 0.32;
                                break;
                            default:
                                mult = 0;
                        }
                        angle += Math.PI * mult;
                        y = (float) (Math.sin(angle) * len);
                        z = (float) (Math.cos(angle) * len);


                        // Rotate the vertex around x0,z0
                        // Rotate on y axis
                        len = Math.sqrt(x * x + z * z);
                        angle = Math.atan2(x, z);
                        switch (pass){
                            case 3:
                            case 0:
                                mult = 0.005;
                                break;
                            case 1:
                                mult = 0.185;
                                break;
                            case 2:
                                mult = 0.08;
                                break;
                            default:
                                mult = 0;
                        }
                        angle += Math.PI * 0.25 * surr.ordinal() + (Math.PI * mult);
                        x = (float) (Math.sin(angle) * len);
                        z = (float) (Math.cos(angle) * len);


                        // Move to center of block
                        x += 0.5f;
                        z += 0.5f;
                        switch (pass){
                            case 0:
                                y += 0.125;
                                break;
                            case 2:
                                y += -0.125;
                                break;
                            default:
                                y += 0;
                        }
                        //y -= 0.25f;


                        // Move to center of palm crown
                        x += surr.getOffset().getX();
                        z += surr.getOffset().getZ();


                        outData[v] = new BlockVertexData(x, y, z, quadData[v].u, quadData[v].v);
                    }

                    builder.addGeneralQuad(
                            new BakedQuad(
                                    Ints.concat(
                                            outData[0].toInts(frondIcon),
                                            outData[1].toInts(frondIcon),
                                            outData[2].toInts(frondIcon),
                                            outData[3].toInts(frondIcon)
                                    ),
                                    0, null, frondIcon, false, DefaultVertexFormats.BLOCK)
                    );

                    builder.addGeneralQuad(
                            new BakedQuad(
                                    Ints.concat(
                                            outData[4].toInts(frondIcon),
                                            outData[5].toInts(frondIcon),
                                            outData[6].toInts(frondIcon),
                                            outData[7].toInts(frondIcon)
                                    ),
                                    0, null, frondIcon, false, DefaultVertexFormats.BLOCK)
                    );


                    bakedFronds[surr.ordinal()] = builder.makeBakedModel();

                }
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        LinkedList<BakedQuad> quads = new LinkedList<BakedQuad>();

        if (side == null && state != null && state.getBlock() instanceof BlockDynamicLeavesPalm && state instanceof IExtendedBlockState) {
            for (int i = 0; i < 8; i++) {
                Boolean b = ((IExtendedBlockState) state).getValue(BlockDynamicLeavesPalm.CONNECTIONS[i]);
                if(b != null && b.booleanValue()) {
                    quads.addAll(bakedFronds[i].getQuads(state, side, rand));
                }
            }
        }

        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    // used for block breaking shards
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return barkParticles;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return bakedFronds[0].getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }



    public class BlockVertexData {

        public float x;
        public float y;
        public float z;
        public int color;
        public float u;
        public float v;

        // Default format of the data in IBakedModel
        /*
         * DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.POSITION, 3));
         * DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.UBYTE, EnumUsage.COLOR, 4));
         * DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.UV, 2));
         * DEFAULT_BAKED_FORMAT.addElement(new VertexFormatElement(0, EnumType.BYTE, EnumUsage.PADDING, 4));
         */

        public BlockVertexData(float x, float y, float z, float u, float v) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.u = u;
            this.v = v;
            color = 0xFFFFFFFF;
        }

        public BlockVertexData(BakedQuad quad, int vIndex) {
            this(quad.getVertexData(), vIndex);
        }

        public BlockVertexData(int data[], int vIndex) {
            vIndex *= 7;
            x = Float.intBitsToFloat(data[vIndex++]);
            y = Float.intBitsToFloat(data[vIndex++]);
            z = Float.intBitsToFloat(data[vIndex++]);
            color = data[vIndex++];
            u = Float.intBitsToFloat(data[vIndex++]);
            v = Float.intBitsToFloat(data[vIndex++]);
        }

        public int[] toInts() {
            return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z),
                    color, Float.floatToRawIntBits(u), Float.floatToRawIntBits(v), 0, };
        }

        protected int[] toInts(TextureAtlasSprite texture) {
            return new int[] {
                    Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z),
                    color,
                    Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)),
                    0,
            };
        }

    }
}