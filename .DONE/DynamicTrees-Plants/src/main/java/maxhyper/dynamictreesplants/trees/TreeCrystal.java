package maxhyper.dynamictreesplants.trees;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockBranchBasic;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicSapling;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import maxhyper.dynamictreesplants.ModContent;
import maxhyper.dynamictreesplants.dropcreators.DropCreatorFruit;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import shadows.plants2.data.enums.TheBigBookOfEnums;
import shadows.plants2.init.ModRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TreeCrystal extends TreeFamily {

    public static Block leavesBlock = ModRegistry.CRYSTAL_LEAF;
    public static Block logBlock = ModRegistry.CRYSTAL_LOG;
    public static Block saplingBlock = ModRegistry.CRYSTAL_SAP;
    public static int meta = 0;

    public class SpeciesCrystal extends Species {

        SpeciesCrystal(TreeFamily treeFamily, ILeavesProperties leaves) {
            super(treeFamily.getName(), treeFamily, leaves);
            setBasicGrowingParameters(tapering, signalEnergy, upProbability, lowestBranchHeight, 0.5f);
        }

        SpeciesCrystal(TreeFamily treeFamily) {
            this(treeFamily, ModContent.crystalLeavesProperties);

            addDropCreator(new DropCreatorFruit(new ItemStack(ModRegistry.GENERIC, 1, 5)));

            generateSeed();
            setupStandardSeedDropping();
        }

        @Override
        protected void setStandardSoils() {
            addAcceptableSoils(DirtHelper.NETHERLIKE, ModContent.CRYSTALLIKE);
        }

        @Override
        public BlockRooty getRootyBlock(World world, BlockPos rootPos) {
            if (DirtHelper.isSoilAcceptable(world.getBlockState(rootPos).getBlock(), DirtHelper.getSoilFlags(ModContent.CRYSTALLIKE))){
                return ModContent.rootyCrystalDirt;
            } else {
                return ModBlocks.blockRootyDirt;
            }

        }

        @Override
        public SoundType getSaplingSound() {
            return SoundType.GLASS;
        }

        @Override
        public boolean plantSapling(World world, BlockPos pos) {
            if ((world.getTotalWorldTime()/10)%3 == 0){
                Species darkCrystalSpecies = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesPlants.MODID, "darkCrystal"));
                if(world.getBlockState(pos).getBlock().isReplaceable(world, pos) && BlockDynamicSapling.canSaplingStay(world, this, pos)) {
                    ModBlocks.blockDynamicSapling.setSpecies(world, pos, darkCrystalSpecies);
                    Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
                return false;
            } else {
                boolean a = super.plantSapling(world,pos);
                Minecraft.getMinecraft().renderGlobal.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
                return a;
            }
        }
    }

    public TreeCrystal(String name) {
        super(new ResourceLocation(DynamicTreesPlants.MODID, name));
        setStick(new ItemStack(ModRegistry.GENERIC, 1, 9)); //crystal stick
    }

    public TreeCrystal() {
        this("crystal");
        setPrimitiveLog(logBlock.getDefaultState().withProperty(PropertyEnum.create("type", TheBigBookOfEnums.CrystalLogs.class), TheBigBookOfEnums.CrystalLogs.CRYSTAL));
        ModContent.crystalLeavesProperties.setTree(this);
        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }

    @Override
    public ItemStack getPrimitiveLogItemStack(int qty) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(logBlock), 1, meta);
        stack.setCount(MathHelper.clamp(qty, 0, 64));
        return stack;
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesCrystal(this));
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
    public BlockBranch createBranch() {
        return new BlockBranchBasic(this.getName()+"branch"){

            @Override
            public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
                return 0;
            }

            @Override
            public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
                return 0;
            }

            @Override
            public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
                return 15;
            }

            @Override
            public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
                return SoundType.GLASS;
            }

            @Override
            public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
                return false;
            }
        };
    }
}