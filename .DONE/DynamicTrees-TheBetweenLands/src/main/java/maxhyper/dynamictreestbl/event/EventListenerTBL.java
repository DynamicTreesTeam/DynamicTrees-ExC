package maxhyper.dynamictreestbl.event;

import com.ferreusveritas.dynamictrees.models.bakedmodels.BakedModelBlockRooty;
import maxhyper.dynamictreestbl.ModContent;
import maxhyper.dynamictreestbl.trees.TreeHearthgrove;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.entity.spawning.ICustomSpawnEntry;
import thebetweenlands.api.event.InitializeBetweenlandsBiomeEvent;
import thebetweenlands.common.entity.mobs.EntityEmberlingWild;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.biome.BiomePatchyIslands;
import thebetweenlands.common.world.biome.spawning.spawners.SurfaceSpawnEntry;

import java.lang.reflect.Field;
import java.util.List;

public class EventListenerTBL {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
            Block[] rootyBlocks = new Block[] {ModContent.blockRootyMud};

            for(Block block: rootyBlocks) {
                IBakedModel rootsObject = event.getModelRegistry().getObject(new ModelResourceLocation(block.getRegistryName(), "normal"));
                if (rootsObject != null) {
                    BakedModelBlockRooty rootyModel = new BakedModelBlockRooty(rootsObject);
                    event.getModelRegistry().putObject(new ModelResourceLocation(block.getRegistryName(), "normal"), rootyModel);
                }
            }
    }

    @SubscribeEvent
    public void onBLBiomeInitializeEvent(InitializeBetweenlandsBiomeEvent event){
        if (event.getBiome() instanceof BiomePatchyIslands){
            try {
                Field spawnEntries = InitializeBetweenlandsBiomeEvent.class.getDeclaredField("spawnEntries");
                spawnEntries.setAccessible(true);

                List<ICustomSpawnEntry> spawnEntriesList = (List<ICustomSpawnEntry>)spawnEntries.get(event);
                spawnEntriesList.add(new SurfaceSpawnEntry(8, EntityEmberlingWild.class, EntityEmberlingWild::new, (short) 20){
                    private boolean isNextToHearthgrove (World world, BlockPos spawnPos){
                        for (EnumFacing dir : EnumFacing.HORIZONTALS)
                            if (world.getBlockState(spawnPos.offset(dir)).getBlock() == TreeHearthgrove.hearthgroveBranch) return true;
                        return false;
                    }
                    @Override
                    public boolean canSpawn(World world, Chunk chunk, BlockPos pos, IBlockState spawnBlockState, IBlockState surfaceBlockState) {
                        return !spawnBlockState.isNormalCube() && isNextToHearthgrove(world, pos) && !spawnBlockState.getMaterial().isLiquid();
                    }
                }.setHostile(true).setGroupSize(1, 1).setSpawnCheckRadius(32));

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}