package maxhyper.dynamictreestheaether.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;

import com.legacy.aether.Aether;
import com.legacy.aether.AetherConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species skyroot, goldenOak, crystal, holiday;

    public void populate(BiomeDataBase dbase) {
        Biome HighlandsBiome = Biome.REGISTRY.getObject(Aether.locate("aether_highlands"));

        skyroot = TreeRegistry.findSpecies(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "skyroot"));
        goldenOak = TreeRegistry.findSpecies(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "goldenoak"));
        crystal = TreeRegistry.findSpecies(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "crystal"));
        holiday = TreeRegistry.findSpecies(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, AetherConfig.world_gen.christmas_time?"holiday":"skyroot"));

        dbase.setCancelVanillaTreeGen(HighlandsBiome, true);
        dbase.setSpeciesSelector(HighlandsBiome, new BiomePropertySelectors.RandomSpeciesSelector()
                .add(skyroot, 50).
                add(goldenOak, 2).
                add(holiday, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(HighlandsBiome, (rand, noiseDensity) -> noiseDensity * 0.1, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(HighlandsBiome, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

    }
}

