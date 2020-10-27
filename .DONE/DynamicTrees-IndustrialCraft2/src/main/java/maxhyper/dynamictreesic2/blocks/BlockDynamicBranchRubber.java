package maxhyper.dynamictreesic2.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import maxhyper.dynamictreesic2.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static maxhyper.dynamictreesic2.DynamicTreesIC2.proxyIC2;

public class BlockDynamicBranchRubber extends BlockBranchBasic {

    private static final int nothingToEmptyChance = 200;
    private static final int emptyToFilledChance = 50;

    public BlockDynamicBranchRubber(boolean tick, String name) {
        super(new ResourceLocation(DynamicTreesIC2.MODID,proxyIC2.IC2GetTreeID() + name).toString());
        setTickRandomly(tick);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    };

    @Override
    public int getMaxRadius() {
        return 7;
    }

    @Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        performUpdate(worldIn, pos);
    }

    private static IBlockState stateFromAge(int age){
        switch (age){
            case 0:
                return ModContent.rubberBranch.getDefaultState();
            case 1:
                return ModContent.rubberBranchEmpty.getDefaultState();
            case 2:
                return ModContent.rubberBranchFilled.getDefaultState();
        }
        throw new IllegalArgumentException();
    }

    private static int ageFromState(IBlockState state){
        if (state.getBlock() == ModContent.rubberBranchFilled){
            return 2;
        } else if (state.getBlock() == ModContent.rubberBranchEmpty){
            return 1;
        } else if (state.getBlock() == ModContent.rubberBranch){
            return 0;
        } else return -1;
    }

    private static void performUpdate(World worldIn, BlockPos pos){
        if (worldIn.getBlockState(pos).getValue(RADIUS) > 6){
            if (ageFromState(worldIn.getBlockState(pos)) == 0 &&
                    RANDOM.nextInt(nothingToEmptyChance * 8 / worldIn.getBlockState(pos).getValue(RADIUS)) == 0 &&
                    ageFromState(worldIn.getBlockState(pos.up())) <= 0 &&
                    ageFromState(worldIn.getBlockState(pos.down())) <= 0){

                worldIn.setBlockState(pos, stateFromAge(1).withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));

            } else if (ageFromState(worldIn.getBlockState(pos)) == 1 &&
                    RANDOM.nextInt(emptyToFilledChance * 8 / worldIn.getBlockState(pos).getValue(RADIUS)) == 0){

                worldIn.setBlockState(pos, stateFromAge(2).withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));

            }
       }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            ItemStack handStack = playerIn.getHeldItemMainhand();
            ItemStack resin = proxyIC2.getIC2ResinStack();
            int operationEnergyCost = 50;
            int age = ageFromState(state);
            if (age > 0) {
                boolean doTap = false;
                if (handStack.getItem() == proxyIC2.getIC2TreeTap(true) && proxyIC2.IC2CanUseElectricItem(handStack, operationEnergyCost)) {
                    proxyIC2.IC2UseElectricItem(handStack, operationEnergyCost, playerIn);
                    doTap = true;
                } else if (handStack.getItem() == proxyIC2.getIC2TreeTap(false)) {
                    handStack.damageItem(1, playerIn);
                    doTap = true;
                }
                if (doTap) {
                    proxyIC2.IC2TapPlaySound(playerIn);
                    if (!worldIn.isRemote){
                        if (age == 2 || (age == 1 && worldIn.rand.nextInt(5) == 0)){
                            worldIn.setBlockState(pos, stateFromAge(age - 1).withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)), 3);
                            resin.setCount((age == 2) ? (1 + RANDOM.nextInt(3)) : (RANDOM.nextInt(2)));
                            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, resin));
                        }
                    }
                }
                return doTap;
            }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
