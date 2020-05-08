package maxhyper.dynamictreesbl.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesbl.DynamicTreesBL;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.dropcreators.DropCreatorResin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techreborn.init.ModItems;
import thebetweenlands.common.registries.ItemRegistry;


public class CommonProxy {
	
	public void preInit() {
	}
	
	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesBL.MODID, "rubber")).
				addDropCreator(new DropCreatorResin(new ItemStack(ItemRegistry.SAP_BALL)));

	}
	
	public void postInit(){
	}
	
}
