package maxhyper.dynamictreesatum.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;


public class FeatureGenOphidianTongue implements IPostGenFeature {

    Block vine;
    int maxLength = 5;

    public FeatureGenOphidianTongue(){
        this.vine = Block.getBlockFromName("atum:ophidian_tongue");
    }

    private BlockPos getLeavesHeight (BlockPos rootPos, World world){
        for (int y= 1; y < 20; y++){
            BlockPos testPos = rootPos.up(y);
            if ((world.getBlockState(testPos).getBlock() instanceof BlockLeaves)){
                return testPos;
            }
        }
        return rootPos;
    }

    @Override
    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        boolean placed = false;
        for (int i=0;i<8;i++){
            if(world.rand.nextFloat() <= 0.4) {
                addVine(world, rootPos, getLeavesHeight(rootPos, world),1 + world.rand.nextInt(maxLength));
                placed = true;
            }
        }
        return placed;
    }

    private void addVine(World world, BlockPos rootPos, BlockPos leavesPos, int length) {
        if (rootPos.getY() == leavesPos.getY()){
            return;
        }
        EnumFacing placeDir = EnumFacing.HORIZONTALS[world.rand.nextInt(4)];
        PropertyBool prop = BlockVine.SOUTH;
        switch (placeDir){
            case EAST:
                prop = BlockVine.WEST;
                break;
            case SOUTH:
                prop = BlockVine.NORTH;
                break;
            case WEST:
                prop = BlockVine.EAST;
                break;
        }
        while (length > 0){
            if (world.isAirBlock(leavesPos.offset(placeDir))){
                world.setBlockState(leavesPos.offset(placeDir), vine.getDefaultState().withProperty(prop, true));
                leavesPos = leavesPos.down();
            }
            length--;
        }
    }
}