package maxhyper.dynamictreesforbiddenarcanus.proxy;


import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.stal111.forbidden_arcanus.init.ModItems;
import com.stal111.forbidden_arcanus.world.feature.WorldGenCherrywoodTree;
import com.stal111.forbidden_arcanus.world.feature.WorldGenMysterywoodTree;
import com.stal111.forbidden_arcanus.world.gen.WorldGen;
import maxhyper.dynamictreesforbiddenarcanus.DynamicTreesForbiddenArcanus;
import maxhyper.dynamictreesforbiddenarcanus.ModContent;
import maxhyper.dynamictreesforbiddenarcanus.dropcreators.DropCreatorFruit;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.Set;

public class CommonProxy {
	
	public void preInit() {
	}
	
	public void init() {
		ItemStack cherry = new ItemStack(ModItems.cherry_peach);
		TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForbiddenArcanus.MODID, "cherry"))
				.addDropCreator(new DropCreatorFruit(cherry));
		ModContent.blockCherry.setDroppedItem(cherry);
	}
	
	public void postInit() {
		if (ModConfigs.worldGen) {
			//This disables worldgen for cherrywood and mysterywood trees.
			try {
				Field worldGenerators = GameRegistry.class.getDeclaredField("worldGenerators");
				worldGenerators.setAccessible(true);

				Set<IWorldGenerator> worldGeneratorsAccessible = (Set<IWorldGenerator>) worldGenerators.get(GameRegistry.class);

				for (IWorldGenerator generator : worldGeneratorsAccessible){
					if (generator instanceof WorldGen){
						((WorldGen) generator).CHERRYWOOD_TREE = new WorldGenCherrywoodTree(true){
							@Override public boolean generate(World worldIn, Random rand, BlockPos position) { return false; }
						};
					((WorldGen) generator).MYSTERYWOOD_TREE = new WorldGenMysterywoodTree(true){
						@Override public boolean generate(World worldIn, Random rand, BlockPos position) { return false; }
					};
					}
				}

			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
