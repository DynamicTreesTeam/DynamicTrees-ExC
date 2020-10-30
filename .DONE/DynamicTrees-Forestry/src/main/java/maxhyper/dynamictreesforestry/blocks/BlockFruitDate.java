package maxhyper.dynamictreesforestry.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockFruitCocoa;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class BlockFruitDate extends BlockFruitCocoa {

    private int fruitMeta;
    private Species species;

    public BlockFruitDate (String name, int fruitMeta){
        super(name);
        this.fruitMeta = fruitMeta;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    private static final double offset = 0.31;

    protected static final AxisAlignedBB[] COCOA_EAST_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.6875D + offset, 0.4375D, 0.375D, 0.9375D + offset, 0.75D, 0.625D),
            new AxisAlignedBB(0.5625D + offset, 0.3125D, 0.3125D, 0.9375D + offset, 0.75D, 0.6875D),
            new AxisAlignedBB(0.4375D + offset, 0.1875D, 0.25D, 0.9375D + offset, 0.75D, 0.75D)
    };
    protected static final AxisAlignedBB[] COCOA_WEST_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0625D - offset, 0.4375D, 0.375D, 0.3125D - offset, 0.75D, 0.625D),
            new AxisAlignedBB(0.0625D - offset, 0.3125D, 0.3125D, 0.4375D - offset, 0.75D, 0.6875D),
            new AxisAlignedBB(0.0625D - offset, 0.1875D, 0.25D, 0.5625D - offset, 0.75D, 0.75D)
    };
    protected static final AxisAlignedBB[] COCOA_NORTH_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.375D, 0.4375D, 0.0625D - offset, 0.625D, 0.75D, 0.3125D - offset),
            new AxisAlignedBB(0.3125D, 0.3125D, 0.0625D - offset, 0.6875D, 0.75D, 0.4375D - offset),
            new AxisAlignedBB(0.25D, 0.1875D, 0.0625D - offset, 0.75D, 0.75D, 0.5625D - offset)
    };
    protected static final AxisAlignedBB[] COCOA_SOUTH_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.375D, 0.4375D, 0.6875D + offset, 0.625D, 0.75D, 0.9375D + offset),
            new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D + offset, 0.6875D, 0.75D, 0.9375D + offset),
            new AxisAlignedBB(0.25D, 0.1875D, 0.4375D + offset, 0.75D, 0.75D, 0.9375D + offset)
    };


    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        int i = state.getValue(AGE);

        switch (state.getValue(FACING))
        {
            case SOUTH:
                return COCOA_SOUTH_AABB[i];
            case NORTH:
            default:
                return COCOA_NORTH_AABB[i];
            case WEST:
                return COCOA_WEST_AABB[i];
            case EAST:
                return COCOA_EAST_AABB[i];
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        pos = pos.offset(state.getValue(FACING));
        IBlockState branchState = world.getBlockState(pos);
        BlockBranch branch = TreeHelper.getBranch(branchState);
        return branch != null && branch.getRadius(branchState) == 3 && branch.getFamily().canSupportCocoa;
    }

    private ItemStack fruitItem (){
        return new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry", "fruits"))), 1, fruitMeta);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return fruitItem();
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        int i = state.getValue(AGE);
        if (i == 2){
            drops.add(fruitItem());
        }
    }

}
