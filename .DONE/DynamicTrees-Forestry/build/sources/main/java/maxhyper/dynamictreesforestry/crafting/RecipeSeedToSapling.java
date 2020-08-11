package maxhyper.dynamictreesforestry.crafting;

import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.items.Seed;
import forestry.api.arboriculture.TreeManager;
import forestry.api.genetics.IIndividual;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import maxhyper.dynamictreesforestry.ModContent;
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

public class RecipeSeedToSapling extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack bucket = ItemStack.EMPTY;
        ItemStack seed = ItemStack.EMPTY;

        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack slotStack = inv.getStackInSlot(j);

            if (!slotStack.isEmpty()) {
                if (!bucket.isEmpty() && !seed.isEmpty()){
                    return false;
                }
                if (slotStack.getItem() == ModItems.dirtBucket) {
                    if (!bucket.isEmpty()) {
                        return false;
                    }

                    bucket = slotStack;
                }
                else if (slotStack.getItem() instanceof Seed &&
                        Objects.requireNonNull(slotStack.getItem().getRegistryName()).getResourceDomain().equals(
                                DynamicTreesForestry.MODID)) {
                    if (!seed.isEmpty()) {
                        return false;
                    }

                    seed = slotStack;
                } else {
                    return false;
                }
            }
        }

        return !bucket.isEmpty() && !seed.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack seed = ItemStack.EMPTY;
        for (int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack slotStack = inv.getStackInSlot(j);
            if (!slotStack.isEmpty() && slotStack.getItem() instanceof Seed &&
                    Objects.requireNonNull(slotStack.getItem().getRegistryName()).getResourceDomain().equals(
                            DynamicTreesForestry.MODID)) {
                seed = slotStack;
            }
        }

        Item finalSapling = ForgeRegistries.ITEMS.getValue(
                new ResourceLocation("forestry","sapling"));
        assert finalSapling != null;

        ItemStack sapling = new ItemStack(finalSapling);

        if (seed.hasTagCompound()){
            NBTTagCompound seedNBT = seed.getTagCompound();
            sapling.setTagCompound(seedNBT);
        } else {
            String species = Objects.requireNonNull(((Seed) seed.getItem()).getSpecies(seed).getRegistryName()).getResourcePath();
            System.out.println(species);
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
