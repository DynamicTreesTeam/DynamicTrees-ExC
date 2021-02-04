package maxhyper.dynamictreesquark.trees;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesquark.DynamicTreesQuark;
import maxhyper.dynamictreesquark.init.DTQuarkRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

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
    }

    Species sweet, fiery, frosty, serene, sunny, warm;
    public BlossomTree (){
        super(new ResourceLocation(DynamicTreesQuark.MOD_ID, "blossom"));
        setPrimitiveLog(Blocks.SPRUCE_LOG);
    }

    @Override
    public void createSpecies() {
        sweet = new BlossomSpecies(new ResourceLocation(getName().getNamespace(), "sweet_"+ getName().getPath()),this, false);
        fiery = new BlossomSpecies(new ResourceLocation(getName().getNamespace(), "fiery_"+ getName().getPath()),this, true);
        frosty = new BlossomSpecies(new ResourceLocation(getName().getNamespace(), "frosty_"+ getName().getPath()),this, true);
        serene = new BlossomSpecies(new ResourceLocation(getName().getNamespace(), "serene_"+ getName().getPath()),this, true);
        sunny = new BlossomSpecies(new ResourceLocation(getName().getNamespace(), "sunny_"+ getName().getPath()),this, true);
        warm = new BlossomSpecies(new ResourceLocation(getName().getNamespace(), "warm_"+ getName().getPath()),this, true);
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
