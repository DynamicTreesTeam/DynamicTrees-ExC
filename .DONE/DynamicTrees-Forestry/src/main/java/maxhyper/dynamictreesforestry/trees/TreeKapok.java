package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockSurfaceRoot;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFlareBottom;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenRoots;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockBranchThickForestry;
import maxhyper.dynamictreesforestry.items.ItemDynamicSeedKapok;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class TreeKapok extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.1"));
    public static int leavesMeta = 2;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.2"));
    public static int logMeta = 0;

    public class SpeciesKapok extends Species {

        SpeciesKapok(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.kapokLeavesProperties);

            setBasicGrowingParameters(0.3f, 27.0f, 30, 16, 0.9f);

            setSeedStack(new ItemStack(new ItemDynamicSeedKapok()));

            setupStandardSeedDropping();

            addGenFeature(new FeatureGenVine());
            addGenFeature(new FeatureGenFlareBottom());
            addGenFeature(new FeatureGenMound(5));
            addGenFeature(new FeatureGenRoots(8).setScaler(getRootScaler()));//Finally Generate Roots
        }

        protected BiFunction<Integer, Integer, Integer> getRootScaler() {
            return (inRadius, trunkRadius) -> {
                float scale = MathHelper.clamp(trunkRadius >= 8 ? (trunkRadius / 18f) : 0, 0, 1);
                return (int) (inRadius * scale);
            };
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
            probMap[EnumFacing.DOWN.getIndex()] = 0;
            if (!signal.isInTrunk()){
                probMap[EnumFacing.UP.getIndex()] = 1;
                if (signal.rand.nextInt(2) == 0){
                    for (EnumFacing dir : EnumFacing.HORIZONTALS){
                        probMap[dir.getIndex()] = 1;
                    }
                }
            }
            probMap[signal.dir.getOpposite().getIndex()] = 0;

            return probMap;
        }

        @Override
        public int maxBranchRadius() {
            return 10;
        }

        @Override
        public boolean isThick() {
            return true;
        }
    }

    BlockSurfaceRoot surfaceRootBlock;

    public TreeKapok() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.KAPOK));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        surfaceRootBlock = new BlockSurfaceRoot(Material.WOOD, getName() + "root");

        ModContent.kapokLeavesProperties.setTree(this);

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), qty, logMeta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public int getRootColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return 0x686f30;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesKapok(this));
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        blockList.add(surfaceRootBlock);
        return super.getRegisterableBlocks(blockList);
    }

    @Override
    public BlockSurfaceRoot getSurfaceRoots() {
        return surfaceRootBlock;
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchThickForestry(this.getName() + "branch", EnumForestryWoodType.KAPOK);
    }
}