package maxhyper.dynamictreesquark.init;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesPaging;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import maxhyper.dynamictreesquark.DynamicTreesQuark;
import maxhyper.dynamictreesquark.trees.BlossomTree;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTQuarkRegistries {
    public static ArrayList<TreeFamily> trees = new ArrayList<>();
    public static Map<String, ILeavesProperties> leaves = new HashMap<>();

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();

        /* Soils
         */
        Block glowcelium = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("quark","glowcelium"));
        DirtHelper.registerSoil(glowcelium, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(glowcelium, DirtHelper.FUNGUS_LIKE);

        /* Leaves
         */
        leaves.putAll(LeavesPaging.build(new ResourceLocation(DynamicTreesQuark.MOD_ID, "leaves/quark.json")));

        /* Trees
         */
        trees.add(new BlossomTree());
        trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));

        /* Blocks
         */
        ArrayList<Block> treeBlocks = new ArrayList<>();
        trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
        treeBlocks.addAll(LeavesPaging.getLeavesListForModId(DynamicTreesQuark.MOD_ID));
        treeBlocks.addAll(RootyBlockHelper.generateListForRegistry(true, DynamicTreesQuark.MOD_ID));
        registry.registerAll(treeBlocks.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
        IForgeRegistry<Item> registry = itemRegistryEvent.getRegistry();

        /* Items
         */
        ArrayList<Item> treeItems = new ArrayList<>();
        trees.forEach(tree -> tree.getRegisterableItems(treeItems));
        registry.registerAll(treeItems.toArray(new Item[0]));
    }
}
