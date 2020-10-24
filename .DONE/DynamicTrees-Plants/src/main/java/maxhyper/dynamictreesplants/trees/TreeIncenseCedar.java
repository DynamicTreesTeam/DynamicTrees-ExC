package maxhyper.dynamictreesplants.trees;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import maxhyper.dynamictreesplants.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import shadows.plants2.data.enums.TheBigBookOfEnums;
import shadows.plants2.init.ModRegistry;

import java.util.List;
import java.util.Objects;

public class TreeIncenseCedar extends TreeFamily {

    public static Block leavesBlock = ModRegistry.LEAF_0;
    public static Block logBlock = ModRegistry.LOG_0;
    public static Block saplingBlock = ModRegistry.SAP_0;
    public static int meta = 2;

    public class SpeciesIncenseCedar extends Species {

        SpeciesIncenseCedar(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.incenseCedarLeavesProperties);
            setGrowthLogicKit(new ConiferLogic(3.0f));

            setBasicGrowingParameters(0.5f, 14.0f, 30, 3, 0.8f);

            generateSeed();

            setupStandardSeedDropping();
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
            int signalHeight = (pos.getY() - signal.rootPos.getY());
            if (signalHeight%2 == 0){
                for (EnumFacing dir : EnumFacing.HORIZONTALS){
                    probMap[dir.getIndex()] = 0;
                }
            }

            if (signal.numTurns > 1){
                for (EnumFacing dir : EnumFacing.HORIZONTALS){
                    probMap[dir.getIndex()] = 0;
                }
            }

            probMap[signal.dir.getOpposite().getIndex()] = 0;

            return probMap;
        }

    }

    public TreeIncenseCedar() {
        super(new ResourceLocation(DynamicTreesPlants.MODID, "incenseCedar"));

        setPrimitiveLog(logBlock.getDefaultState().withProperty(PropertyEnum.create("type", TheBigBookOfEnums.Logs.class), TheBigBookOfEnums.Logs.INCENSE_CEDAR));

        ModContent.incenseCedarLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesIncenseCedar(this));
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