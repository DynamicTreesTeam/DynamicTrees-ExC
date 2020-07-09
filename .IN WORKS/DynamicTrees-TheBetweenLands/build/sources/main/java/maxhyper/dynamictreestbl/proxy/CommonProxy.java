package maxhyper.dynamictreestbl.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.dropcreators.DropCreatorResin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thebetweenlands.common.registries.ItemRegistry;


public class CommonProxy {
	
	public void preInit() {
	}
	
	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTBL.MODID, "rubber")).
				addDropCreator(new DropCreatorResin(new ItemStack(ItemRegistry.SAP_BALL)));

	}
	
	public void postInit(){
	}
	
}
