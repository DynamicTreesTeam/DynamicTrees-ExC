package maxhyper.dynamictreescuisine.proxy;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;

import maxhyper.dynamictreescuisine.trees.TreeCitrus;
import net.minecraft.block.Block;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
	}
	
	@Override
	public void init() {
		super.init();
		registerColorHandlers();
	}
	
	@Override public void postInit() {
		super.postInit();
	}
	
	public void registerColorHandlers() {
		for (TreeCitrus.citrusType type: TreeCitrus.citrusType.values()) {
			ModelHelper.regColorHandler(type.leavesBlock, (state, worldIn, pos, tintIndex) -> {
				//boolean inWorld = worldIn != null && pos != null;
				if (tintIndex == 1){
					return type.fruitTint;
				} else {
					Block block = state.getBlock();

					if (TreeHelper.isLeaves(block)) {
						return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
					}
					return 0x00FF00FF; //Magenta
				}
			});
		}
	}
}
