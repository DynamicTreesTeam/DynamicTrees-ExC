package maxhyper.dynamictreestheaether2.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;

import maxhyper.dynamictreestheaether2.DynamicTreesTheAether2;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species skyroot, goldenOak, crystal, holiday;

    public void populate(BiomeDataBase dbase) {
//        Biome HighlandsBiome = Biome.REGISTRY.getObject(Aether.locate("aether_highlands"));
//
//        skyroot = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether2.MODID, "skyroot"));
//        goldenOak = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether2.MODID, "goldenoak"));
//        crystal = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether2.MODID, "crystal"));
//        holiday = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether2.MODID, AetherConfig.world_gen.christmas_time?"holiday":"skyroot"));
//
//        dbase.setCancelVanillaTreeGen(HighlandsBiome, true);
//        dbase.setSpeciesSelector(HighlandsBiome, new BiomePropertySelectors.RandomSpeciesSelector()
//                .add(skyroot, 50).
//                add(goldenOak, 2).
//                add(holiday, 1), BiomeDataBase.Operation.REPLACE);
//        dbase.setDensitySelector(HighlandsBiome, (rand, noiseDensity) -> noiseDensity * 0.1, BiomeDataBase.Operation.REPLACE);
//        dbase.setChanceSelector(HighlandsBiome, (rand, species, radius) -> BiomePropertySelectors.EnumChance.OK, BiomeDataBase.Operation.REPLACE);

    }
}

