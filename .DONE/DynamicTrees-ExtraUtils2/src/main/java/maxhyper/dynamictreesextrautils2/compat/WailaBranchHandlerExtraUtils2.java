package maxhyper.dynamictreesextrautils2.compat;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockTrunkShell;
import com.ferreusveritas.dynamictrees.compat.WailaBranchHandler;
import com.ferreusveritas.dynamictrees.compat.WailaOther;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeNetVolume;
import com.ferreusveritas.dynamictrees.trees.Species;
import javafx.util.Pair;
import maxhyper.dynamictreesextrautils2.blocks.BlockBranchFeJuniper;
import maxhyper.dynamictreesextrautils2.nodes.NodeNetSpecialBranchVolume;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class WailaBranchHandlerExtraUtils2 extends WailaBranchHandler {

    private BlockPos lastPos = BlockPos.ORIGIN;
    private Species lastSpecies = Species.NULLSPECIES;
    private float lastVolume = 0;
    private float lastBurntVolume = 0;

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        if(WailaOther.invalid) {
            lastPos = BlockPos.ORIGIN;
            lastSpecies = Species.NULLSPECIES;
            lastVolume = 0;
            lastBurntVolume = 0;
        }

        NBTTagCompound nbtData = accessor.getNBTData();
        BlockPos pos = accessor.getPosition();
        Species species = Species.NULLSPECIES;

        //Attempt to get species from server via NBT data
        if(nbtData.hasKey("species")) {
            species = TreeRegistry.findSpecies(new ResourceLocation(nbtData.getString("species")));
        }
        //Attempt to get species by checking if we're still looking at the same block
        if(species == Species.NULLSPECIES && lastPos.equals(pos)) {
            species = lastSpecies;
        }
        //Attempt to get species from the world as a last resort as the operation can be rather expensive
        if(species == Species.NULLSPECIES) {
            species = getWailaSpecies(accessor.getWorld(), pos);
        }

        if(!lastPos.equals(pos)) {
            getTreeVolume(accessor.getWorld(), pos);
        }

        //Update the cached species and position
        lastSpecies = species;
        lastPos = pos;

        if(species != Species.NULLSPECIES) {

            if(species != species.getFamily().getCommonSpecies()) {
                tooltip.add("Species: " + species.getLocalizedName());
            }

            if(Minecraft.getMinecraft().gameSettings.advancedItemTooltips) {
                tooltip.add(TextFormatting.DARK_GRAY + species.getRegistryName().toString());
            }

            StringBuilder renderString = new StringBuilder();

            ItemStack seedStack = species.getSeedStack(1);
            String seedName = seedStack.getItem().getRegistryName().toString();
            renderString.append(SpecialChars.getRenderString("waila.stack", "1", seedName, String.valueOf(1), String.valueOf(seedStack.getItemDamage())));

            if(lastVolume > 0) {
                boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, accessor.getPlayer().getHeldItemMainhand()) == 1;

                for (ItemStack drop : BlockBranchFeJuniper.logDrops(lastVolume, lastBurntVolume, silkTouch)){
                    if (!drop.isEmpty()){
                        String name = drop.getItem().getRegistryName().toString();
                        renderString.append(SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(drop.getCount()), String.valueOf(drop.getItemDamage())));
                    }
                }

            }
            tooltip.add(renderString.toString());
        }

        return tooltip;
    }

    private void getTreeVolume(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        //Dereference proxy trunk shell block
        if(block instanceof BlockTrunkShell) {
            BlockTrunkShell.ShellMuse muse = ((BlockTrunkShell)block).getMuse(world, pos);
            if(muse != null) {
                state = muse.state;
                block = state.getBlock();
                pos = muse.pos;
            }
        }

        if(block instanceof BlockBranch) {
            BlockBranch branch = (BlockBranch) block;

            // Analyze only part of the tree beyond the break point and calculate it's volume, then destroy the branches
            NodeNetVolume volumeSum = new NodeNetVolume();
            NodeNetSpecialBranchVolume volumeBurntSum = new NodeNetSpecialBranchVolume();
            branch.analyse(state, world, pos, null, new MapSignal(volumeSum, volumeBurntSum));

            lastVolume = volumeSum.getVolume() * ModConfigs.treeHarvestMultiplier;
            lastBurntVolume = volumeBurntSum.getVolume() * ModConfigs.treeHarvestMultiplier;
        }
    }

    private Species getWailaSpecies(World world, BlockPos pos) {
        return TreeHelper.getBestGuessSpecies(world, pos);
    }

}
