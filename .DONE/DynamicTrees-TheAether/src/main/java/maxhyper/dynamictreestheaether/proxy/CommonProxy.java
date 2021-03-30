package maxhyper.dynamictreestheaether.proxy;


import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.items.ItemsAether;
import maxhyper.dynamictreestheaether.DynamicTreesTheAether;
import maxhyper.dynamictreestheaether.ModConfigs;
import maxhyper.dynamictreestheaether.ModContent;
import maxhyper.dynamictreestheaether.event.EventListenerAether;
import maxhyper.dynamictreestheaether.growth.CustomCellKits;
import maxhyper.dynamictreestheaether.worldgen.WorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		CustomCellKits.preInit();
		if(WorldGenRegistry.isWorldGenEnabled()) {
			
			GameRegistry.registerWorldGenerator(new WorldGen(), 21);
		}
	}
	
	public void init() {
		ModContent.blockWhiteApple.setDroppedItem(new ItemStack(ItemsAether.white_apple));

		// Register sapling replacements.
		registerSaplingReplacement(BlocksAether.golden_oak_sapling, "goldenoak");
		registerSaplingReplacement(BlocksAether.skyroot_sapling, "skyroot");
	}

	private static void registerSaplingReplacement(final Block saplingBlock, final String speciesName) {
		TreeRegistry.registerSaplingReplacer(saplingBlock.getDefaultState(), TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTheAether.MODID, speciesName)));
	}

	public void postInit() { }

}
