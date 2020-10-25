package maxhyper.dynamictreesthaumicbases.event;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.rumaruka.thaumicbases.utils.TBConfig;
import maxhyper.dynamictreesthaumicbases.DynamicTreesThaumcraftAddons;
import com.rumaruka.thaumicbases.init.TBBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

public class RecipeHandler  {
    @Optional.Method(modid = "thaumicbases")
    public static void TCInfusion (){
        ItemStack oakSeed = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "oak")).getSeedStack(1);
        ItemStack goldenOakSeed = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "goldenOak")).getSeedStack(1);
        ItemStack enderOakSeed = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "enderOak")).getSeedStack(1);
        ItemStack hellishOakSeed = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "hellishOak")).getSeedStack(1);
        ItemStack cactusSeed = TreeRegistry.findSpecies(new ResourceLocation(ModConstants.MODID, "cactus")).getSeedStack(1);
        ItemStack rainbowCactusSeed = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesThaumcraftAddons.MODID, "rainbowcactus")).getSeedStack(1);

        InfusionRecipe goldenSeed = new InfusionRecipe("TB.TREE", goldenOakSeed,4,new AspectList().add(Aspect.PLANT,48).add(Aspect.DESIRE,64), oakSeed, new ItemStack(Items.GOLDEN_APPLE),new ItemStack(Items.GOLDEN_APPLE),new ItemStack(Items.APPLE),new ItemStack(Items.APPLE));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("TB.goldenSapling"),goldenSeed);
        InfusionRecipe netherSeed = new InfusionRecipe("TB.TREE2", hellishOakSeed,4,new AspectList().add(Aspect.PLANT,48).add(Aspect.FIRE,64), oakSeed, new ItemStack(Items.NETHER_WART),new ItemStack(Items.NETHER_WART),new ItemStack(Items.BLAZE_POWDER),new ItemStack(Items.BLAZE_POWDER));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("TB.netherSapling"),netherSeed);
        InfusionRecipe enderSeed = new InfusionRecipe("TB.TREE1", enderOakSeed,4,new AspectList().add(Aspect.PLANT,48).add(Aspect.AURA,64), oakSeed, new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.ENDER_EYE),new ItemStack(Items.ENDER_EYE));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("TB.enderSapling"),enderSeed);

        InfusionRecipe goldenSapling = new InfusionRecipe("TB.TREE", new ItemStack(TBBlocks.goldensapling),4,new AspectList().add(Aspect.PLANT,48).add(Aspect.DESIRE,64), new ItemStack(Blocks.SAPLING,1,0),new ItemStack(Items.GOLDEN_APPLE),new ItemStack(Items.GOLDEN_APPLE),new ItemStack(Items.APPLE),new ItemStack(Items.APPLE));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("TB.goldenSaplingOld"),goldenSapling);
        InfusionRecipe netherSapling = new InfusionRecipe("TB.TREE2", new ItemStack(TBBlocks.nethersapling),4,new AspectList().add(Aspect.PLANT,48).add(Aspect.FIRE,64), new ItemStack(Blocks.SAPLING,1,0),new ItemStack(Items.NETHER_WART),new ItemStack(Items.NETHER_WART),new ItemStack(Items.BLAZE_POWDER),new ItemStack(Items.BLAZE_POWDER));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("TB.netherSaplingOld"),netherSapling);
        InfusionRecipe enderSapling = new InfusionRecipe("TB.TREE1", new ItemStack(TBBlocks.endersapling),4,new AspectList().add(Aspect.PLANT,48).add(Aspect.AURA,64), new ItemStack(Blocks.SAPLING,1,0),new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.ENDER_PEARL),new ItemStack(Items.ENDER_EYE),new ItemStack(Items.ENDER_EYE));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation("TB.enderSaplingOld"),enderSapling);

        if(TBConfig.allowTobacco){
            CrucibleRecipe catusSeedToRCSeed= new CrucibleRecipe("TB.CACTUS",rainbowCactusSeed,cactusSeed,new AspectList().add(Aspect.PLANT,35).add(Aspect.MAGIC,15));
            ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("TB.catusToRC"),catusSeedToRCSeed);
            CrucibleRecipe catusToRC= new CrucibleRecipe("TB.CACTUS",new ItemStack(TBBlocks.rainbowcactus),new ItemStack(Blocks.CACTUS),new AspectList().add(Aspect.PLANT,35).add(Aspect.MAGIC,15));
            ThaumcraftApi.addCrucibleRecipe(new ResourceLocation("TB.catusToRCOld"),catusToRC);
        }
    }
}
