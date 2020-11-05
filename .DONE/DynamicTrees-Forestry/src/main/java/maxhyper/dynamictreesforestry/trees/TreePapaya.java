package maxhyper.dynamictreesforestry.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreatorSeed;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import forestry.api.arboriculture.EnumForestryWoodType;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.blocks.BlockDynamicLeavesPalm;
import maxhyper.dynamictreesforestry.genfeatures.FeatureGenFruitPod;
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
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreePapaya extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "leaves.decorative.2"));
    public static int leavesMeta = 0;
    public static Block logBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry",
            "logs.4"));
    public static int logMeta = 3;

    public class SpeciesPapaya extends Species {

        SpeciesPapaya(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.papayaLeavesProperties);

            //Dark Oak Trees are tall, slowly growing, thick trees
            setBasicGrowingParameters(0.5f, 8.0f, 4, 3, 0.8f);

            generateSeed();
            //setupStandardSeedDropping();

            ModContent.papayaFruit.setSpecies(this);
            addGenFeature(new FeatureGenFruitPod(ModContent.papayaFruit, 2));

            addDropCreator(new DropCreatorSeed(3.0f) {
                @Override
                public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
                    if(random.nextInt(16) == 0) { // 1 in 4 chance to drop a seed on destruction..
                        dropList.add(getFruit());
                    }
                    return dropList;
                }

                private ItemStack getFruit (){
                    return new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(
                            new ResourceLocation("forestry", "fruits"))), 1, 6);
                }

                @Override
                public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
                    int chance = 16;
                    if (fortune > 0) {
                        chance -= fortune;
                        if (chance < 3) {
                            chance = 3;
                        }
                    }
                    if (random.nextInt(chance) == 0) {
                        dropList.add(getFruit());
                    }
                    return dropList;
                }
            });
        }

        @Override
        public float seasonalFruitProductionFactor(World world, BlockPos pos) {
            return 1; //all year round
        }

        @Override
        public boolean testFlowerSeasonHold(World world, BlockPos pos, float seasonValue) {
            return false;
        }

        @Override
        public int getSeasonalTooltipFlags(int dimension) {
            return 15;//all year round
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
            EnumFacing originDir = signal.dir.getOpposite();

            // Alter probability map for direction change
            probMap[0] = 0; // Down is always disallowed for palm
            probMap[1] = 10;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;
            probMap[originDir.ordinal()] = 0; // Disable the direction we came from

            return probMap;
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 3); // Vary the height energy by a psuedorandom hash function
        }

        @Override
        public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, int soilLife, boolean natural) {
            IBlockState trunkBlockState = world.getBlockState(treePos);
            BlockBranch branch = TreeHelper.getBranch(trunkBlockState);
            NodeFindEnds endFinder = new NodeFindEnds();
            MapSignal signal = new MapSignal(endFinder);
            branch.analyse(trunkBlockState, world, treePos, EnumFacing.DOWN, signal);
            List<BlockPos> endPoints = endFinder.getEnds();

            for (BlockPos endPoint: endPoints) {
                TreeHelper.ageVolume(world, endPoint, 2, 3, 3, SafeChunkBounds.ANY);
            }

            // Make sure the bottom block is always just a little thicker that the block above it.
            int radius = branch.getRadius(world.getBlockState(treePos.up()));
            if (radius != 0) {
                branch.setRadius(world, treePos, radius + 1, null);
            }

            return super.postGrow(world, rootPos, treePos, soilLife, natural);
        }

        @Override
        public void postGeneration(World world, BlockPos rootPos, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
            for (BlockPos endPoint : endPoints) {
                TreeHelper.ageVolume(world, endPoint, 1, 2, 3, safeBounds);
            }
            super.postGeneration(world, rootPos, biome, radius, endPoints, safeBounds, initialDirtState);
        }

        public boolean transitionToTree(World world, BlockPos pos) {
            //Ensure planting conditions are right
            TreeFamily family = getFamily();
            if(world.isAirBlock(pos.up()) && isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()))) {
                family.getDynamicBranch().setRadius(world, pos, (int)family.getPrimaryThickness(), null);//set to a single branch with 1 radius
                world.setBlockState(pos.up(), getLeavesProperties().getDynamicLeavesState());//Place 2 leaf blocks on top
                world.setBlockState(pos.up(2), getLeavesProperties().getDynamicLeavesState().withProperty(BlockDynamicLeaves.HYDRO, 3));
                placeRootyDirtBlock(world, pos.down(), 15);//Set to fully fertilized rooty dirt underneath
                return true;
            }
            return false;
        }

        @Override
        public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
            if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(DirtHelper.SANDLIKE))){
                return ModBlocks.blockRootySand;
            } else {
                return ModBlocks.blockRootyDirt;
            }
        }
    }

    public TreePapaya() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.PAPAYA));

        //setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.papayaLeavesProperties.setTree(this);

        this.canSupportCocoa = true;

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
        return 0xb69b30;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new TreePapaya.SpeciesPapaya(this));
    }

    @Override
    public float getPrimaryThickness() {
        return 3.0f;
    }

    @Override
    public float getSecondaryThickness() {
        return 3.0f;
    }

    @Override
    public HashMap<BlockPos, IBlockState> getFellingLeavesClusters(BranchDestructionData destructionData) {

        if(destructionData.getNumEndpoints() < 1) {
            return null;
        }

        HashMap<BlockPos, IBlockState> leaves = new HashMap<>();
        BlockPos relPos = destructionData.getEndPointRelPos(0).up();//A palm tree is only supposed to have one endpoint at it's top.
        ILeavesProperties leavesProperties = getCommonSpecies().getLeavesProperties();

        leaves.put(relPos, leavesProperties.getDynamicLeavesState(4));//The barky overlapping part of the palm frond cluster
        leaves.put(relPos.up(), leavesProperties.getDynamicLeavesState(3));//The leafy top of the palm frond cluster

        //The 4 corners and 4 sides of the palm frond cluster
        for(int hydro = 1; hydro <= 2; hydro++) {
            IExtendedBlockState extState = (IExtendedBlockState) leavesProperties.getDynamicLeavesState(hydro);
            for(CoordUtils.Surround surr : BlockDynamicLeavesPalm.hydroSurroundMap[hydro]) {
                leaves.put(relPos.add(surr.getOpposite().getOffset()), extState.withProperty(BlockDynamicLeavesPalm.CONNECTIONS[surr.ordinal()], true));
            }
        }

        return leaves;
    }

    @Override
    public BlockBranch createBranch() {
        return new BlockBranchBasic(this.getName() + "branch"){
            @Override
            public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
                int radius = getRadius(blockState);
                return EnumForestryWoodType.PALM.getHardness() * (radius * radius) / 64.0f * 8.0f;
            }
        };
    }
}