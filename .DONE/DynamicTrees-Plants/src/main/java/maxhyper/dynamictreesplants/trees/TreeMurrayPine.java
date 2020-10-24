package maxhyper.dynamictreesplants.trees;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
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

public class TreeMurrayPine extends TreeFamily {

    public static Block leavesBlock = ModRegistry.LEAF_0;
    public static Block logBlock = ModRegistry.LOG_0;
    public static Block saplingBlock = ModRegistry.SAP_0;
    public static int meta = 3;

    public class SpeciesMurrayPine extends Species {

        SpeciesMurrayPine(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.murrayPineLeavesProperties);
            setGrowthLogicKit(new ConiferLogic(3.0f));

            setBasicGrowingParameters(0.5f, 18.0f, 30, 3, 0.9f);

            generateSeed();

            setupStandardSeedDropping();
        }

        /*
                                            * * *
                                          *       * *
                                        *             *
                                      *                 *
                                    *                     *
                            _ * * *                         *
        */
        int[] growthcurve = {0,1,1,1,2,3,4,5,6,6,6,5,5,4,3,2,1};

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {

            int signalHeight = (pos.getY() - signal.rootPos.getY());
            for (EnumFacing dir : EnumFacing.HORIZONTALS){
                probMap[dir.getIndex()] = signalHeight >= growthcurve.length?1:growthcurve[signalHeight];
            }

            if (signalHeight < lowestBranchHeight+2){
                for (EnumFacing dir : EnumFacing.HORIZONTALS){
                    probMap[dir.getIndex()] = (signal.numTurns > 0)?0:10;
                }
            } else if (signalHeight>signalEnergy-2){
                for (EnumFacing dir : EnumFacing.HORIZONTALS){
                    probMap[dir.getIndex()] = 0;
                }
            }

            if (!signal.isInTrunk()){
                probMap[EnumFacing.UP.getIndex()] = 0;
            }

            probMap[signal.dir.getOpposite().getIndex()] = 0;

            return probMap;
        }

    }

    public TreeMurrayPine() {
        super(new ResourceLocation(DynamicTreesPlants.MODID, "murrayPine"));

        setPrimitiveLog(logBlock.getDefaultState().withProperty(PropertyEnum.create("type", TheBigBookOfEnums.Logs.class), TheBigBookOfEnums.Logs.MURRAY_PINE));

        ModContent.murrayPineLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesMurrayPine(this));
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