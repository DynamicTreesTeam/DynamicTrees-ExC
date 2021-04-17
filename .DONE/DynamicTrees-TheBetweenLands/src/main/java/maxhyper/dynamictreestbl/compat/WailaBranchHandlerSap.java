package maxhyper.dynamictreestbl.compat;

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
import maxhyper.dynamictreestbl.trees.TreeRubber;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import thebetweenlands.common.registries.ItemRegistry;

import java.util.List;

public class WailaBranchHandlerSap extends WailaBranchHandler {

    private BlockPos lastPos = BlockPos.ORIGIN;
    private Species lastSpecies = Species.NULLSPECIES;
    private float lastVolume = 0;

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        tooltip.clear();

        if(WailaOther.invalid) {
            lastPos = BlockPos.ORIGIN;
            lastSpecies = Species.NULLSPECIES;
            lastVolume = 0;
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
            lastVolume = getTreeVolume(accessor.getWorld(), pos);
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

            String renderString = "";

            ItemStack seedStack = species.getSeedStack(1);
            String seedName = seedStack.getItem().getRegistryName().toString();
            renderString += SpecialChars.getRenderString("waila.stack", "1", seedName, String.valueOf(1), String.valueOf(seedStack.getItemDamage()));

            if(lastVolume > 0) {
                Species.LogsAndSticks las = species.getLogsAndSticks(lastVolume);
                boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,accessor.getPlayer().getHeldItemMainhand())!=0;
                if (silkTouch){
                    if(las.logs > 0) {
                        ItemStack logStack = species.getFamily().getPrimitiveLogItemStack(1);
                        String logName = logStack.getItem().getRegistryName().toString();
                        renderString += SpecialChars.getRenderString("waila.stack", "1", logName, String.valueOf(las.logs), String.valueOf(logStack.getItemDamage()));
                    }
                    if(las.sticks > 0) {
                        ItemStack stickStack = species.getFamily().getStick(1);
                        String stickName = stickStack.getItem().getRegistryName().toString();
                        renderString += SpecialChars.getRenderString("waila.stack", "1", stickName, String.valueOf(las.sticks), String.valueOf(stickStack.getItemDamage()));
                    }
                } else {
                    int sapCount = (int)(lastVolume * maxhyper.dynamictreestbl.ModConfigs.sapDropMultiplier);
                    if (sapCount > 0){
                        ItemStack resin = new ItemStack(ItemRegistry.SAP_BALL);
                        String amberName = resin.getItem().getRegistryName().toString();
                        renderString += SpecialChars.getRenderString("waila.stack", "1", amberName, String.valueOf(sapCount), String.valueOf(resin.getItemDamage()));
                    }
                }
            }

            tooltip.add(renderString);
        }

        return tooltip;
    }

    private float getTreeVolume(World world, BlockPos pos) {
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
            branch.analyse(state, world, pos, null, new MapSignal(volumeSum));

            return volumeSum.getVolume() * ModConfigs.treeHarvestMultiplier;
        }

        return 0;
    }

    private Species getWailaSpecies(World world, BlockPos pos) {
        return TreeHelper.getBestGuessSpecies(world, pos);
    }

}
