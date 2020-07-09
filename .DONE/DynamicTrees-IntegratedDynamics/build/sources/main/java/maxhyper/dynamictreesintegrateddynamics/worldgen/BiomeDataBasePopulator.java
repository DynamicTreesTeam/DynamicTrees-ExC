package maxhyper.dynamictreesintegrateddynamics.worldgen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors.RandomSpeciesSelector;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase.Operation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;
import maxhyper.dynamictreesintegrateddynamics.DynamicTreesIntegratedDynamics;

import org.cyclops.integrateddynamics.world.biome.BiomeMeneglin;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species oak;
    private static Species menril;

    public void populate(BiomeDataBase dbase) {
        oak = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak"));

        if (Loader.isModLoaded("integrateddynamics")) {
            menril = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, "menril"));

            Biome.REGISTRY.forEach(biome -> {
                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {

                    RandomSpeciesSelector selector = new RandomSpeciesSelector().add(350).
                            add(menril, 1);
                    dbase.setSpeciesSelector(biome, selector, Operation.SPLICE_BEFORE);

                }
            });
            dbase.setSpeciesSelector(BiomeMeneglin.getInstance(), new RandomSpeciesSelector().add(oak, 4).add(menril, 1), Operation.REPLACE);
            dbase.setDensitySelector(BiomeMeneglin.getInstance(), (rand, noiseDensity) -> noiseDensity * 0.25, Operation.REPLACE);
            dbase.setChanceSelector(BiomeMeneglin.getInstance(), (rand, species, radius) -> {
                if (radius >= 3) { // Start dropping tree spawn opportunities when the radius gets bigger than 3
                    float chance = 2.0f / (radius);
                    return rand.nextFloat() < ((Math.sqrt(chance) * 1.125f) + 0.25f) ? BiomePropertySelectors.EnumChance.OK : BiomePropertySelectors.EnumChance.CANCEL;
                }
                return BiomePropertySelectors.EnumChance.CANCEL;
            }, Operation.REPLACE);
            dbase.setCancelVanillaTreeGen(BiomeMeneglin.getInstance(), true);
        }
    }
}

