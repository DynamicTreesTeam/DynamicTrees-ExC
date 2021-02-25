package maxhyper.dynamictreesquark.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesPaging;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesquark.DynamicTreesQuark;
import maxhyper.dynamictreesquark.init.DTQuarkRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Random;

public class BlossomTree extends TreeFamily {

    public class BlossomSpecies extends Species {
        BlossomSpecies(ResourceLocation name, TreeFamily treeFamily, boolean requireTile) {
            super(name, treeFamily, DTQuarkRegistries.leaves.get(name.getPath()).setTree(treeFamily));
            setRequiresTileEntity(requireTile);
            setBasicGrowingParameters(0.3f, 14.0f, 2, 4, 0.8f);

            generateSeed();
            generateSapling();

            setupStandardSeedDropping();
        }

        @Override
        public boolean showSpeciesOnWaila() { return true; }

        @Override
        public DynamicLeavesBlock createLeavesBlock(ILeavesProperties leavesProperties) {
            DynamicLeavesBlock block = new DynamicLeavesBlock(leavesProperties){
                @Override
                public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
                    if (worldIn.isAirBlock(pos.down()) && rand.nextInt(5) == 0) {
                        double windStrength = 5.0D + Math.cos((double)worldIn.getGameTime() / 2000.0D) * 2.0D;
                        double windX = Math.cos((double)worldIn.getGameTime() / 1200.0D) * windStrength;
                        double windZ = Math.sin((double)worldIn.getGameTime() / 1000.0D) * windStrength;
                        worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, stateIn), (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, windX, -1.0D, windZ);
                    }
                }
            };
            block.setRegistryName(this.getRegistryName() + "_leaves");
            this.addValidLeafBlocks(leavesProperties);
            LeavesPaging.addLeavesBlockForModId(block, this.getRegistryName().getNamespace());
            return block;
        }
    }

    Species sweet, fiery, frosty, serene, sunny, warm;
    public BlossomTree (){
        super(new ResourceLocation(DynamicTreesQuark.MOD_ID, "blossom"));
        setPrimitiveLog(Blocks.SPRUCE_LOG);
    }

    @Override
    public void createSpecies() {
        sweet = new BlossomSpecies(new ResourceLocation(getRegistryName().getNamespace(), "sweet_"+ getRegistryName().getPath()),this, false);
        fiery = new BlossomSpecies(new ResourceLocation(getRegistryName().getNamespace(), "fiery_"+ getRegistryName().getPath()),this, true);
        frosty = new BlossomSpecies(new ResourceLocation(getRegistryName().getNamespace(), "frosty_"+ getRegistryName().getPath()),this, true);
        serene = new BlossomSpecies(new ResourceLocation(getRegistryName().getNamespace(), "serene_"+ getRegistryName().getPath()),this, true);
        sunny = new BlossomSpecies(new ResourceLocation(getRegistryName().getNamespace(), "sunny_"+ getRegistryName().getPath()),this, true);
        warm = new BlossomSpecies(new ResourceLocation(getRegistryName().getNamespace(), "warm_"+ getRegistryName().getPath()),this, true);
        setCommonSpecies(sweet);
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        speciesRegistry.registerAll(fiery, frosty, serene, sunny, warm);
    }

    @Override
    public List<Block> getRegisterableBlocks(List<Block> blockList) {
        fiery.getSapling().ifPresent(blockList::add);
        frosty.getSapling().ifPresent(blockList::add);
        serene.getSapling().ifPresent(blockList::add);
        sunny.getSapling().ifPresent(blockList::add);
        warm.getSapling().ifPresent(blockList::add);
        fiery.getLeavesBlock().ifPresent(blockList::add);
        frosty.getLeavesBlock().ifPresent(blockList::add);
        serene.getLeavesBlock().ifPresent(blockList::add);
        sunny.getLeavesBlock().ifPresent(blockList::add);
        warm.getLeavesBlock().ifPresent(blockList::add);
        return super.getRegisterableBlocks(blockList);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        fiery.getSeed().ifPresent(itemList::add);
        frosty.getSeed().ifPresent(itemList::add);
        serene.getSeed().ifPresent(itemList::add);
        sunny.getSeed().ifPresent(itemList::add);
        warm.getSeed().ifPresent(itemList::add);
        return super.getRegisterableItems(itemList);
    }
}
