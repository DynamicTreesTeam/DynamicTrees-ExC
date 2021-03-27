package maxhyper.dynamictreescuisine.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreescuisine.DynamicTreesCuisine;
import maxhyper.dynamictreescuisine.ModConfigs;
import maxhyper.dynamictreescuisine.trees.TreeCitrus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

/**
 * @author Harley O'Connor
 */
public final class BlockDynamicLeavesFruit extends BlockDynamicLeaves {

    private TreeCitrus.citrusType type;

    public BlockDynamicLeavesFruit(final String registryName, TreeCitrus.citrusType type) {
        this.setRegistryName(DynamicTreesCuisine.MODID, registryName);
        this.setUnlocalizedName(registryName);
        this.type = type;
        needsRandomTick = ModConfigs.fruityLeaves;
    }

    public static void addEntityBiodustFX(World world, double x, double y, double z, int ammount) {
        for (int i=0; i<ammount;i++) {
            ParticleManager effectRenderer = Minecraft.getMinecraft().effectRenderer;
            Particle particle = effectRenderer.spawnEffectParticle(EnumParticleTypes.VILLAGER_HAPPY.ordinal(), x + world.rand.nextFloat(), y + world.rand.nextFloat(), z + world.rand.nextFloat(), 0, 0, 0);
            if (particle != null) {
                effectRenderer.addEffect(particle);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess access, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = super.getDrops(access, pos, state, fortune);
        int fruitAge = state.getValue(BlockDynamicLeaves.TREE);
        if (fruitAge == 3){
            drops.add(type.getPrimitiveFruitItem());
        }
        return drops;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote){
            ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);
            if (state.getValue(TREE) == 3){
                if (mainHand.isEmpty() && offHand.isEmpty()) {
                    ItemStack fruit = type.getPrimitiveFruitItem();
                    ItemHandlerHelper.giveItemToPlayer(player, fruit);
                    world.setBlockState(pos, state.withProperty(TREE, 0));
                    return true;
                }
            } else if (mainHand.getItem() == Items.DYE && mainHand.getMetadata() == 15 && hand == EnumHand.MAIN_HAND) {
                useBoneMeal(world, pos, state, mainHand, player);
                return true;
            } else if (offHand.getItem() == Items.DYE && offHand.getMetadata() == 15 && hand == EnumHand.OFF_HAND){
                useBoneMeal(world, pos, state, offHand, player);
                return true;
            }
        }
        return false;
    }

    private void useBoneMeal (World world, BlockPos pos, IBlockState state, ItemStack handStack, EntityPlayer player) {
        addEntityBiodustFX(world, pos.getX(), pos.getY(), pos.getZ(), 4);
        int grow = 1;
        world.setBlockState(pos, state.withProperty(TREE, state.getValue(TREE)+ grow ));
        if (!player.isCreative()){
            handStack.shrink(1);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED; // fruit overlays require CUTOUT_MIPPED, even in Fast graphics
    }

}
