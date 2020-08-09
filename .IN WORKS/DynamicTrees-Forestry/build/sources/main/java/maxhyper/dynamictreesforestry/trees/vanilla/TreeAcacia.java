package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.TreeFamilyVanilla;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class TreeAcacia extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.1"));
    public static int leavesMeta = 6;
    public static Block logBlock = Blocks.LOG2;
    public static int logMeta = 0;

    public class SpeciesAcacia extends Species {

        SpeciesAcacia(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.acaciaLeavesProperties);

            setBasicGrowingParameters(0.4f, 10.0f, 1, 4, 0.7f);

            envFactor(Type.COLD, 0.25f);
            envFactor(Type.NETHER, 0.75f);
            envFactor(Type.WET, 0.75f);

            generateSeed();
            setupStandardSeedDropping();
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return BiomeDictionary.hasType(biome, Type.SAVANNA);
        }

    }

    public TreeAcacia() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, "acacia"));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.acaciaLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesAcacia(this));
    }

    @Override
    public boolean autoCreateBranch() {
        return true;
    }
}
