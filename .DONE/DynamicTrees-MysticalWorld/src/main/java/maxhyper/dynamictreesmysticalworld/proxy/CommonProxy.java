package maxhyper.dynamictreesmysticalworld.proxy;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamilyVanilla;
import epicsquid.mysticalworld.config.ConfigManager;
import maxhyper.dynamictreesmysticalworld.DynamicTreesMysticalWorld;
import maxhyper.dynamictreesmysticalworld.ModConfig;
import maxhyper.dynamictreesmysticalworld.dropcreators.DropCreatorSilkwormEgg;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.List;

public class CommonProxy {
	
	public void preInit() {
	}
	
	public void init() {
		if (ModConfigs.worldGen){
			ConfigManager.burntTrees.attempts = 0;
		}

		if (ConfigManager.silkworm.enabled && ConfigManager.silkworm.leafDrops)
			addSilkwormDrops();
	}

	private void addSilkwormDrops () {
		final DropCreator dropCreatorSilkwormEgg = new DropCreatorSilkwormEgg();

		Arrays.asList(ModConfig.silkwormSpecies).forEach(speciesName -> {
			try {
				final Species species = TreeRegistry.findSpecies(new ResourceLocation(speciesName.substring(0, speciesName.indexOf(":")), speciesName.substring(speciesName.indexOf(":") + 1)));

				if (!species.isValid()) {
					LogManager.getLogger().warn("Error whilst adding silkworm egg drop creator for species '" + speciesName + "'. Could not find species with this registry name.");
					return;
				}

				species.addDropCreator(dropCreatorSilkwormEgg);
			} catch (Exception e) {
				LogManager.getLogger().warn("Error whilst adding silkworm egg drop creator for species '" + speciesName + "'. Please check each entry is a valid resource location.");
			}
		});
	}
	
	public void postInit() {
	}
	
}
