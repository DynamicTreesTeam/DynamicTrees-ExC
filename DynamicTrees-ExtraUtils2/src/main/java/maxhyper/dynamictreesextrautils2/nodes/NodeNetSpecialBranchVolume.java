package maxhyper.dynamictreesextrautils2.nodes;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NodeNetSpecialBranchVolume implements INodeInspector {

    public static final int VOXELSPERLOG = 4096; //A log contains 4096 voxels of wood material(16x16x16 pixels)

    private int volume;//number of voxels(1x1x1 pixels) of wood accumulated from network analysis

    @Override
    public boolean run(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir) {
        if(TreeHelper.isBranch(blockState) && world.getBlockState(pos).getBlock() != TreeHelper.getTreePart(blockState).getFamily(blockState, world, pos).getDynamicBranch()) {
            int radius = TreeHelper.getTreePart(blockState).getRadius(blockState);
            volume += radius * radius * 64;//Integrate volume of this tree part into the total volume calculation
        }
        return true;
    }

    @Override
    public boolean returnRun(IBlockState blockState, World world, BlockPos pos, EnumFacing fromDir) {
        return false;
    }

    public float getVolume() {
        return volume / (float)VOXELSPERLOG;
    }

}