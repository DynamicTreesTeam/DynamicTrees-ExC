package maxhyper.dynamictreesplants.trees;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import maxhyper.dynamictreesplants.ModContent;
import maxhyper.dynamictreesplants.dropcreators.DropCreatorFruit;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistry;
import shadows.plants2.data.enums.TheBigBookOfEnums;
import shadows.plants2.init.ModRegistry;

import java.util.List;
import java.util.Objects;

public class TreeDarkCrystal extends TreeCrystal {

    public static int meta = 1;

    public class SpeciesDarkCrystal extends SpeciesCrystal {

        SpeciesDarkCrystal(TreeFamily treeFamily) {
            super(treeFamily, ModContent.darkCrystalLeavesProperties);

            addDropCreator(new DropCreatorFruit(new ItemStack(ModRegistry.GENERIC, 1, 7)));

            setupStandardSeedDropping();
        }

        public ItemStack getSeedStack(int qty) {
            Species Crystal = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "crystal"));
            ItemStack stack = Crystal.getSeedStack(qty).copy();
            stack.setCount(MathHelper.clamp(qty, 0, 64));
            return stack;
        }
    }

    public TreeDarkCrystal() {
        super("darkCrystal");
        setPrimitiveLog(logBlock.getDefaultState().withProperty(PropertyEnum.create("type", TheBigBookOfEnums.CrystalLogs.class), TheBigBookOfEnums.CrystalLogs.DARK_CRYSTAL));
        ModContent.darkCrystalLeavesProperties.setTree(this);
        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }
    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, meta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesDarkCrystal(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        return super.getRegisterableItems(itemList);
    }

}