package maxhyper.dynamictreesexc.blocks;
import com.ferreusveritas.dynamictrees.ModConfigs;

import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import slimeknights.tconstruct.world.TinkerWorld;

public class SlimeMimicProperty extends MimicProperty {

    public SlimeMimicProperty(String name) {
        super(name);
    }

    @Override
    public boolean isValid(IBlockState value) {
        return value != null;
    }

    @Override
    public Class<IBlockState> getType() {
        return IBlockState.class;
    }

    @Override
    public String valueToString(IBlockState value) {
        return value.toString();
    }
    public static IBlockState getSlimeMimic(IBlockAccess access, BlockPos pos) {

        if(!ModConfigs.rootyTextureMimicry) {
            return TinkerWorld.slimeGrass.getDefaultState();
        }

        final int dMap[] = {0, -1, 1};//Y-Axis depth map

        IBlockState mimic = TinkerWorld.slimeDirt.getDefaultState();//Default to dirt in case no dirt or grass is found
        IBlockState cache[] = new IBlockState[12];//A cache so we don't need to pull the blocks from the world twice
        int i = 0;

        //Prioritize Grass by searching for grass first
        for (int depth : dMap) {
            for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                IBlockState ground = cache[i++] = access.getBlockState(pos.offset(dir).down(depth));
                if (ground.getBlock() == TinkerWorld.slimeGrass && ground.isBlockNormalCube()) {
                    return ground;
                }
            }
        }

        //Settle for other kinds of dirt
        for (i = 0; i < 12; i++) {
            IBlockState ground = cache[i];
            if(ground != mimic && ground.getBlock() == TinkerWorld.slimeDirt && ground.isBlockNormalCube() && !(ground.getBlock() instanceof IMimic)) {
                return ground;
            }
        }

        //If all else fails then just return plain ol' dirt
        return mimic;
    }

    public static interface IMimic {
        IBlockState getMimic(IBlockAccess access, BlockPos pos);
    }
}
