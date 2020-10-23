package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchThick;
import com.ferreusveritas.dynamictrees.cells.CellMetadata;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeDestroyer;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeExtState;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeNetVolume;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeSpecies;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockDynamicBranchBloodwood extends BlockBranchThick {

    public BlockDynamicBranchBloodwood() {
        super(new ResourceLocation(DynamicTreesNatura.MODID,"bloodwoodbranch").toString());
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 2f * (radius * radius) / 64.0f * 8.0f;
    };

    @Override public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }
    @Override public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

    @Override public int getMaxRadius() {
        return 16;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return false;
    }

    @Override public BranchDestructionData destroyBranchFromNode(World world, BlockPos cutPos, EnumFacing toolDir, boolean wholeTree) {

        IBlockState blockState = world.getBlockState(cutPos);
        NodeSpecies nodeSpecies = new NodeSpecies();
        MapSignal signal = analyse(blockState, world, cutPos, null, new MapSignal(nodeSpecies));// Analyze entire tree network to find root node and species
        Species species = nodeSpecies.getSpecies();//Get the species from the root node

        // Analyze only part of the tree beyond the break point and map out the extended block states
        // We can't destroy the branches during this step since we need accurate extended block states that include connections
        NodeExtState extStateMapper = new NodeExtState(cutPos);
        analyse(blockState, world, cutPos, wholeTree ? null : signal.localRootDir, new MapSignal(extStateMapper));

        // Analyze only part of the tree beyond the break point and calculate it's volume, then destroy the branches
        NodeNetVolume volumeSum = new NodeNetVolume();
        NodeDestroyer destroyer = new NodeDestroyer(species);
        destroyMode = EnumDestroyMode.HARVEST;
        analyse(blockState, world, cutPos, wholeTree ? null : signal.localRootDir, new MapSignal(volumeSum, destroyer));
        destroyMode = EnumDestroyMode.SLOPPY;

        //Destroy all the leaves on the branch, store them in a map and convert endpoint coordinates from absolute to relative
        List<BlockPos> endPoints = destroyer.getEnds();
        Map<BlockPos, IBlockState> destroyedLeaves = new HashMap<>();
        List<BlockItemStack> leavesDropsList = new ArrayList<>();
        destroyLeaves(world, cutPos, species, endPoints, destroyedLeaves, leavesDropsList);
        endPoints = endPoints.stream().map(p -> p.subtract(cutPos)).collect(Collectors.toList());

        //Calculate main trunk height
        int trunkHeight = 1;
        for(BlockPos iter = new BlockPos(0, 1, 0); extStateMapper.getExtStateMap().containsKey(iter); iter = iter.down()) {
            trunkHeight++;
        }

        EnumFacing cutDir = signal.localRootDir;
        if(cutDir == null) {
            cutDir = EnumFacing.UP;
        }

        return new BranchDestructionData(species, extStateMapper.getExtStateMap(), destroyedLeaves, leavesDropsList, endPoints, volumeSum.getVolume(), cutPos, cutDir, toolDir, trunkHeight);
    }
    @Override public void futureBreak(IBlockState state, World world, BlockPos cutPos, EntityLivingBase entity) {

        //Try to get the face being pounded on
        final double reachDistance = entity instanceof EntityPlayerMP ? entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() : 5.0D;
        RayTraceResult rtResult = playerRayTrace(entity, reachDistance, 1.0F);
        EnumFacing toolDir = rtResult != null ? (entity.isSneaking() ? rtResult.sideHit.getOpposite() : rtResult.sideHit) : EnumFacing.UP;

        //Do the actual destruction
        BranchDestructionData destroyData = destroyBranchFromNode(world, cutPos, toolDir, false);

        //Get all of the wood drops
        ItemStack heldItem = entity.getHeldItemMainhand();
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
        float fortuneFactor = 1.0f + 0.25f * fortune;
        float woodVolume = destroyData.woodVolume;// The amount of wood calculated from the body of the tree network
        List<ItemStack> woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor);

        if(entity.getActiveHand() == null) {//What the hell man? I trusted you!
            entity.setActiveHand(EnumHand.MAIN_HAND);//Players do things with hands.
        }

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
    @Override protected void sloppyBreak(World world, BlockPos cutPos, EntityFallingTree.DestroyType destroyType) {
        //Do the actual destruction
        BranchDestructionData destroyData = destroyBranchFromNode(world, cutPos, EnumFacing.UP, false);

        //Get all of the wood drops
        List<ItemStack> woodDropList = getLogDrops(world, cutPos, destroyData.species, destroyData.woodVolume);

        //This will drop the EntityFallingTree into the world
        EntityFallingTree.dropTree(world, destroyData, woodDropList, destroyType);
    }

    @Override
    protected int getSideConnectionRadius(IBlockAccess blockAccess, BlockPos pos, int radius, EnumFacing side) {
        BlockPos deltaPos = pos.offset(side);
        IBlockState blockState = blockAccess.getBlockState(deltaPos);

        int connectionRadius = TreeHelper.getTreePart(blockState).getRadiusForConnection(blockState, blockAccess, deltaPos, this, side, radius);

        if (radius > 8) {
            if (side == EnumFacing.UP) {
                return connectionRadius >= radius ? 1 : 0;
            } else if (side == EnumFacing.DOWN) {
                return connectionRadius >= radius ? 2 : connectionRadius > 0 ? 1 : 0;
            }
        }

        return Math.min(RADMAX_NORMAL, connectionRadius);
    }

    @Override
    public GrowSignal growSignal(World world, BlockPos pos, GrowSignal signal) {

        if (signal.dir == EnumFacing.UP) {
            signal.dir = EnumFacing.DOWN;
            signal.numTurns = 0;
        }
        return super.growSignal(world, pos, signal);
    }

}