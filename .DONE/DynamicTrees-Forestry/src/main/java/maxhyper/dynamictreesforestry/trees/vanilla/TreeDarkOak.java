package maxhyper.dynamictreesforestry.trees.vanilla;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorApple;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenHugeMushrooms;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenPredicate;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.TreeFamilyVanilla;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
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

public class TreeDarkOak extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 1;
    public static Block logBlock = Blocks.LOG2;
    public static int logMeta = 1;

    public class SpeciesDarkOak extends Species {

        protected FeatureGenHugeMushrooms underGen;
        protected FeatureGenFlareBottom flareBottomGen;
        protected FeatureGenRoots rootGen;
        protected FeatureGenMound moundGen;

        SpeciesDarkOak(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.darkOakLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.60f, 10.0f, 4, 4, 0.8f);
            setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.DARKOAK));

            setSoilLongevity(14);//Grows for a long long time

            envFactor(Type.COLD, 0.75f);
            envFactor(Type.HOT, 0.50f);
            envFactor(Type.DRY, 0.25f);
            envFactor(Type.MUSHROOM, 1.25f);

            generateSeed();
            setupStandardSeedDropping();

            //Add species features
            addGenFeature(new FeatureGenClearVolume(6));//Clear a spot for the thick tree trunk
            addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
            addGenFeature(new FeatureGenMound(5));//Establish mounds
            addGenFeature(new FeatureGenPredicate(
                            new FeatureGenHugeMushrooms().setMaxShrooms(1).setMaxAttempts(3)//Generate Huge Mushrooms
                    ).setBiomePredicate(biome -> biome == Biomes.ROOFED_FOREST)//Only allow this feature in roofed forests
            );
            addGenFeature(new FeatureGenRoots(13).setScaler(getRootScaler()));//Finally Generate Roots
        }

        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
            return (inRadius, trunkRadius) -> {
                float scale = MathHelper.clamp(trunkRadius >= 13 ? (trunkRadius / 24f) : 0, 0, 1);
                return (int) (inRadius * scale);
            };
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return isOneOfBiomes(biome, Biomes.ROOFED_FOREST);
        };

        @Override
        public int getLowestBranchHeight(World world, BlockPos pos) {
            return (int)(super.getLowestBranchHeight(world, pos) * biomeSuitability(world, pos));
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            return super.getEnergy(world, pos) * biomeSuitability(world, pos);
        }

        @Override
        public float getGrowthRate(World world, BlockPos pos) {
            return super.getGrowthRate(world, pos) * biomeSuitability(world, pos);
        }

        @Override
        public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
            if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
                if(radius > 2 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 6) {
                    world.setBlockState(pos, ModBlocks.blockStates.redMushroom);//Change branch to a red mushroom
                    world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
                }
                return true;
            }

            return false;
        }

        @Override
        public boolean isThick() {
            return true;
        }

    }

    BlockSurfaceRoot surfaceRootBlock;

    public TreeDarkOak() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.DARKOAK));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");

        ModContent.darkOakLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesDarkOak(this));
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList = super.getRegisterableBlocks(blockList);
        blockList.add(surfaceRootBlock);
        return blockList;
    }

    @Override
    public BlockSurfaceRoot getSurfaceRoots() {
        return surfaceRootBlock;
    }

}