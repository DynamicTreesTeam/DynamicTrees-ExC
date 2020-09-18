package maxhyper.dynamictreesatum.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.utils.AtumRegistry;
import maxhyper.dynamictreesatum.DynamicTreesAtum;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species deadtree, deadpalm, palm;

    private static void createStaticAliases() {
        deadtree = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesAtum.MODID, "deadtree"));
        deadpalm = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesAtum.MODID, "deadpalm"));
        palm = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesAtum.MODID, "palm"));
    }

    Map<Biome, Float> DeadTreeRarity = new HashMap<Biome, Float>(){{
         put(AtumBiomes.DEADWOOD_FOREST, 1f);
        put(AtumBiomes.LIMESTONE_CRAGS, 0.12f);
        put(AtumBiomes.LIMESTONE_MOUNTAINS, 0.1f);
        put(AtumBiomes.SAND_DUNES, 0.1f);
        put(AtumBiomes.SAND_HILLS, 0.08f);
        put(AtumBiomes.SAND_PLAINS, 0.01f);
    }};

    public void populate(BiomeDataBase dbase) {
        createStaticAliases();
        //AtumConfig.FOG_ENABLED = false;

        for (Biome biome : AtumRegistry.BIOMES){
            dbase.setCancelVanillaTreeGen(biome, true);
        }

        dbase.setSpeciesSelector(AtumBiomes.DEAD_OASIS, new BiomePropertySelectors.RandomSpeciesSelector().
                add(deadpalm, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(AtumBiomes.DEAD_OASIS, (rand, noiseDensity) -> noiseDensity * 0.01, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(AtumBiomes.DEAD_OASIS, (rand, species, radius) -> rand.nextFloat() < 0.35f ? BiomePropertySelectors.EnumChance.OK : BiomePropertySelectors.EnumChance.CANCEL, BiomeDataBase.Operation.REPLACE);

        dbase.setSpeciesSelector(AtumBiomes.OASIS, new BiomePropertySelectors.RandomSpeciesSelector().
                add(palm, 1), BiomeDataBase.Operation.REPLACE);
        dbase.setDensitySelector(AtumBiomes.OASIS, (rand, noiseDensity) -> noiseDensity * 0.01, BiomeDataBase.Operation.REPLACE);
        dbase.setChanceSelector(AtumBiomes.OASIS, (rand, species, radius) -> rand.nextFloat() < 0.35f ? BiomePropertySelectors.EnumChance.OK : BiomePropertySelectors.EnumChance.CANCEL, BiomeDataBase.Operation.REPLACE);

        for (Biome biome : DeadTreeRarity.keySet()){
            dbase.setSpeciesSelector(biome, new BiomePropertySelectors.RandomSpeciesSelector().
                    add(deadtree, 1), BiomeDataBase.Operation.REPLACE);
            dbase.setDensitySelector(biome, (rand, noiseDensity) -> noiseDensity * 0.5, BiomeDataBase.Operation.REPLACE);
            dbase.setChanceSelector(biome, (rand, species, radius) -> rand.nextFloat()*2 < DeadTreeRarity.get(biome) ? BiomePropertySelectors.EnumChance.OK : BiomePropertySelectors.EnumChance.CANCEL, BiomeDataBase.Operation.REPLACE);
        }

    }
}

