package maxhyper.dynamictreesforestry.crafting;

import com.ferreusveritas.dynamictrees.ModItems;
import forestry.api.arboriculture.TreeManager;
import forestry.api.genetics.IIndividual;
import maxhyper.dynamictreesforestry.ModContent;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Objects;

public class RecipeFruitToSapling extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack bucket = ItemStack.EMPTY;
        ItemStack fruit = ItemStack.EMPTY;
        ItemStack boneMeal = ItemStack.EMPTY;

        boolean needsBonemeal = false;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack slotStack = inv.getStackInSlot(j);

            if (!slotStack.isEmpty()) {
                if (!bucket.isEmpty() && !fruit.isEmpty() && !boneMeal.isEmpty()){
                    return false;
                }

                if (slotStack.getItem() == ModItems.dirtBucket) { //found bucket
                    if (!bucket.isEmpty()) {
                        return false;
                    }

                    bucket = slotStack;

                } else if (Objects.requireNonNull(slotStack.getItem().getRegistryName()).getResourceDomain().equals("forestry") && //found fruit
                        Objects.requireNonNull(slotStack.getItem().getRegistryName()).getResourcePath().equals("fruits")) {
                    if (!fruit.isEmpty()) {
                        return false;
                    }
                    if (slotStack.getMetadata() == 1 || slotStack.getMetadata() == 2){
                        needsBonemeal = true;
                    } else {
                        needsBonemeal = false;
                    }
                    fruit = slotStack;

                } else if (slotStack.getItem() == Items.DYE && slotStack.getMetadata() == 15){ //found bonemeal
                    if (!boneMeal.isEmpty()){
                        return false;
                    }
                    boneMeal = slotStack;

                } else {
                    return false;
                }
            }
        }
        if ((needsBonemeal && boneMeal.isEmpty()) || (!needsBonemeal && !boneMeal.isEmpty())){
            return false;
        }
        return !bucket.isEmpty() && !fruit.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack fruit = ItemStack.EMPTY;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack slotStack = inv.getStackInSlot(j);
            if (Objects.requireNonNull(slotStack.getItem().getRegistryName()).getResourceDomain().equals("forestry") && //found fruit
                    Objects.requireNonNull(slotStack.getItem().getRegistryName()).getResourcePath().equals("fruits")) {
                fruit = slotStack;
            }
        }

        Item finalSapling = ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry","sapling"));
        assert finalSapling != null;

        ItemStack sapling = new ItemStack(finalSapling);

        if (fruit.hasTagCompound()){
            NBTTagCompound seedNBT = fruit.getTagCompound();
            sapling.setTagCompound(seedNBT);
        } else {
            String species;
            switch (fruit.getMetadata()){
                case 0:
                    species = "cherry";
                    break;
                case 1:
                    species = "walnut";
                    break;
                case 2:
                    species = "chestnut";
                    break;
                case 3:
                    species = "lemon";
                    break;
                case 4:
                    species = "plum";
                    break;
                case 5:
                    species = "palm";
                    break;
                case 6:
                    species = "papaya";
                    break;
                default:
                    return ItemStack.EMPTY;
            }
            String genome = ModContent.getTreeUIDfromID(species);
            NBTTagCompound finalNBT = new NBTTagCompound();
            assert TreeManager.treeRoot != null;
            for (IIndividual individual : TreeManager.treeRoot.getIndividualTemplates()) {
                //System.out.println(individual.getGenome().getPrimary().toString().equals(genome));
                if (individual.getGenome().getPrimary().toString().equals(genome)){
                    NBTTagCompound genomeNBT = new NBTTagCompound();
                    genomeNBT = individual.getGenome().writeToNBT(genomeNBT);
                    finalNBT = new NBTTagCompound();
                    finalNBT.setTag("Genome", genomeNBT);
                    finalNBT.setBoolean("IsAnalyzed", false);
                    break;
                }
            }
            sapling.setTagCompound(finalNBT);
        }

        return sapling;
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
