package maxhyper.dynamictreesforestry.blocks;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.trees.Species;
import forestry.core.network.packets.PacketFXSignal;
import forestry.core.utils.NetworkUtil;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConfigs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;

public class BlockDynamicLeavesFruit extends BlockDynamicLeaves {

    private static final Map<fruitTypes, int[]> fruitColors = new HashMap<fruitTypes, int[]>(){{
        put(fruitTypes.APPLE, new int[]{ 0xE3F49C, 0xECB277, 0xF67053, 0xFF2E2E});
        put(fruitTypes.WALNUT, new int[]{ 0xC4D24A, 0xD6C249, 0xE9B249, 0xFBA248});
        put(fruitTypes.CHESTNUT, new int[]{ 0xC4D24A, 0xAD9D46, 0x966841, 0x7F333D});
        put(fruitTypes.CHERRY, new int[]{ 0xC4D24A, 0xD89B41, 0xEB6537, 0xFF2E2E});
        put(fruitTypes.LEMON, new int[]{ 0x99FF00, 0xB5F900, 0xD2F400, 0xEEEE00});
        put(fruitTypes.PLUM, new int[]{ 0xEEFF1A, 0xC1BB29, 0x937837, 0x663446});
    }};
    private static final Map<fruitTypes, ItemStack> fruitDrops = new HashMap<fruitTypes, ItemStack>(){{
        put(fruitTypes.APPLE, new ItemStack(Items.APPLE));
        put(fruitTypes.WALNUT, new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry", "fruits"))), 1, 1));
        put(fruitTypes.CHESTNUT, new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry", "fruits"))), 1, 2));
        put(fruitTypes.CHERRY, new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry", "fruits"))), 1, 0));
        put(fruitTypes.LEMON, new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry", "fruits"))), 1, 3));
        put(fruitTypes.PLUM, new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry", "fruits"))), 1, 4));
    }};

    public enum fruitTypes {
        APPLE, WALNUT, CHESTNUT, CHERRY, LEMON, PLUM
    }

    private fruitTypes leafFruitType;

    private Species species;

    public BlockDynamicLeavesFruit setSpecies(Species species) {
        this.species = species;
        return this;
    }

    public BlockDynamicLeavesFruit (String name, fruitTypes fruitType){
        setRegistryName(DynamicTreesForestry.MODID, name);
        setUnlocalizedName(name);
        leafFruitType = fruitType;
    }

    public void setProperties(ILeavesProperties properties) {
        this.setProperties(0, properties);
        this.setProperties(1, properties);
        this.setProperties(2, properties);
        this.setProperties(3, properties);
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
        if (fruitAge == 3 || (fruitAge == 2 && fortune > 0)){
            drops.add(fruitDrops.get(leafFruitType));
        }
        return drops;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (ModConfigs.fruityLeaves){
            ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
            ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);
            if (state.getValue(TREE) == 3){
                PacketFXSignal packet = new PacketFXSignal(PacketFXSignal.VisualFXType.BLOCK_BREAK, PacketFXSignal.SoundFXType.BLOCK_BREAK, pos, state);
                NetworkUtil.sendNetworkPacket(packet, pos, world);
                ItemStack fruit = fruitDrops.get(leafFruitType);
                ItemHandlerHelper.giveItemToPlayer(player, fruit);
                world.setBlockState(pos, state.withProperty(TREE, 0));
                return true;
            } else if (ModConfigs.boneMealLeaves){
                if (mainHand.getItem() == Items.DYE && mainHand.getMetadata() == 15 && hand == EnumHand.MAIN_HAND) {
                    useBoneMeal(world, pos, state, mainHand, player);
                    return true;
                } else if (offHand.getItem() == Items.DYE && offHand.getMetadata() == 15 && hand == EnumHand.OFF_HAND){
                    useBoneMeal(world, pos, state, offHand, player);
                    return true;
                }
            }
        }
        return false;
    }

    private void useBoneMeal (World world, BlockPos pos, IBlockState state, ItemStack handStack, EntityPlayer player) {
        addEntityBiodustFX(world, pos.getX(), pos.getY(), pos.getZ(), 4);
        int grow = 1;// + ((state.getValue(TREE) < 2) ? world.rand.nextInt(2) : 0);
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

    @SideOnly(Side.CLIENT)
    public int fruitColor(IBlockState state) {
        return fruitColors.get(leafFruitType)[state.getValue(TREE)];
    }
}
