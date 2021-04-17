package maxhyper.dynamictreestbl.blocks;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class BlockBranchSap extends BlockBranchBasic {

    public BlockBranchSap(String name) {
        super(name);
    }

    @Override
    public void futureBreak(IBlockState state, World world, BlockPos cutPos, EntityLivingBase entity) {

        //Try to get the face being pounded on
        final double reachDistance = entity instanceof EntityPlayerMP ? entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() : 5.0D;
        RayTraceResult rtResult = playerRayTrace(entity, reachDistance, 1.0F);
        EnumFacing toolDir = rtResult != null ? (entity.isSneaking() ? rtResult.sideHit.getOpposite() : rtResult.sideHit) : EnumFacing.DOWN;

        if(toolDir == null) {//Some rayTracing results can theoretically produce a face hit with no side designation.
            toolDir = EnumFacing.DOWN;//Make everything better
        }

        //Do the actual destruction
        BranchDestructionData destroyData = destroyBranchFromNode(world, cutPos, toolDir, false);

        //Get all of the wood drops
        ItemStack heldItem = entity.getHeldItemMainhand();
        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem)!=0;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
        float fortuneFactor = 1.0f + 0.25f * fortune;
        float woodVolume = destroyData.woodVolume;// The amount of wood calculated from the body of the tree network
        List<ItemStack> woodItems;
        if (silkTouch)
            woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor);
        else
            woodItems = getSapDrops(woodVolume * fortuneFactor);

        float chance = 1.0f;
        //Fire the block harvesting event.  For An-Sar's PrimalCore mod :)
        if (entity instanceof EntityPlayer)
        {
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(woodItems, world, cutPos, state, fortune, chance, false, (EntityPlayer) entity);
        }
        final float finalChance = chance;

        //Build the final wood drop list taking chance into consideration
        List<ItemStack> woodDropList = woodItems.stream().filter(i -> world.rand.nextFloat() <= finalChance).collect(Collectors.toList());

        //This will drop the EntityFallingTree into the world
        EntityFallingTree.dropTree(world, destroyData, woodDropList, EntityFallingTree.DestroyType.HARVEST);

        //Damage the axe by a prescribed amount
        damageAxe(entity, heldItem, getRadius(state), woodVolume);
    }

    public List<ItemStack> getSapDrops(float volume) {
        List<ItemStack> ret = new java.util.ArrayList<>();
        ItemStack resin = new ItemStack(ItemRegistry.SAP_BALL);
        resin.setCount((int) (volume * ModConfigs.treeHarvestMultiplier * maxhyper.dynamictreestbl.ModConfigs.sapDropMultiplier));
        ret.add(resin);
        return ret;
    }

    public List<ItemStack> getLogDrops(World world, BlockPos pos, Species species, float volume) {
        List<ItemStack> ret = new java.util.ArrayList<>();//A list for storing all the dead tree guts
        volume *= ModConfigs.treeHarvestMultiplier;// For cheaters.. you know who you are.
        return species.getLogsDrops(world, pos, ret, volume);
    }

}
