package maxhyper.dynamictreesforestry.trees.vanilla;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenClearVolume;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenCocoa;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenMound;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenUndergrowth;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenVine;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.TreeFamilyVanilla;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModConstants;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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

public class TreeJungle extends TreeFamily {

    public static Block leavesBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("forestry","leaves.decorative.0"));
    public static int leavesMeta = 15;
    public static Block logBlock = Blocks.LOG;
    public static int logMeta = 3;

    public class SpeciesJungle extends Species {

        SpeciesJungle(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.jungleLeavesProperties);

            //Jungle Trees are tall, wildly growing, fast growing trees with low branches to provide inconvenient obstruction and climbing
            setBasicGrowingParameters(0.2f, 28.0f, 3, 2, 1.0f);
            setGrowthLogicKit(TreeRegistry.findGrowthLogicKit(ModTrees.JUNGLE));

            envFactor(Type.COLD, 0.15f);
            envFactor(Type.DRY,  0.20f);
            envFactor(Type.HOT, 1.1f);
            envFactor(Type.WET, 1.1f);

            generateSeed();
            setupStandardSeedDropping();

            //Add species features
            addGenFeature(new FeatureGenCocoa());
            addGenFeature(new FeatureGenVine().setQuantity(16).setMaxLength(16));
            addGenFeature(new FeatureGenUndergrowth());
        }

        @Override
        public boolean isBiomePerfect(Biome biome) {
            return BiomeDictionary.hasType(biome, Type.JUNGLE);
        };

    }

    public TreeJungle() {
        super(new ResourceLocation(DynamicTreesForestry.MODID, ModConstants.JUNGLE));

        setPrimitiveLog(logBlock.getStateFromMeta(logMeta), new ItemStack(logBlock, 1, logMeta));

        ModContent.jungleLeavesProperties.setTree(this);

        canSupportCocoa = true;

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
        setCommonSpecies(new SpeciesJungle(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        //speciesRegistry.register(megaSpecies);
    }

    @Override
    public boolean isThick() {
        return true;
    }

    @Override
    public boolean onTreeActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        //Place Cocoa Pod if we are holding Cocoa Beans
        if(heldItem != null) {
            if(heldItem.getItem() == Items.DYE && heldItem.getItemDamage() == 3) {
                BlockBranch branch = TreeHelper.getBranch(state);
                if(branch != null && branch.getRadius(state) == 8) {
                    if(side != EnumFacing.UP && side != EnumFacing.DOWN) {
                        pos = pos.offset(side);
                    }
                    if (world.isAirBlock(pos)) {
                        IBlockState cocoaState = ModBlocks.blockFruitCocoa.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player);
                        EnumFacing facing = cocoaState.getValue(BlockHorizontal.FACING);
                        world.setBlockState(pos, ModBlocks.blockFruitCocoa.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 2);
                        if (!player.capabilities.isCreativeMode) {
                            heldItem.shrink(1);
                        }
                        return true;
                    }
                }
            }
        }

        //Need this here to apply potions or bone meal.
        return super.onTreeActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
    }

}