package maxhyper.dynamictreesforestry.trees.vanilla;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.genfeatures.FeatureGenFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class TreeOak extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 0;
    public static Block logBlock = Blocks.LOG;
    public static int logMeta = 0;

    public class SpeciesOak extends Species {

        SpeciesOak(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.oakLeavesProperties);

            //Oak trees are about as average as you can get
            setBasicGrowingParameters(0.4f, 10.0f, 1, 4, 0.7f);

            envFactor(Type.COLD, 0.75f);
            envFactor(Type.HOT, 0.75f);
            envFactor(Type.DRY, 0.25f);

            generateSeed();
            //setupStandardSeedDropping();

            addGenFeature(new FeatureGenFruitLeaves(8, 10, ModContent.oakLeavesProperties.getDynamicLeavesState(), ModContent.fruitAppleLeavesProperties.getDynamicLeavesState(), 0.5f));

            addGenFeature(new FeatureGenFruit(ModBlocks.blockApple).setRayDistance(4));
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return isOneOfBiomes(biome, Biomes.FOREST, Biomes.FOREST_HILLS, Biomes.PLAINS);
        }

        @Override
        public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
            if(super.rot(world, pos, neighborCount, radius, random, rapid)) {
                if(radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
                    world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
                    world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
                }
                return true;
            }

            return false;
        }

    }

    public TreeOak() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.OAK));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.oakLeavesProperties.setTree(this);
        ModContent.fruitAppleLeavesProperties.setTree(this);

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
        setCommonSpecies(new SpeciesOak(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        return super.getRegisterableItems(itemList);
    }

    @Override
    public boolean autoCreateBranch() {
        return true;
    }
}