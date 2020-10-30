package maxhyper.dynamictreestbl.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.compat.RegistryReplacements;
import maxhyper.dynamictreestbl.dropcreators.DropCreatorResin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thebetweenlands.common.registries.ItemRegistry;


public class CommonProxy {
	
	public void preInit() {
		System.out.println("The following warnings are intentional overrides made by Dynamic Trees for The Between Lands. Please do not report this as a bug.");
		System.out.println("########");
		RegistryReplacements.replaceWaters();
		RegistryReplacements.replaceSludgyDirt();
		System.out.println("########");
		System.out.println("End of intentional override warnings");
	}
	
	public void init() {
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTBL.MODID, "rubber")).
				addDropCreator(new DropCreatorResin(new ItemStack(ItemRegistry.SAP_BALL)));
	}
	
	public void postInit(){
	}
	
}
