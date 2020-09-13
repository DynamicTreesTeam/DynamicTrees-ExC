package maxhyper.dynamictreesic2.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import ic2.core.block.Ic2Sapling;
import ic2.core.init.BlocksItems;
import ic2.core.item.type.MiscResourceType;
import ic2.core.ref.ItemName;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import maxhyper.dynamictreesic2.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreesic2.dropcreators.DropCreatorResin;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preInit() {
	}

	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, "rubberIC")).
				addDropCreator(new DropCreatorResin(ItemName.misc_resource.getItemStack(MiscResourceType.resin)));

		// Register sapling replacements.
		registerSaplingReplacement(Block.getBlockFromName("ic2:sapling"), "rubberIC");
	}

	private static void registerSaplingReplacement(final Block saplingBlock, final String speciesName) {
		TreeRegistry.registerSaplingReplacer(saplingBlock.getDefaultState(), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, speciesName)));
	}

	public void postInit() {

	}
	
}
