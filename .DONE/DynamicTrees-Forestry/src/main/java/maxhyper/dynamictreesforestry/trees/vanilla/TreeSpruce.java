package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenPodzol;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.TreeFamilyVanilla;
import forestry.core.blocks.BlockRegistry;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class TreeSpruce extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 10;
    public static Block logBlock = Blocks.LOG;
    public static int logMeta = 1;

    public class SpeciesSpruce extends Species {

        SpeciesSpruce(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.spruceLeavesProperties);

            //Spruce are conical thick slower growing trees
            setBasicGrowingParameters(0.6f, 18.0f, 1, 5, 0.9f);
            setGrowthLogicKit(new ConiferLogic(2.0f));

            envFactor(Type.HOT, 0.50f);
            envFactor(Type.DRY, 0.25f);
            envFactor(Type.WET, 0.75f);

            generateSeed();
            setupStandardSeedDropping();

            //Add species features
            addGenFeature(new FeatureGenConiferTopper(getLeavesProperties()));
            //addGenFeature(new FeatureGenPodzol());
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int[] probMap) {
            if (signal.isInTrunk()){
                probMap[EnumFacing.UP.getIndex()] = 2;
                for (EnumFacing dir : EnumFacing.HORIZONTALS){
                    probMap[dir.getIndex()] = 2;
                }
            }
            probMap[signal.dir.getOpposite().getIndex()] = 0;
            return probMap;
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return BiomeDictionary.hasType(biome, Type.CONIFEROUS);
        }

    }

    public TreeSpruce() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.SPRUCE));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.spruceLeavesProperties.setTree(this);

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, logMeta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesSpruce(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
    }

    @Override
    public boolean isThick() {
        return true;
    }

}
