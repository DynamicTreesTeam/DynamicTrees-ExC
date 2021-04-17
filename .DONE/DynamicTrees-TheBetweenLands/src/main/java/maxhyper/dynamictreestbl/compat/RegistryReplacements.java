package maxhyper.dynamictreestbl.compat;

import com.google.common.collect.BiMap;
import maxhyper.dynamictreestbl.blocks.BlockSludgyDirtOverride;
import maxhyper.dynamictreestbl.blocks.BlockSpreadingSludgyDirtOverride;
import maxhyper.dynamictreestbl.blocks.BlockStagnantWaterOverride;
import maxhyper.dynamictreestbl.blocks.BlockSwampWaterOverride;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import thebetweenlands.common.registries.BlockRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class RegistryReplacements {

    public static void replaceWaters(){
        BiMap<String, Fluid> fluidBlocksAccessible;
        try{
            //This replaces both swamp water and stagnant water blocks in TBL's Block Registry before they are added to forge's registry.
            final Block SWAMP_WATER = new BlockSwampWaterOverride(new ResourceLocation("thebetweenlands","swamp_water"));
            final Block STAGNANT_WATER = new BlockStagnantWaterOverride(new ResourceLocation("thebetweenlands","stagnant_water"));
            BlockRegistry.BLOCKS.remove(BlockRegistry.SWAMP_WATER);
            BlockRegistry.BLOCKS.add(SWAMP_WATER);
            BlockRegistry.BLOCKS.remove(BlockRegistry.STAGNANT_WATER);
            BlockRegistry.BLOCKS.add(STAGNANT_WATER);

            final Field swampWaterField = BlockRegistry.class.getDeclaredField("SWAMP_WATER");
            swampWaterField.setAccessible(true);
            final Field stagnantWaterField = BlockRegistry.class.getDeclaredField("STAGNANT_WATER");
            stagnantWaterField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt( swampWaterField, swampWaterField.getModifiers() & ~Modifier.FINAL );
            modifiersField.setInt( stagnantWaterField, stagnantWaterField.getModifiers() & ~Modifier.FINAL );

            swampWaterField.set(null, SWAMP_WATER);
            stagnantWaterField.set(null, STAGNANT_WATER);

            swampWaterField.setAccessible(false);
            stagnantWaterField.setAccessible(false);

            //////////////////////////////////////////

            //This replaces the fluid block in the stagnant water fluid to our block override
            final Field fluidsField = net.minecraftforge.fluids.FluidRegistry.class.getDeclaredField("fluids");
            fluidsField.setAccessible(true);

            fluidBlocksAccessible = (BiMap<String, Fluid>) fluidsField.get(net.minecraftforge.fluids.FluidRegistry.class);

            Fluid newStagnantWater = FluidRegistry.getFluid("stagnant_water");

            Field fluidBlockField = Fluid.class.getDeclaredField("block");
            fluidBlockField.setAccessible(true);

            fluidBlockField.set(newStagnantWater, STAGNANT_WATER);

            fluidBlocksAccessible.replace("stagnant_water", newStagnantWater);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void replaceSludgyDirt (){
        try {

            final Block SLUDGY_DIRT = new BlockSludgyDirtOverride(new ResourceLocation("thebetweenlands","sludgy_dirt"));
            final Block SPREADING_SLUDGY_DIRT = new BlockSpreadingSludgyDirtOverride(new ResourceLocation("thebetweenlands","spreading_sludgy_dirt"));
            BlockRegistry.BLOCKS.remove(BlockRegistry.SLUDGY_DIRT);
            BlockRegistry.BLOCKS.add(SLUDGY_DIRT);
            BlockRegistry.BLOCKS.remove(BlockRegistry.SPREADING_SLUDGY_DIRT);
            BlockRegistry.BLOCKS.add(SPREADING_SLUDGY_DIRT);

            final Field itemBlockField = ItemBlock.class.getDeclaredField("block");
            itemBlockField.setAccessible(true);

            final Field sludgyDirtField = BlockRegistry.class.getDeclaredField("SLUDGY_DIRT");
            sludgyDirtField.setAccessible(true);
            final Field spreadingSludgyDirtField = BlockRegistry.class.getDeclaredField("SPREADING_SLUDGY_DIRT");
            spreadingSludgyDirtField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt( sludgyDirtField, sludgyDirtField.getModifiers() & ~Modifier.FINAL );
            modifiersField.setInt( spreadingSludgyDirtField, spreadingSludgyDirtField.getModifiers() & ~Modifier.FINAL );
            modifiersField.setInt( itemBlockField, itemBlockField.getModifiers() & ~Modifier.FINAL );

            sludgyDirtField.set(null, SLUDGY_DIRT);
            spreadingSludgyDirtField.set(null, SPREADING_SLUDGY_DIRT);

            for (ItemBlock ib : BlockRegistry.ITEM_BLOCKS){
                if (ib.getRegistryName().getResourcePath().equals("sludgy_dirt")){
                    itemBlockField.set(ib, SLUDGY_DIRT);
                }
                if (ib.getRegistryName().getResourcePath().equals("spreading_sludgy_dirt")){
                    itemBlockField.set(ib, SPREADING_SLUDGY_DIRT);
                }
            }

            sludgyDirtField.setAccessible(false);
            spreadingSludgyDirtField.setAccessible(false);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
