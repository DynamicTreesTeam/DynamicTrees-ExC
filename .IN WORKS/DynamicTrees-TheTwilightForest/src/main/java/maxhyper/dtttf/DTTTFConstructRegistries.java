package maxhyper.dtttf;

import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTTTFConstructRegistries {

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        //event.registerType(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "blossom"), BlossomLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

//        for (Block slimeDirt : TinkerWorld.slimeDirt.values())
//            DirtHelper.registerSoil(slimeDirt, SLIME_LIKE);
//        for (EnumObject<SlimeGrassBlock.FoliageType, SlimeGrassBlock> grassTypes : TinkerWorld.slimeGrass.values())
//            for (SlimeGrassBlock slimeGrass : grassTypes.values())
//                DirtHelper.registerSoil(slimeGrass, SLIME_LIKE);
//
//        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(true, DynamicTreesTheTwilightForest.MOD_ID))
//            event.getRegistry().register(rooty);
    }

}
