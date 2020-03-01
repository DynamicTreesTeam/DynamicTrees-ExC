package maxhyper.dynamictreesexc.compat;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;
import ic2.api.item.IC2Items;
import ic2.core.item.type.MiscResourceType;
import ic2.core.ref.ItemName;
import maxhyper.dynamictreesexc.DynamicTreesExC;
import maxhyper.dynamictreesexc.dropcreators.DropCreatorFruit;
import maxhyper.dynamictreesexc.dropcreators.DropCreatorResin;
import maxhyper.dynamictreesexc.event.RecipeHandler;
import maxhyper.dynamictreesexc.featuregen.SlimeGrowthLogic;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import org.cyclops.integrateddynamics.block.BlockMenrilSaplingConfig;
import org.cyclops.integrateddynamics.item.ItemCrystalizedMenrilChunkConfig;
import org.cyclops.integrateddynamics.item.ItemMenrilBerriesConfig;
import slimeknights.tconstruct.shared.TinkerCommons;
import techreborn.init.ModItems;

public class CompatInit {

    //
    // PRE INIT
    //
    @Optional.Method(modid = "tconstruct")
    public static void preInitTinkersConstruct(){
        TreeRegistry.registerGrowthLogicKit(new ResourceLocation(ModConstants.MODID, "slime"), new SlimeGrowthLogic());
    }

    //
    // INIT
    //
    @Optional.Method(modid = "tconstruct")
    public static void initTinkersConstruct(){
        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "slimeBlue")).
                addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallBlue).setRarity(0.05f));

        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "slimePurple")).
                addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallPurple).setRarity(0.05f));

        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "slimeMagma")).
                addDropCreator(new DropCreatorFruit(TinkerCommons.matSlimeBallMagma).setRarity(0.05f));
    }
    @Optional.Method(modid = "integrateddynamics")
    public static void initIntegratedDynamics(){
        Species menril = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "menril"));

        menril.addDropCreator(new DropCreatorFruit(new ItemStack(ItemMenrilBerriesConfig._instance.getItemInstance())).setRarity(0.25f));
        menril.addDropCreator(new DropCreatorResin(new ItemStack(ItemCrystalizedMenrilChunkConfig._instance.getItemInstance(), 2)));
    }
    @Optional.Method(modid = "techreborn")
    public static void initTechReborn (){
        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "rubber")).
                addDropCreator(new DropCreatorResin(new ItemStack(ModItems.PARTS, 1, 31)));
    }
    @Optional.Method(modid = "ic2")
    public static void initIC2 (){
        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "rubberIC")).
                addDropCreator(new DropCreatorResin(ItemName.misc_resource.getItemStack(MiscResourceType.resin)));
    }
    @Optional.Method(modid = "thaumicbases")
    public static void initThaumicBases(){
        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "goldenoak")).
                addDropCreator(new DropCreatorFruit(new ItemStack(Items.GOLDEN_APPLE)).setRarity(0.02f));

        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "enderoak")).
                addDropCreator(new DropCreatorFruit(new ItemStack(Items.ENDER_PEARL)).setRarity(0.02f));

        TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesExC.MODID, "hellishoak")).
                addDropCreator(new DropCreatorFruit(new ItemStack(Items.MAGMA_CREAM)).setRarity(0.02f));

    }

    //
    // POST INIT
    //
    @Optional.Method(modid = "thaumicbases")
    public static void postInitThaumicBases(){
        RecipeHandler.TCInfusion();
    }
}
