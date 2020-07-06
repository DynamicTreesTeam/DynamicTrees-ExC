package maxhyper.dynamictreesic2.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import ic2.core.item.type.MiscResourceType;
import ic2.core.ref.ItemName;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import maxhyper.dynamictreesic2.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreesic2.dropcreators.DropCreatorResin;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {
	
	public void preInit() {
	}

	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIC2.MODID, "rubberIC")).
				addDropCreator(new DropCreatorResin(ItemName.misc_resource.getItemStack(MiscResourceType.resin)));
	}
	
	public void postInit() {

	}
	
}
