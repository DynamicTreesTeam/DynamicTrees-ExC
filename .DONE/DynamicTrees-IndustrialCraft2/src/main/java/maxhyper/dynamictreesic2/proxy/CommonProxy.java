package maxhyper.dynamictreesic2.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import maxhyper.dynamictreesic2.ModConfigs;
import maxhyper.dynamictreesic2.compat.IC2Proxy;
import maxhyper.dynamictreesic2.dropcreators.DropCreatorResin;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static maxhyper.dynamictreesic2.DynamicTreesIC2.proxyIC2;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModConfigs.preInit(event);
		// Disable default rubber tree world gen.
		if (com.ferreusveritas.dynamictrees.ModConfigs.worldGen){
			proxyIC2.IC2disableWorldGen();
		}
	}

	public void init() {
		ItemStack resin = proxyIC2.getIC2ResinStack();
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, "rubber")).
				addDropCreator(new DropCreatorResin(resin, ModConfigs.rubberDropMultiplier));

		// Register sapling replacements.
		registerSaplingReplacement(proxyIC2.IC2GetTreeBlocks(IC2Proxy.TreeBlock.SAPLING), "rubber");
	}

	private static void registerSaplingReplacement(final Block saplingBlock, final String speciesName) {
		TreeRegistry.registerSaplingReplacer(saplingBlock.getDefaultState(), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, speciesName)));
	}

	public void postInit() {
	}
	
}
