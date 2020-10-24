package maxhyper.dynamictreesintegrateddynamics.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesintegrateddynamics.DynamicTreesIntegratedDynamics;
import maxhyper.dynamictreesintegrateddynamics.ModConfigs;
import maxhyper.dynamictreesintegrateddynamics.ModContent;
import maxhyper.dynamictreesintegrateddynamics.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreesintegrateddynamics.dropcreators.DropCreatorResin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.cyclops.integrateddynamics.GeneralConfig;
import org.cyclops.integrateddynamics.item.*;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModConfigs.preInit(event);
		GeneralConfig.wildMenrilTreeChance = 0;
	}

	public void init() {
		Species menril = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesIntegratedDynamics.MODID, "menril"));
		menril.addDropCreator(new DropCreatorFruit(new ItemStack(ItemMenrilBerriesConfig._instance.getItemInstance())).setRarity(ModConfigs.menrilBerriesRarity));
		menril.addDropCreator(new DropCreatorResin(new ItemStack(ItemCrystalizedMenrilChunkConfig._instance.getItemInstance()), ModConfigs.menrilResinMultiplier));
		ModContent.blockMenrilBerries.setDroppedItem(new ItemStack(ItemMenrilBerriesConfig._instance.getItemInstance()));
	}

	public void postInit() {
	}

}
