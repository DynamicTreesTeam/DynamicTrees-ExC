package maxhyper.dynamictreestbl.proxy;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import maxhyper.dynamictreestbl.ModConfigs;
import maxhyper.dynamictreestbl.compat.RegistryReplacements;
import maxhyper.dynamictreestbl.growth.CustomCellKits;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thebetweenlands.common.registries.ItemRegistry;
import thebetweenlands.common.world.gen.biome.decorator.DecorationHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModConfigs.preInit(event);
		CustomCellKits.preInit();
		System.out.println("The following warnings are intentional overrides made by Dynamic Trees for The Between Lands. Please do not report this as a bug.");
		System.out.println("########");
		RegistryReplacements.replaceWaters();
		//RegistryReplacements.replaceSludgyDirt();
		System.out.println("########");
		System.out.println("End of intentional override warnings");
	}

	public static final WorldGenerator worldGeneratorDummy = new WorldGenerator() {
		@Override public boolean generate(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos position) { return false; }
	};

	public void init() {
		try {
			List<String> treeFields = Arrays.asList("GEN_SAP_TREE","GEN_RUBBER_TREE","GEN_NIBBLETWIG_TREE","GEN_HEARTHGROVE_TREE");
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			for (Field field : DecorationHelper.class.getFields()){
				if (treeFields.contains(field.getName())){
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
					field.set(DecorationHelper.class, worldGeneratorDummy);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public void postInit(){
	}
	
}
