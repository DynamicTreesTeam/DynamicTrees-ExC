package maxhyper.dynamictreesmysticalworld.dropcreators;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import epicsquid.mysticalworld.config.ConfigManager;
import epicsquid.mysticalworld.init.ModItems;
import maxhyper.dynamictreesmysticalworld.DynamicTreesMysticalWorld;
import maxhyper.dynamictreesmysticalworld.ModConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * @author Harley O'Connor
 */
public final class DropCreatorSilkwormEgg extends DropCreator {

    public DropCreatorSilkwormEgg () {
        super(new ResourceLocation(DynamicTreesMysticalWorld.MODID, "silkworm_egg"));
    }

    @Override
    public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
        if (ModConfig.requireHand) return dropList;
        return this.getDrops(world, species, leafPos, random, dropList, fortune);
    }

    @Override
    public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
        return this.getDrops(access, species, breakPos, random, dropList, fortune);
    }

    private List<ItemStack> getDrops (IBlockAccess access, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int fortune) {
        if (random.nextInt(ConfigManager.safeInt(ConfigManager.silkworm.leafDropChance)) == 0)
            dropList.add(new ItemStack(ModItems.silkworm_egg));

        return dropList;
    }

}
