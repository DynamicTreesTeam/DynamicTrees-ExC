package maxhyper.dynamictreesextrautils2.blocks;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeDestroyer;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeExtState;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeNetVolume;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeSpecies;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesextrautils2.DynamicTreesExtraUtils2;
import maxhyper.dynamictreesextrautils2.ModContent;
import maxhyper.dynamictreesextrautils2.nodes.BranchDestructionDataExtra;
import maxhyper.dynamictreesextrautils2.nodes.NodeNetSpecialBranchVolume;
import maxhyper.dynamictreesextrautils2.trees.TreeFeJuniper;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class BlockBranchFeJuniper extends BlockBranchBasic {

    public BlockBranchFeJuniper() {
        super(new ResourceLocation(DynamicTreesExtraUtils2.MODID,"ferrousjuniperbranch").toString());
        setTickRandomly(true);
    }
    public BlockBranchFeJuniper(boolean burnt) {
        super(new ResourceLocation(DynamicTreesExtraUtils2.MODID,"ferrousjuniperbranchburnt").toString());
        setTickRandomly(false);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
        int radius = getRadius(blockState);
        return 0.5f * (radius * radius) / 64.0f * 8.0f;
    };

    @Override public boolean isFireSource(World world, BlockPos pos, EnumFacing direction) {
        return world.getBlockState(pos).getBlock() != getBlockFromName(new ResourceLocation(DynamicTreesExtraUtils2.MODID,"ferrousjuniperbranchburnt").toString());
    }
    @Override public boolean isFlammable(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        return world.getBlockState(pos).getBlock() != getBlockFromName(new ResourceLocation(DynamicTreesExtraUtils2.MODID,"ferrousjuniperbranchburnt").toString());
    }
    @Override public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 15;
    }
    @Override public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 0;
    }

//protected static final PropertyBool BURNT = PropertyBool.create("burnt");
//    @Override
//    protected BlockStateContainer createBlockState() {
//        IProperty[] listedProperties = { BURNT, RADIUS };
//        return new ExtendedBlockState(this, listedProperties, CONNECTIONS);
//    }

    @Override public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        performUpdate(worldIn, pos, state, rand);
    }
    private void performUpdate(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getBlock() == ModContent.fejuniperBranchRaw) {
            for (EnumFacing facing : EnumFacing.values()) {
                IBlockState fireState = worldIn.getBlockState(pos.offset(facing));
                if (fireState.getBlock() == Blocks.FIRE
                        || fireState.getMaterial() == Material.FIRE
                        || (fireState.getBlock() == ModContent.fejuniperBranchBurnt)
                ) {
                    for (EnumFacing enumFacing : EnumFacing.values()) {
                        BlockPos offset = pos.offset(enumFacing);
                        IBlockState blockState = worldIn.getBlockState(offset);
                        if (blockState.getBlock() == Blocks.FIRE || blockState.getBlock().isAir(blockState, worldIn, offset)) {
                            worldIn.setBlockState(offset, Blocks.FIRE.getDefaultState().withProperty(BlockFire.AGE, 0), 3);
                        } else if (blockState.getBlock() == ModContent.fejuniperBranchRaw || blockState.getBlock() == ModContent.fejuniperBranchBurnt) {
                            for (EnumFacing enumFacing2 : EnumFacing.values()) {
                                if (rand.nextBoolean()) continue;
                                BlockPos offset2 = pos.offset(enumFacing2);
                                IBlockState blockState2 = worldIn.getBlockState(offset2);
                                if (blockState2.getBlock() == Blocks.FIRE || blockState2.getBlock().isAir(blockState2, worldIn, offset2) || blockState2.getBlock().isReplaceable(worldIn, offset2) ) {
                                    worldIn.setBlockState(offset2, Blocks.FIRE.getDefaultState().withProperty(BlockFire.AGE, 0), 3);
                                }
                            }
                        }
                    }
                    worldIn.setBlockState(pos, ModContent.fejuniperBranchBurnt.getDefaultState().withProperty(RADIUS, worldIn.getBlockState(pos).getValue(RADIUS)));

                    for (EnumFacing enumFacing : EnumFacing.values()) {
                        if (rand.nextBoolean()) continue;
                        BlockPos offset = pos.offset(enumFacing);
                        IBlockState offsetBlockState = worldIn.getBlockState(offset);
                        if (offsetBlockState.getBlock() == ModContent.fejuniperBranchRaw) {
                            worldIn.setBlockState(offset, ModContent.fejuniperBranchBurnt.getDefaultState().withProperty(RADIUS, worldIn.getBlockState(offset).getValue(RADIUS)));
                        }
                    }
                    return;
                }
            }
        }
    }

    @Override public void futureBreak(IBlockState state, World world, BlockPos cutPos, EntityLivingBase entity) {

        //Try to get the face being pounded on
        final double reachDistance = entity instanceof EntityPlayerMP ? entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() : 5.0D;
        RayTraceResult rtResult = playerRayTrace(entity, reachDistance, 1.0F);
        EnumFacing toolDir = rtResult != null ? (entity.isSneaking() ? rtResult.sideHit.getOpposite() : rtResult.sideHit) : EnumFacing.DOWN;

        //Do the actual destruction
        BranchDestructionDataExtra destroyData = destroyBranchFromNode(world, cutPos, toolDir, false);

        //Get all of the wood drops
        ItemStack heldItem = entity.getHeldItemMainhand();
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, heldItem);
        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) != 0;
        float fortuneFactor = 1.0f + 0.25f * fortune;
        float woodVolume = destroyData.woodVolume;// The amount of wood calculated from the body of the tree network
        float burntWoodVolume = destroyData.secondaryWoodVolume;
        List<ItemStack> woodItems;
        if (silkTouch){
            woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor, burntWoodVolume,true);
        } else {
            woodItems = getLogDrops(world, cutPos, destroyData.species, woodVolume * fortuneFactor, burntWoodVolume, false);
        }

        entity.getActiveHand();

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
        BranchDestructionDataExtra destroyData = destroyBranchFromNode(world, cutPos, EnumFacing.DOWN, false);
        float burntWoodVolume = 0;

        //Get all of the wood drops
        List<ItemStack> woodDropList = getLogDrops(world, cutPos, destroyData.species, destroyData.woodVolume, destroyData.secondaryWoodVolume, false);

        //This will drop the EntityFallingTree into the world
        EntityFallingTree.dropTree(world, destroyData, woodDropList, destroyType);
    }
    public static List<ItemStack> logDrops (float volume, float burntVolume, boolean silkTouch){
        List<ItemStack> ret = new java.util.ArrayList<>();//A list for storing all the dead tree guts

        ret.add(new ItemStack(TreeFeJuniper.logBlock, (int) burntVolume, 1)); //drops burnt logs if there some parts of the tree are burnt
        burntVolume = (int) burntVolume;
        float rawVolume = volume - burntVolume;
        if (silkTouch){
            ret.add(new ItemStack(TreeFeJuniper.logBlock, (int) rawVolume, 0)); //drops logs with silktouch
        } else {
            ret.add(new ItemStack(TreeFeJuniper.planksBlock, (int) rawVolume, 1)); //drops planks without silktouch
        }

        ret.add(new ItemStack(Items.STICK, (int) ((volume - ((int) rawVolume + (int) burntVolume)) * 8)));
        return ret;
    }

    public List<ItemStack> getLogDrops(World world, BlockPos pos, Species species, float volume, float burntVolume, boolean silkTouch) {
        List<ItemStack> drops = logDrops(volume, burntVolume, silkTouch);
        volume *= ModConfigs.treeHarvestMultiplier;// For cheaters.. you know who you are.
        return species.getLogsDrops(world, pos, drops, volume);
    }

    @Override public BranchDestructionDataExtra destroyBranchFromNode(World world, BlockPos cutPos, EnumFacing toolDir, boolean wholeTree) {

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
        NodeNetSpecialBranchVolume volumeBurntSum = new NodeNetSpecialBranchVolume();
        NodeDestroyer destroyer = new NodeDestroyer(species);
        destroyMode = EnumDestroyMode.HARVEST;
        analyse(blockState, world, cutPos, wholeTree ? null : signal.localRootDir, new MapSignal(volumeSum, volumeBurntSum, destroyer));
        destroyMode = EnumDestroyMode.SLOPPY;

        //Destroy all the leaves on the branch, store them in a map and convert endpoint coordinates from absolute to relative
        List<BlockPos> endPoints = destroyer.getEnds();
        Map<BlockPos, IBlockState> destroyedLeaves = new HashMap<>();
        List<BlockItemStack> leavesDropsList = new ArrayList<>();
        destroyLeaves(world, cutPos, species, endPoints, destroyedLeaves, leavesDropsList);
        endPoints = endPoints.stream().map(p -> p.subtract(cutPos)).collect(Collectors.toList());

        //Calculate main trunk height
        int trunkHeight = 1;
        for(BlockPos iter = new BlockPos(0, 1, 0); extStateMapper.getExtStateMap().containsKey(iter); iter = iter.up()) {
            trunkHeight++;
        }

        EnumFacing cutDir = signal.localRootDir;
        if(cutDir == null) {
            cutDir = EnumFacing.DOWN;
        }

        return new BranchDestructionDataExtra(species, extStateMapper.getExtStateMap(), destroyedLeaves, leavesDropsList, endPoints, volumeSum.getVolume(), volumeBurntSum.getVolume(), cutPos, cutDir, toolDir, trunkHeight);
    }
}
