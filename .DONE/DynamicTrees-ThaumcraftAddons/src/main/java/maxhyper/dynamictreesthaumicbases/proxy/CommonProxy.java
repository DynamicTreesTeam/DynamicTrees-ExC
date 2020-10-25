package maxhyper.dynamictreesthaumicbases.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreesthaumicbases.DynamicTreesThaumcraftAddons;
import maxhyper.dynamictreesthaumicbases.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreesthaumicbases.event.RecipeHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class CommonProxy {
	
	public void preInit() {
	}

	public void init() {

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "goldenoak")).
				addDropCreator(new DropCreatorFruit(new ItemStack(Items.GOLDEN_APPLE)).setRarity(0.02f));

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "enderoak")).
				addDropCreator(new DropCreatorFruit(new ItemStack(Items.ENDER_PEARL)).setRarity(0.02f));

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "hellishoak")).
				addDropCreator(new DropCreatorFruit(new ItemStack(Items.MAGMA_CREAM)).setRarity(0.02f));

	}
	
	public void postInit() {
		if (Loader.isModLoaded("thaumicbases")) {
			RecipeHandler.TCInfusion();
		}
	}
	
}
