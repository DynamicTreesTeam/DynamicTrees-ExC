package maxhyper.dtatum.models;

import com.ferreusveritas.dynamictrees.blocks.leaves.PalmLeavesProperties;
import com.ferreusveritas.dynamictrees.client.ModelUtils;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.google.common.primitives.Ints;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PalmLeavesBakedModel implements IDynamicBakedModel {

    public static List<PalmLeavesBakedModel> INSTANCES = new ArrayList<>();

    protected final BlockModel blockModel;

    ResourceLocation frondsResLoc;
    TextureAtlasSprite frondsTexture;

    private final IBakedModel[] bakedFronds = new IBakedModel[8]; // 8 = Number of surrounding blocks

    public PalmLeavesBakedModel (ResourceLocation modelResLoc, ResourceLocation frondsResLoc){
        this.blockModel = new BlockModel(null, new ArrayList<>(), new HashMap<>(), false, BlockModel.GuiLight.FRONT, ItemCameraTransforms.NO_TRANSFORMS, ItemOverrideList.EMPTY.getOverrides());
        this.frondsResLoc = frondsResLoc;
        INSTANCES.add(this);
    }

    public void setupModels (){
        frondsTexture = ModelUtils.getTexture(frondsResLoc);

        for (CoordUtils.Surround surr : CoordUtils.Surround.values()) {

            SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(blockModel.customData, ItemOverrideList.EMPTY).particle(frondsTexture);

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
                            default:
                            case 3:
                            case 0:
                                mult = 0;
                                break;
                            case 1:
                                mult = 0.185 - 0.25;
                                break;
                            case 2:
                                mult = 0.08;
                                break;
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

                    int[] vertices = Ints.concat(
                            outData[0].toInts(frondsTexture),
                            outData[1].toInts(frondsTexture),
                            outData[2].toInts(frondsTexture),
                            outData[3].toInts(frondsTexture)
                    );
                    builder.addUnculledFace(new BakedQuad(vertices,
                                    0, FaceBakery.calculateFacing(vertices), frondsTexture, false)
                    );

                    vertices = Ints.concat(
                            outData[4].toInts(frondsTexture),
                            outData[5].toInts(frondsTexture),
                            outData[6].toInts(frondsTexture),
                            outData[7].toInts(frondsTexture)
                    );
                    builder.addUnculledFace(new BakedQuad(vertices,
                                    0, FaceBakery.calculateFacing(vertices), frondsTexture, false)
                    );


                    bakedFronds[surr.ordinal()] = builder.build();

                }
            }
        }
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (state == null || side != null)
            return Collections.emptyList();

        LinkedList<BakedQuad> quads = new LinkedList<>();

        int direction = state.getValue(PalmLeavesProperties.DynamicPalmLeavesBlock.DIRECTION);

        if (direction != 0)
            quads.addAll(bakedFronds[direction-1].getQuads(state, null, rand, extraData));


        return quads;
    }

//    @Nonnull
//    @Override
//    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
//        final Block block = state.getBlock();
//
//        if (!(block instanceof PalmLeavesProperties.DynamicPalmLeavesBlock))
//            return new ModelPalmSurround();
//
//        return new ModelPalmSurround(((PalmLeavesProperties.DynamicPalmLeavesBlock) block).getHydroSurround(state, world, pos), state.getValue(DynamicLeavesBlock.DISTANCE));
//    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return frondsTexture;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public boolean doesHandlePerspectives() {
        return false;
    }

//    public static class ModelPalmSurround implements IModelData {
//
//        private final int hydro;
//        private final boolean[] surround;
//
//        public ModelPalmSurround() {
//            this(new boolean[8], 0);
//        }
//
//        public ModelPalmSurround(boolean[] surround, int hydro) {
//            this.surround = surround;
//            this.hydro = hydro;
//        }
//
//        public boolean[] getSurround (){
//            return surround;
//        }
//
//        public int getHydro (){
//            return hydro;
//        }
//
//        @Override
//        public boolean hasProperty(ModelProperty<?> prop) {
//            return false;
//        }
//
//        @Nullable
//        @Override
//        public <T> T getData(ModelProperty<T> prop) {
//            return null;
//        }
//
//        @Nullable
//        @Override
//        public <T> T setData(ModelProperty<T> prop, T data) {
//            return null;
//        }
//    }

    public static class BlockVertexData {

        public float x;
        public float y;
        public float z;
        public int color;
        public float u;
        public float v;

        public BlockVertexData(float x, float y, float z, float u, float v) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.u = u;
            this.v = v;
            color = 0xFFFFFFFF;
        }

        public BlockVertexData(BakedQuad quad, int vIndex) {
            this(quad.getVertices(), vIndex);
        }

        public BlockVertexData(int[] data, int vIndex) {
            vIndex *= 8;
            x = Float.intBitsToFloat(data[vIndex++]);
            y = Float.intBitsToFloat(data[vIndex++]);
            z = Float.intBitsToFloat(data[vIndex++]);
            color = data[vIndex++];
            u = Float.intBitsToFloat(data[vIndex++]);
            v = Float.intBitsToFloat(data[vIndex]);
        }

        public int[] toInts() {
            return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z),
                    color, Float.floatToRawIntBits(u), Float.floatToRawIntBits(v), 0, 0 };
        }

        protected int[] toInts(TextureAtlasSprite texture) {
            return new int[] {
                    Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z),
                    color,
                    Float.floatToRawIntBits(texture.getU(u)), Float.floatToRawIntBits(texture.getV(v)),
                    0, 0
            };
        }

    }

}
