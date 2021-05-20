package maxhyper.dtatum.leavesProperties;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class PalmLeavesProperties extends LeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(PalmLeavesProperties::new);

    public PalmLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(AbstractBlock.Properties properties) {
        return new DynamicPalmLeavesBlock(this, properties);
    }

    public static class DynamicPalmLeavesBlock extends DynamicLeavesBlock {

        public static final CoordUtils.Surround[][] hydroSurroundMap = new CoordUtils.Surround[][] {
                {}, //distance 0
                {CoordUtils.Surround.NE, CoordUtils.Surround.SE, CoordUtils.Surround.SW, CoordUtils.Surround.NW}, //distance 1
                {CoordUtils.Surround.N, CoordUtils.Surround.E, CoordUtils.Surround.S, CoordUtils.Surround.W}, //distance 2
                {}, //distance 3
                {}, //distance 4
                {}, //distance 5
                {}, //distance 6
                {}  //distance 7
        };

        public DynamicPalmLeavesBlock(LeavesProperties leavesProperties, Properties properties) {
            super(leavesProperties, properties);
        }

        public boolean[] getHydroSurround (BlockState state, IBlockReader access, BlockPos pos){
            boolean[] hydroSurround = new boolean[8];
            for(CoordUtils.Surround surr : hydroSurroundMap[state.getValue(DynamicLeavesBlock.DISTANCE)]) {
                BlockState scanState = access.getBlockState(pos.offset(surr.getOffset()));
                if(scanState.getBlock() == this) {
                    if( scanState.getValue(DynamicLeavesBlock.DISTANCE) == 3 ) {
                        hydroSurround[surr.ordinal()] = true;
                    }
                }
            }
            return hydroSurround;
        }

        @Override
        public int getRadiusForConnection(BlockState blockState, IBlockReader world, BlockPos pos, BranchBlock from, Direction side, int fromRadius) {
            return side == Direction.UP && from.getFamily().isCompatibleDynamicLeaves(Species.NULL_SPECIES, blockState, world, pos) ? fromRadius : 0;
        }

        @Override
        public int branchSupport(BlockState blockState, IBlockReader blockAccess, BranchBlock branch, BlockPos pos, Direction dir, int radius) {
            return branch.getFamily() == getFamily(blockState, blockAccess, pos) ? BranchBlock.setSupport(0, 1) : 0;
        }

//        @Override
//        public VoxelShape getOcclusionShape(BlockState state, IBlockReader world, BlockPos pos) {
//            AxisAlignedBB base = super.getOcclusionShape(state, world, pos).bounds();
//            base.inflate(1, 0, 1);
//            base.inflate(-1,-0,-1);
//            return VoxelShapes.create(base);
//        }

    }

}
