package maxhyper.dynamictreestechreborn.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestechreborn.DynamicTreesTechReborn;
import maxhyper.dynamictreestechreborn.ModConfigs;
import maxhyper.dynamictreestechreborn.dropcreators.DropCreatorResin;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import techreborn.Core;
import techreborn.init.ModItems;

import java.util.Objects;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		ModConfigs.preInit(event);
	}
	
	public void init() {
		// Disable default rubber tree world gen.
		if (com.ferreusveritas.dynamictrees.ModConfigs.worldGen) Core.worldGen.config.rubberTreeConfig.shouldSpawn = false;

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTechReborn.MODID, "rubber")).
				addDropCreator(new DropCreatorResin(new ItemStack(ModItems.PARTS, 1, 31), ModConfigs.rubberDropMultiplier));


		registerSaplingReplacement(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("techreborn", "rubber_sapling"))), "rubber");
	}

	private static void registerSaplingReplacement(final Block saplingBlock, final String speciesName) {
		TreeRegistry.registerSaplingReplacer(saplingBlock.getDefaultState(), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTechReborn.MODID, speciesName)));
	}

	public void postInit() {
	}
	
}
