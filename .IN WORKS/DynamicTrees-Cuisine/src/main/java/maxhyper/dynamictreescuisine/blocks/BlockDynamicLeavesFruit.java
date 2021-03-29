package maxhyper.dynamictreescuisine.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import maxhyper.dynamictreescuisine.DynamicTreesCuisine;
import maxhyper.dynamictreescuisine.ModConfigs;
import maxhyper.dynamictreescuisine.trees.TreeCitrus;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import snownee.cuisine.CuisineRegistry;
import snownee.cuisine.items.ItemBasicFood;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author Harley O'Connor
 */
public final class BlockDynamicLeavesFruit extends BlockDynamicLeaves {

    private TreeCitrus.citrusType type;

    public BlockDynamicLeavesFruit(final String registryName, TreeCitrus.citrusType type) {
        this.setRegistryName(DynamicTreesCuisine.MODID, registryName);
        this.setUnlocalizedName(registryName);
        this.type = type;
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

    private ItemStack getFruitStack (Random rand){
        return TreeCitrus.getFruitWithRarity(new ItemStack(CuisineRegistry.BASIC_FOOD, 1, type.fruitItemMeta), rand);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess access, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = super.getDrops(access, pos, state, fortune);
        int fruitAge = state.getValue(BlockDynamicLeaves.TREE);
        if (fruitAge == 3){
            drops.add(getFruitStack(new Random()));
        }
        return drops;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (ModConfigs.fruityLeaves && ModConfigs.boneMealLeaves){
            ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);
            if (state.getValue(TREE) == 3){
                    ItemHandlerHelper.giveItemToPlayer(player, getFruitStack(world.rand));
                    world.setBlockState(pos, state.withProperty(TREE, 1));
                    return true;
            } else if (mainHand.getItem() == Items.DYE && mainHand.getMetadata() == 15 && hand == EnumHand.MAIN_HAND) {
                useBoneMeal(world, pos, state, mainHand, player);
                return true;
            } else if (offHand.getItem() == Items.DYE && offHand.getMetadata() == 15 && hand == EnumHand.OFF_HAND){
                useBoneMeal(world, pos, state, offHand, player);
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
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

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean unknown) {
        if(!((entityIn instanceof EntityItem && ((EntityItem) entityIn).getItem().getItem() instanceof ItemBasicFood) || entityIn instanceof EntityBat)) {
            //Let cuisine fruits fall through the canopy
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, unknown);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

        if (!worldIn.isRemote && state.getBlock() == this && blockIn == Blocks.AIR && fromPos.equals(pos.up()) && worldIn.getBlockState(fromPos).getBlock() == Blocks.FIRE)
        {
            worldIn.setBlockToAir(fromPos);
            boolean flag = false;
            for (Entity entity : worldIn.weatherEffects)
            {
                if (entity instanceof EntityLightningBolt && entity.getPosition().equals(fromPos))
                {
                    flag = true;
                }
            }
            if (!flag)
            {
                return;
            }

            for (BlockPos pos2 : BlockPos.getAllInBoxMutable(pos.getX() - 6, pos.getY() - 8, pos.getZ() - 6, pos.getX() + 6, pos.getY() + 3, pos.getZ() + 6))
            {
                IBlockState state2 = worldIn.getBlockState(pos2);
                boolean citronFound = false;
                if (state2.getBlock() == this && state2.getValue(TREE) == 3){
                    worldIn.setBlockState(pos2, state.withProperty(TREE, 1));
                    citronFound = true;
                }
                if (state2.getBlock() == TreeCitrus.citrusType.CITRON.fruitBlock){
                    worldIn.setBlockToAir(pos2);
                    citronFound = true;
                }
                if (citronFound) {
                    if (worldIn.getGameRules().getBoolean("doTileDrops") && !worldIn.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
                    {
                        //Empowered Citron
                        ItemStack stack = TreeCitrus.getFruitWithRarity(new ItemStack(CuisineRegistry.BASIC_FOOD, 1, 16), worldIn.rand);
                        if (captureDrops.get())
                        {
                            capturedDrops.get().add(stack);
                            continue;
                        }
                        double d0 = worldIn.rand.nextFloat() * 0.5F + 0.25D;
                        double d1 = worldIn.rand.nextFloat() * 0.5F + 0.25D;
                        double d2 = worldIn.rand.nextFloat() * 0.5F + 0.25D;
                        EntityItem entityitem = new EntityItem(worldIn, pos2.getX() + d0, pos2.getY() + d1, pos2.getZ() + d2, stack);
                        entityitem.setDefaultPickupDelay();
                        entityitem.setEntityInvulnerable(true);
                        worldIn.spawnEntity(entityitem);
                        EntityBat bat = new EntityBat(worldIn);
                        bat.setPosition(pos2.getX() + d0, pos2.getY() + d1, pos2.getZ() + d2);
                        bat.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 10));
                        bat.setCustomNameTag("ForestBat");
                        bat.setAlwaysRenderNameTag(true);
                        worldIn.spawnEntity(bat);
                    }
                }
            }
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }
}
