package maxhyper.dynamictreesextrautils2.event;

import maxhyper.dynamictreesextrautils2.DynamicTreesExtraUtils2;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesExtraUtils2.MODID)
public class ReplaceSaplingEventHandler {
	
//	@SubscribeEvent
//	public static void onPlaceSapling(PlaceEvent event) {
//		if (!ModConfigs.replaceVanillaSapling) return;
//
//		IBlockState state = event.getPlacedBlock();
//
//		Species species = null;
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
//			return TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExtraUtils2.MODID, id));
//		} else {
//			return species;
//		}
//	}
}
