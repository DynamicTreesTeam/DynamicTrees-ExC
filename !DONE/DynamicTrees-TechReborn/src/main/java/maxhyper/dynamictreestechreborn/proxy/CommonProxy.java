package maxhyper.dynamictreestechreborn.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestechreborn.DynamicTreesTechReborn;
import maxhyper.dynamictreestechreborn.dropcreators.DropCreatorResin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import techreborn.init.ModItems;

public class CommonProxy {
	
	public void preInit() {
	}
	
	public void init() {
		if (Loader.isModLoaded("techreborn")) {
			TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTechReborn.MODID, "rubber")).
					addDropCreator(new DropCreatorResin(new ItemStack(ModItems.PARTS, 1, 31)));
		}
	}
	
	public void postInit() {
	}
	
}
