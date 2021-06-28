package maxhyper.dttconstruct;

import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.SlimeGrassBlock;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTTConstructRegistries {

    public static final String SLIME_LIKE = "slime_like";

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        //event.registerType(new ResourceLocation(DynamicTreesTConstruct.MOD_ID, "blossom"), BlossomLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        DirtHelper.createNewAdjective(SLIME_LIKE);

        for (Block slimeDirt : TinkerWorld.slimeDirt.values()){
            DirtHelper.registerSoil(slimeDirt, DirtHelper.DIRT_LIKE);
            DirtHelper.registerSoil(slimeDirt, SLIME_LIKE);
        }
        for (EnumObject<SlimeType, SlimeGrassBlock> grassTypes : TinkerWorld.slimeGrass.values())
            for (SlimeGrassBlock slimeGrass : grassTypes.values())
                DirtHelper.registerSoil(slimeGrass, SLIME_LIKE);

        for (RootyBlock rooty : RootyBlockHelper.generateListForRegistry(true, DynamicTreesTConstruct.MOD_ID))
            event.getRegistry().register(rooty);
    }

}
