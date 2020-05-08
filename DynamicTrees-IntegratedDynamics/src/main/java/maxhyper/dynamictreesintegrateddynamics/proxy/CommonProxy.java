package maxhyper.dynamictreesintegrateddynamics.proxy;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesintegrateddynamics.DynamicTreesIntegratedDynamics;
import maxhyper.dynamictreesintegrateddynamics.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreesintegrateddynamics.dropcreators.DropCreatorResin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.cyclops.integrateddynamics.GeneralConfig;
import org.cyclops.integrateddynamics.item.ItemCrystalizedMenrilChunkConfig;
import org.cyclops.integrateddynamics.item.ItemMenrilBerriesConfig;

public class CommonProxy {
	
	public void preInit() {

			GeneralConfig.wildMenrilTreeChance = 0;

	}
	
	public void init() {

			Species menril = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, "menril"));

			menril.addDropCreator(new DropCreatorFruit(new ItemStack(ItemMenrilBerriesConfig._instance.getItemInstance())).setRarity(0.25f));
			menril.addDropCreator(new DropCreatorResin(new ItemStack(ItemCrystalizedMenrilChunkConfig._instance.getItemInstance(), 2)));

	}
	
	public void postInit() {
	}
	
}
