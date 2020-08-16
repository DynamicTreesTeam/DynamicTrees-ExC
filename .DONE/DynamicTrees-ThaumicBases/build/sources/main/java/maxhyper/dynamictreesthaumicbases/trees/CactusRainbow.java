package maxhyper.dynamictreesthaumicbases.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchCactus;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.event.SpeciesPostGenerationEvent;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.systems.substances.SubstanceTransform;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import maxhyper.dynamictreesthaumicbases.DynamicTreesThaumicBases;
import maxhyper.dynamictreesthaumicbases.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CactusRainbow extends TreeFamily {

    public static Block logBlock = Block.getBlockFromName("thaumicbases:rainbowcactus");

    public class SpeciesCactus extends Species {

        SpeciesCactus(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.cactusLeavesProperties);

            setBasicGrowingParameters(0.875f, 6.0f, 6, 2, 0.5f);

            this.setSoilLongevity(1);

            generateSeed();

            addDropCreator(new DropCreator(new ResourceLocation(ModConstants.MODID, "rainbowcactusseeds")) {
                @Override
                public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
                    int numLogs = (int) (volume);
                    while(numLogs > 0) {
//                        ItemStack fruit = new ItemStack(NaturaOverworld.saguaroFruitItem);
//                        fruit.setCount(Math.min(numLogs, 64));
                        dropList.add(species.getSeedStack(Math.min(numLogs/2, 64)));
                        //if (!world.isRemote){
                            for(int i = 0; i < numLogs * (3 + world.rand.nextInt(8)); ++i)
                                dropList.add(new ItemStack(Items.DYE,1,allowedDyes[world.rand.nextInt(allowedDyes.length)]));
                        //}
                        numLogs -= 64;
                    }
                    return dropList;
                }
                public final int[] allowedDyes = new int[]{1,2,5,2,6,7,2,8,9,10,2,11,12,13,14,2};
            });

            envFactor(Type.SNOWY, 0.25f);
            envFactor(Type.COLD, 0.5f);
            envFactor(Type.SANDY, 1.05f);

            clearAcceptableSoils();
            addAcceptableSoil(Blocks.SAND);
        }

        @Override
        public JoCode getJoCode(String joCodeString) {
            return new JoCodeCactus(joCodeString);
        }

        @Override
        public float getEnergy(World world, BlockPos pos) {
            long day = world.getTotalWorldTime() / 24000L;
            int month = (int)day / 30; //Change the hashs every in-game month

            return super.getEnergy(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 2) % 3);//Vary the height energy by a psuedorandom hash function
        }

        @Override
        public BlockRooty getRootyBlock() {
            return ModBlocks.blockRootySand;
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return isOneOfBiomes(biome, Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.MUTATED_DESERT);
        }

        @Override
        public boolean handleRot(World world, List<BlockPos> ends, BlockPos rootPos, BlockPos treePos, int soilLife, SafeChunkBounds safeBounds) {
            return false;
        }

        @Override
        protected int[] customDirectionManipulation(World world, BlockPos pos, int radius, GrowSignal signal, int probMap[]) {
            EnumFacing originDir = signal.dir.getOpposite();

            //Alter probability map for direction change
            probMap[0] = 0;//Down is always disallowed for cactus
            probMap[1] = signal.delta.getX() % 2 == 0 || signal.delta.getZ() % 2 == 0 ? getUpProbability() : 0;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = signal.isInTrunk() && (signal.energy > 1) ? 1 : 0;
            if (signal.dir != EnumFacing.UP) probMap[signal.dir.ordinal()] = 0;//Disable the current direction, unless that direction is up
            probMap[originDir.ordinal()] = 0;//Disable the direction we came from
            return probMap;
        }

        @Override
        protected EnumFacing newDirectionSelected(EnumFacing newDir, GrowSignal signal) {
            if(signal.isInTrunk() && newDir != EnumFacing.UP){ //Turned out of trunk
                signal.energy += 0.0f;
            }
            return newDir;
        }

        @Override
        public boolean applySubstance(World world, BlockPos rootPos, BlockPos hitPos, EntityPlayer player, EnumHand hand, ItemStack itemStack) {

            // Prevent transformation potions from being used on Cacti
            if(!(getSubstanceEffect(itemStack) instanceof SubstanceTransform)) {
                return super.applySubstance(world, rootPos, hitPos, player, hand, itemStack);
            }

            return false;
        }

        @Override
        public boolean canBoneMeal() {
            return false;
        }

        @Override
        public boolean transitionToTree(World world, BlockPos pos) {
            //Ensure planting conditions are right
            TreeFamily tree = getFamily();
            if(world.isAirBlock(pos.up()) && isAcceptableSoil(world, pos.down(), world.getBlockState(pos.down()))) {
                world.setBlockState(pos, tree.getDynamicBranch().getDefaultState());//set to a single branch
                placeRootyDirtBlock(world, pos.down(), 15);//Set to fully fertilized rooty sand underneath
                return true;
            }

            return false;
        }

        @Override
        public AxisAlignedBB getSaplingBoundingBox() {
            return new AxisAlignedBB(0.375f, 0.0f, 0.375f, 0.625f, 0.5f, 0.625f);
        }

        public SoundType getSaplingSound() {
            return SoundType.CLOTH;
        }
    }

    public CactusRainbow() {
        super(new ResourceLocation(DynamicTreesThaumicBases.MODID, "rainbowcactus"));

  //      setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 2));

        setStick(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock));
        stack.setCount(MathHelper.clamp(qty/2, 0, 64));
        return stack;
    }

    @Override
    public ILeavesProperties getCommonLeaves() {
        return ModBlocks.leaves.get(getName().getResourcePath());
    }

    @Override
    public BlockBranch createBranch() {
        BlockBranchCactus branch = new BlockBranchCactus( DynamicTreesThaumicBases.MODID + ":rainbowcactusbranch"){
            @Override
            public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) { }
        };
        branch.setHardness(0);
        return branch;
    }

    @Override
    public float getPrimaryThickness() {
        return 5.0f;
    }

    @Override
    public float getSecondaryThickness() {
        return 4.0f;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesCactus(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        getCommonSpecies().generateSeed();
    }

    protected class JoCodeCactus extends JoCode {

        public JoCodeCactus(String code) {
            super(code);
        }

        @Override
        public void generate(World world, Species species, BlockPos rootPos, Biome biome, EnumFacing facing, int radius, SafeChunkBounds safeBounds) {
            IBlockState initialDirtState = world.getBlockState(rootPos); // Save the initial state of the dirt in case this fails
            species.placeRootyDirtBlock(world, rootPos, 0); // Set to unfertilized rooty dirt

            // A Tree generation boundary radius is at least 2 and at most 8
            radius = MathHelper.clamp(radius, 2, 8);
            BlockPos treePos = rootPos.up();

            // Create tree
            setFacing(facing);
            generateFork(world, species, 0, rootPos, false);

            // Fix branch thicknesses and map out leaf locations
            BlockBranch branch = TreeHelper.getBranch(world.getBlockState(treePos));
            if(branch != null) {//If a branch exists then the growth was successful
                NodeFindEnds endFinder = new NodeFindEnds(); // This is responsible for gathering a list of branch end points
                MapSignal signal = new MapSignal(endFinder);
                branch.analyse(world.getBlockState(treePos), world, treePos, EnumFacing.DOWN, signal);
                List<BlockPos> endPoints = endFinder.getEnds();

                // Allow for special decorations by the tree itself
                species.postGeneration(world, rootPos, biome, radius, endPoints, safeBounds, initialDirtState);
                MinecraftForge.EVENT_BUS.post(new SpeciesPostGenerationEvent(world, species, rootPos, endPoints, safeBounds, initialDirtState));
            } else { // The growth failed.. turn the soil back to what it was
                world.setBlockState(rootPos, initialDirtState, careful ? 3 : 2);
            }
        }

        @Override
        public boolean setBlockForGeneration(World world, Species species, BlockPos pos, EnumFacing dir, boolean careful) {
            IBlockState defaultBranchState = species.getFamily().getDynamicBranch().getDefaultState();
            if (world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                boolean trunk = false;
                if (dir == EnumFacing.UP) {
                    IBlockState downState = world.getBlockState(pos.down());
                    if (TreeHelper.isRooty(downState) || (downState.getBlock() == defaultBranchState.getBlock() && downState.getValue(BlockBranchCactus.TRUNK) && downState.getValue(BlockBranchCactus.ORIGIN) == EnumFacing.DOWN)) {
                        trunk = true;
                    }
                }
                return !world.setBlockState(pos, defaultBranchState.withProperty(BlockBranchCactus.TRUNK, trunk).withProperty(BlockBranchCactus.ORIGIN, dir.getOpposite()), careful ? 3 : 2);
            }
            return true;
        }

    }

}