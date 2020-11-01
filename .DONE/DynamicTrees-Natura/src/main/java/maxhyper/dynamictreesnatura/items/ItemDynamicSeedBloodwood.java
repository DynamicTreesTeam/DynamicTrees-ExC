package maxhyper.dynamictreesnatura.items;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.mantle.util.LocUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDynamicSeedBloodwood extends Seed {
    public ItemDynamicSeedBloodwood() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"bloodwoodseed").toString());
    }
    @Override
    public boolean doPlanting(World world, BlockPos pos, EntityPlayer planter, ItemStack seedStack) {
        Species species = getSpecies(seedStack);
        if(species.plantSapling(world, pos)) {//Do the planting
            String joCode = getCode(seedStack);
            if(!joCode.isEmpty()) {
                world.setBlockToAir(pos);//Remove the newly created dynamic sapling
                species.getJoCode(joCode).setCareful(true).generate(world, species, pos.up(), world.getBiome(pos), planter != null ? planter.getHorizontalFacing() : EnumFacing.NORTH, 8, SafeChunkBounds.ANY);
            }
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() + LocUtils.translateRecursive(LocUtils.translateRecursive("tile.natura.nether_sapling2.bloodwood.tooltip"))));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override public EnumActionResult onItemUsePlantSeed(EntityPlayer player, World world, BlockPos pos, EnumHand hand, ItemStack seedStack, EnumFacing facing, float hitX, float hitY, float hitZ) {

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if(block.isReplaceable(world, pos)) {
            pos = pos.up();
            facing = EnumFacing.DOWN;
        }

        if (facing == EnumFacing.DOWN) {//Ensure this seed is only used on the bottom side of a block
            if (player.canPlayerEdit(pos, facing, seedStack) && player.canPlayerEdit(pos.down(), facing, seedStack)) {//Ensure permissions to edit block
                if(doPlanting(world, pos.down(), player, seedStack)) {
                    seedStack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    @Nullable
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        EntityItemBloodwoodSeed bloodwoodSeedEntity = new EntityItemBloodwoodSeed(world, location.posX, location.posY, location.posZ, itemstack);

        //We need to also copy the motion of the replaced entity or it acts funny when the item spawns.
        bloodwoodSeedEntity.motionX = location.motionX;
        bloodwoodSeedEntity.motionY = location.motionY;
        bloodwoodSeedEntity.motionZ = location.motionZ;

        return bloodwoodSeedEntity;
    }

    public static class EntityItemBloodwoodSeed extends EntityItem {

        public EntityItemBloodwoodSeed(World worldIn) {
            super(worldIn);
        }

        public EntityItemBloodwoodSeed(World worldIn, double x, double y, double z, ItemStack stack) {
            super(worldIn, x, y, z, stack);
            this.setDefaultPickupDelay();
        }

        public int seedsPlantedPerStack = 16;
        public float chanceToPlant = ModConfigs.seedPlantRate;

        @Override
        public void onUpdate() {
            motionY += 0.041f;
            IBlockState thisState = world.getBlockState(getPosition());
            Species thisSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesNatura.MODID, "bloodwood"));

//        BlockPos slightlyUpPos = new BlockPos(posX, posY + ceilingOffset, posZ);
//        if (world.getBlockState(slightlyUpPos).isSideSolid(world, slightlyUpPos, EnumFacing.DOWN)){
//            motionY = 0;
//            posY = Math.round(posY) - ceilingOffset;
//        }

            if (getItem().getCount() > 0 && !isDead && ticksExisted % (ModConfigs.seedTimeToLive/seedsPlantedPerStack) == 0 && thisSpecies.isAcceptableSoil(world, getPosition(), thisState) && world.isAirBlock(getPosition().down())){
                boolean FAIL = false;
                for (EnumFacing dir : EnumFacing.HORIZONTALS){
                    if (world.getBlockState(getPosition().offset(dir)).getBlock() instanceof BlockRooty){
                        FAIL = true;
                    }
                }
                if (!FAIL && world.rand.nextFloat() <= chanceToPlant){
                    thisSpecies.plantSapling(world,getPosition().down());
                    //if the stack has more than one item, make it jump in a random direction
                    if (getItem().getCount() > 1){
                        world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, 0,0,0);
                        world.playSound(posX, posY, posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS,1, 1, false);
                        motionY -= 0.2f;
                        motionX += world.rand.nextFloat()/2 - 0.25f;
                        motionY += world.rand.nextFloat()/2 - 0.25f;
                    }
                }
                getItem().shrink(1);
                ticksExisted = 0;
            }
            super.onUpdate();
        }

        @Override
        public void setDefaultPickupDelay() {
            this.setPickupDelay(50);
        }

    }


}
