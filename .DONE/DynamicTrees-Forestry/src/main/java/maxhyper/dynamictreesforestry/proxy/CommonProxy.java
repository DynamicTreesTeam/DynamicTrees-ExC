package maxhyper.dynamictreesforestry.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.dropcreators.DropCreatorOtherSeed;
import maxhyper.dynamictreesforestry.growth.CustomCellKits;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class CommonProxy {
	
	public void preInit() {
		CustomCellKits.preInit();
	}
	
	public void init() {

		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "oak"))
				.addDropCreator(new DropCreatorOtherSeed(new ItemStack(Items.APPLE, 1, 0)));

		ItemStack cherry = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))), 1, 0);
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "cherry"))
				.addDropCreator(new DropCreatorOtherSeed(cherry));
		ModContent.cherryFruit.setDroppedItem(cherry);

		ItemStack walnut = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))), 1, 1);
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "walnut"))
				.addDropCreator(new DropCreatorOtherSeed(walnut));
		ModContent.walnutFruit.setDroppedItem(walnut);

		ItemStack chestnut = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))), 1, 2);
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "chestnut"))
				.addDropCreator(new DropCreatorOtherSeed(chestnut));
		ModContent.chestnutFruit.setDroppedItem(chestnut);

		ItemStack lemon = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))), 1, 3);
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "lemon"))
				.addDropCreator(new DropCreatorOtherSeed(lemon));
		ModContent.lemonFruit.setDroppedItem(lemon);

		ItemStack plum = new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("forestry", "fruits"))), 1, 4);
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "plum"))
				.addDropCreator(new DropCreatorOtherSeed(plum));
		ModContent.plumFruit.setDroppedItem(plum);

	}
	
	public void postInit(){
	}
	
}
