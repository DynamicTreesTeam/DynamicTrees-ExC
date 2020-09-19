package maxhyper.dynamictreespamtrees.trees;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.pam.spookytree.BlockRegistry;
import com.pam.spookytree.Spookytree;
import maxhyper.dynamictreespamtrees.DynamicTreesPamTrees;
import maxhyper.dynamictreespamtrees.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import com.ferreusveritas.dynamictrees.items.Seed;

import java.util.List;
import java.util.function.BiFunction;

public class TreeSpooky extends TreeFamily {

    private static Block log;

    public class SpeciesSpooky extends Species {

        public SpeciesSpooky(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.spookyLeavesProperties);

            setBasicGrowingParameters(0.3f, 14.0f, 4, 4, 0.8f);

            generateSeed();

            setupStandardSeedDropping();
        }

        @Override
        public Species getMegaSpecies() {
            return megaSpecies;
        }
    }

    public class SpeciesMegaSpooky extends Species {

        public SpeciesMegaSpooky(TreeFamily treeFamily) {
            super(new ResourceLocation(treeFamily.getName().getResourceDomain(), "megaspooky"), treeFamily, ModContent.spookyLeavesProperties);

            setBasicGrowingParameters(0.30f, 18.0f, 4, 6, 0.8f);
            setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.DARKOAK));

            setSoilLongevity(14);//Grows for a long long time

            generateSeed();

            setupStandardSeedDropping();
            addGenFeature(new FeatureGenClearVolume(6));//Clear a spot for the thick tree trunk
            addGenFeature(new FeatureGenFlareBottom());//Flare the bottom
            addGenFeature(new FeatureGenMound(5));//Establish mounds
            addGenFeature(new FeatureGenRoots(13).setScaler(getRootScaler()));//Finally Generate Roots
        }
        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
            return (inRadius, trunkRadius) -> {
                float scale = MathHelper.clamp(trunkRadius >= 13 ? (trunkRadius / 24f) : 0, 0, 1);
                return (int) (inRadius * scale);
            };
        }

        @Override public ItemStack getSeedStack(int qty) { return getCommonSpecies().getSeedStack(qty); }
        @Override public Seed getSeed() { return getCommonSpecies().getSeed(); }

        @Override
        public boolean isMega() {
            return true;
        }

        @Override
        public int maxBranchRadius() {
            return 24;
        }

        @Override
        public boolean isThick() {
            return true;
        }

        @Override
        public boolean getRequiresTileEntity(World world, BlockPos pos) {
            return !isLocationForMega(world, pos);
        }
    }

    protected boolean isLocationForMega(World world, BlockPos trunkPos) {
        return BiomeDictionary.hasType(world.getBiome(trunkPos), BiomeDictionary.Type.SPOOKY);
    }

    Species megaSpecies;
    BlockSurfaceRoot surfaceRootBlock;
    public TreeSpooky() {
        super(new ResourceLocation(DynamicTreesPamTrees.MODID, "spooky"));

        log = BlockRegistry.spookytreeLog;
        setPrimitiveLog(BlockRegistry.spookytreeLog.getDefaultState());

        surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");
        addSpeciesLocationOverride((world, trunkPos) -> isLocationForMega(world, trunkPos) ? megaSpecies : Species.NULLSPECIES);

        ModContent.spookyLeavesProperties.setTree(this);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(log, qty);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        megaSpecies = new SpeciesMegaSpooky(this);
        setCommonSpecies(new SpeciesSpooky(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        speciesRegistry.register(megaSpecies);
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList = super.getRegisterableBlocks(blockList);
        blockList.add(surfaceRootBlock);
        return blockList;
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public BlockSurfaceRoot getSurfaceRoots() {
        return surfaceRootBlock;
    }

    //Just have this here for no other reason that the imports need to be in an excluded class
    public static void clearVanillaTreeSpawning(){
        if (ModConfigs.worldGen){
            ModContent.spookySpawnNaturally = Spookytree.spawnNaturally;
            Spookytree.spawnNaturally = false;
        }
    }

}
