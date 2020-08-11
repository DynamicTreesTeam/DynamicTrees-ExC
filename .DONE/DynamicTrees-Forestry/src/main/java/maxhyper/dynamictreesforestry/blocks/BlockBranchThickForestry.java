package maxhyper.dynamictreesforestry.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import forestry.api.arboriculture.EnumForestryWoodType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBranchThickForestry extends BlockBranchThick {

    protected EnumForestryWoodType woodType;

    public BlockBranchThickForestry(String name, EnumForestryWoodType type) {
        this(Material.WOOD, name, type);

    }

    public BlockBranchThickForestry(Material material, String name, EnumForestryWoodType type) {
        super(material, name);
        otherBlock = new BlockBranchThickForestry(material, name + "x", true, type);
        otherBlock.otherBlock = this;
        woodType = type;
        cacheBranchThickStates();
    }

    protected BlockBranchThickForestry(Material material, String name, boolean extended, EnumForestryWoodType type) {
        super(material, name, extended);
        woodType = type;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        int radius = getRadius(blockState);
        return woodType.getHardness() * (radius * radius) / 64.0f * 8.0f;
    }

}
