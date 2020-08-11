package maxhyper.dynamictreesforestry.crafting;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeSaplingToSeed extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack bucket = ItemStack.EMPTY;
        ItemStack sapling = ItemStack.EMPTY;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack slotStack = inv.getStackInSlot(j);

            if (!slotStack.isEmpty()) {
                if (!bucket.isEmpty() && !sapling.isEmpty()){
                    return false;
                }
                if (slotStack.getItem() == ModItems.dirtBucket) {
                    if (!bucket.isEmpty()) {
                        return false;
                    }

                    bucket = slotStack;
                }
                else if (slotStack.getItem() == ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("forestry","sapling"))) {
                    if (!sapling.isEmpty()) {
                        return false;
                    }

                    sapling = slotStack;
                } else {
                    return false;
                }
            }
        }

        return !bucket.isEmpty() && !sapling.isEmpty();
    }

    //		setUpSeedRecipes("spruce","forestry.treeSpruce");
//		setUpSeedRecipes("birch","forestry.treeBirch");
//		setUpSeedRecipes("jungle","forestry.treeJungle");
//		setUpSeedRecipes("darkOak","forestry.treeDarkOak");
//		setUpSeedRecipes("acacia","forestry.treeAcaciaVanilla");

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack sapling = ItemStack.EMPTY;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack slotStack = inv.getStackInSlot(j);
            if (!slotStack.isEmpty()) {
                if (slotStack.getItem() == ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("forestry","sapling"))) {
                    sapling = slotStack;
                }
            }
        }

        ItemStack seedStack = ItemStack.EMPTY;
        if (sapling.hasTagCompound()){
            NBTTagCompound saplingNBT;
            saplingNBT = sapling.getTagCompound();
            assert saplingNBT != null;

            NBTTagList genomes = saplingNBT.getCompoundTag("Genome").getTagList("Chromosomes", 10);
            String genome = genomes.getCompoundTagAt(0).getString("UID0"); //primary genome is always in slot 0
            String species = ModContent.getTreeIDfromUID(genome);
            if (!species.equals("giantSequoia")){
                seedStack = new ItemStack(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, species)).getSeed());
                seedStack.setTagCompound(saplingNBT);
            }
        } else {
            seedStack = new ItemStack(TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesForestry.MODID, "oak")).getSeed());
        }

        return seedStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
