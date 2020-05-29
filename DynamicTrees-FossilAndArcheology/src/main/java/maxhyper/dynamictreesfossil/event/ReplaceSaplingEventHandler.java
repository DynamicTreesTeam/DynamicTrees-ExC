package maxhyper.dynamictreesfossil.event;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;

import maxhyper.dynamictreesfossil.DynamicTreesFossil;
import maxhyper.dynamictreesfossil.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DynamicTreesFossil.MODID)
public class ReplaceSaplingEventHandler {
	
//	@SubscribeEvent
//	public static void onPlaceSapling(PlaceEvent event) {
//		if (!ModConfigs.replaceVanillaSapling) return;
//
//		IBlockState state = event.getPlacedBlock();
//
//		Species species = null;
//
//		species = findSapling(state, species, QTreeBlossoming.saplingBlock, 1, "blossoming");
//		species = findSapling(state, species, QTreeSwampOak.saplingBlock, 0,"swampOak");
//		species = findSapling(state, species, IDTreeMenril.saplingBlock, 0,"menril");
//		species = findSapling(state, species, TRTreeRubber.saplingBlock, 0,"rubber");
//		species = findSapling(state, species, TBTreeGoldenOak.saplingBlock, 0,"goldenOak");
//		species = findSapling(state, species, TBTreeEnderOak.saplingBlock, 0,"enderOak");
//		species = findSapling(state, species, TBTreeHellishOak.saplingBlock, 0,"hellishOak");
//		species = findSapling(state, species, TCTreeSlimeBlue.saplingBlock, 0,"slimeBlue");
//		species = findSapling(state, species, TCTreeSlimePurple.saplingBlock, 1,"slimePurple");
//		species = findSapling(state, species, TCTreeSlimeMagma.saplingBlock, 2,"slimeMagma");
//
//		if (species != null) {
//			event.getWorld().setBlockToAir(event.getPos());
//			if(!species.plantSapling(event.getWorld(), event.getPos())) {
//				double x = event.getPos().getX() + 0.5;
//				double y = event.getPos().getY() + 0.5;
//				double z = event.getPos().getZ() + 0.5;
//				EntityItem itemEntity = new EntityItem(event.getWorld(), x, y, z, species.getSeedStack(1));
//				event.getWorld().spawnEntity(itemEntity);
//			}
//		}
//	}
//
//	private static Species findSapling (IBlockState state, Species species, Block sapling, int meta, String id){
//		if (state.getBlock() == sapling && state.getBlock().getMetaFromState(state) == meta) {
//			return TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesFossil.MODID, id));
//		} else {
//			return species;
//		}
//	}
}
