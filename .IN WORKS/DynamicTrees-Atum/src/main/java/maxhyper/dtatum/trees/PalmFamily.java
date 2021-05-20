package maxhyper.dtatum.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.trees.Family;
import net.minecraft.util.ResourceLocation;

public class PalmFamily extends Family {
    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(PalmFamily::new);

    public PalmFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    public int getPrimaryThickness() {
        return 3;
    }

    @Override
    public int getSecondaryThickness() {
        return 3;
    }

//    @Override
//    public HashMap<BlockPos, BlockState> getFellingLeavesClusters(BranchDestructionData destructionData) {
//
//        if(destructionData.getNumEndpoints() < 1) {
//            return null;
//        }
//
//        HashMap<BlockPos, BlockState> leaves = new HashMap<>();
//        BlockPos relPos = destructionData.getEndPointRelPos(0).above();//A palm tree is only supposed to have one endpoint at it's top.
//        LeavesProperties leavesProperties = getCommonSpecies().getLeavesProperties();
//
//        leaves.put(relPos, leavesProperties.getDynamicLeavesState(4));//The barky overlapping part of the palm frond cluster
//        leaves.put(relPos.above(), leavesProperties.getDynamicLeavesState(3));//The leafy top of the palm frond cluster
//
//        //The 4 corners and 4 sides of the palm frond cluster
//        for(int hydro = 1; hydro <= 2; hydro++) {
//            IExtendedBlockState extState = (IExtendedBlockState) leavesProperties.getDynamicLeavesState(hydro);
//            for(CoordUtils.Surround surr : BlockDynamicLeavesPalm.hydroSurroundMap[hydro]) {
//                leaves.put(relPos.add(surr.getOpposite().getOffset()), extState.withProperty(BlockDynamicLeavesPalm.CONNECTIONS[surr.ordinal()], true));
//            }
//        }
//
//        return leaves;
//    }

}
