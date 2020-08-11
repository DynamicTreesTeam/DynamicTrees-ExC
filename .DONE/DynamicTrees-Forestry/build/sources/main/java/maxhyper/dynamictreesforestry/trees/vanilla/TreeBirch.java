package maxhyper.dynamictreesforestry.trees.vanilla;

import java.util.Objects;
import java.util.Random;

import com.ferreusveritas.dynamictrees.api.TreeHelper;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.TreeFamilyVanilla;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TreeBirch extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 2;
    public static Block logBlock = Blocks.LOG;
    public static int logMeta = 2;

    public class SpeciesBirch extends Species {

        SpeciesBirch(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.birchLeavesProperties);

            //Birch are tall, skinny, fast growing trees
            setBasicGrowingParameters(0.1f, 14.0f, 4, 4, 1.25f);

            envFactor(Type.COLD, 0.75f);
            envFactor(Type.HOT, 0.50f);
            envFactor(Type.DRY, 0.50f);
            envFactor(Type.FOREST, 1.05f);

            generateSeed();
            setupStandardSeedDropping();
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return isOneOfBiomes(biome, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS);
        };

        @Override
        public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
            if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
                if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
                    world.setBlockState(pos, Blocks.BROWN_MUSHROOM.getDefaultState());//Change branch to a brown mushroom
                    world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState(), 3);//Change rooty dirt to dirt
                }
                return true;
            }

            return false;
        }

    }

    public TreeBirch() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.BIRCH));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.birchLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesBirch(this));
    }

    @Override
    public boolean autoCreateBranch() {
        return true;
    }
}
