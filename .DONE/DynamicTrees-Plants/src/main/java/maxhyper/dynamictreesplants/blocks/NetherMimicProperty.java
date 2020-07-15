package maxhyper.dynamictreesplants.blocks;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class NetherMimicProperty extends MimicProperty {

    public NetherMimicProperty(String name) {
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
    public static IBlockState getNetherMimic(IBlockAccess access, BlockPos pos) {
        if(!ModConfigs.rootyTextureMimicry) {
            return Blocks.NETHERRACK.getDefaultState();
        }

        final int dMap[] = {0, -1, 1};//Y-Axis depth map

        IBlockState mimic = Blocks.NETHERRACK.getDefaultState();//Default to netherrack
        IBlockState cache[] = new IBlockState[12];//A cache so we don't need to pull the blocks from the world twice
        int i = 0;

        for (int depth : dMap) {
            for (EnumFacing dir : EnumFacing.HORIZONTALS) {
                IBlockState ground = cache[i++] = access.getBlockState(pos.offset(dir).down(depth));
                if ((ground.getBlock() == Blocks.NETHERRACK || ground.getBlock() == Blocks.SOUL_SAND )&& ground.isBlockNormalCube()) {
                    return ground;
                }
            }
        }

        //If all else fails then just return plain ol' dirt
        return mimic;
    }
}