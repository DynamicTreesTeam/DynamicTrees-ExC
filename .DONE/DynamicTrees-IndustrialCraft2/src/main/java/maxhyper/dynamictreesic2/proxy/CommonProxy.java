package maxhyper.dynamictreesic2.proxy;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import ic2.core.init.MainConfig;
import ic2.core.item.type.MiscResourceType;
import ic2.core.ref.ItemName;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import maxhyper.dynamictreesic2.dropcreators.DropCreatorResin;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class CommonProxy {
	
	public void preInit() {
		// Disable default rubber tree world gen.
		if (ModConfigs.worldGen) MainConfig.get().set("worldgen/rubberTree", false);
	}

	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, "rubberIC")).
				addDropCreator(new DropCreatorResin(ItemName.misc_resource.getItemStack(MiscResourceType.resin)));

		// Register sapling replacements.
		registerSaplingReplacement(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ic2", "sapling"))), "rubberIC");
	}

	private static void registerSaplingReplacement(final Block saplingBlock, final String speciesName) {
		TreeRegistry.registerSaplingReplacer(saplingBlock.getDefaultState(), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, speciesName)));
	}

	public void postInit() {

	}
	
}
