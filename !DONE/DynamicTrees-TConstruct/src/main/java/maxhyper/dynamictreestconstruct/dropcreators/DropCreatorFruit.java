package maxhyper.dynamictreestconstruct.dropcreators;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;

import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DropCreatorFruit extends DropCreator {
	
	private final Item fruit1, fruit2;
	private final int fruitMeta1, fruitMeta2;
	private float rarity = 1f;
	
	public DropCreatorFruit(ItemStack fruitStack1, ItemStack fruitStack2) {
		super(new ResourceLocation(DynamicTreesTConstruct.MODID, fruitStack1.getItem().getRegistryName().getResourcePath()));
		this.fruit1 = fruitStack1.getItem();
		this.fruit2 = fruitStack2.getItem();
		this.fruitMeta1 = fruitStack1.getMetadata();
		this.fruitMeta2 = fruitStack2.getMetadata();
	}
	public DropCreatorFruit(ItemStack fruitStack1) {
		super(new ResourceLocation(DynamicTreesTConstruct.MODID, fruitStack1.getItem().getRegistryName().getResourcePath()));
		this.fruit1 = this.fruit2 = fruitStack1.getItem();
		this.fruitMeta1 = this.fruitMeta2 = fruitStack1.getMetadata();
	}
	
	public DropCreatorFruit setRarity(float rarity) {
		this.rarity = rarity;
		return this;
	}
	
	@Override
	public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
		return getLeafDrops(access, species, breakPos, random, dropList, fortune, 4);
	}
	
	@Override
	public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
		return getLeafDrops(world, species, leafPos, random, dropList, fortune, 2);
	}
	
	protected List<ItemStack> getLeafDrops(IBlockAccess access, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int fortune, int baseChance) {
		int chance = (int) (baseChance / rarity);
		if (fortune > 0) {
			chance -= 10 << fortune;
			if (chance < 40) {
				chance = 40;
			}
		}
		
		if (random.nextInt(chance) == 0) {
			if (random.nextInt(3) == 0){
				dropList.add(new ItemStack(fruit1, 1, fruitMeta1));
			} else {
				dropList.add(new ItemStack(fruit2, 1, fruitMeta2));
			}

		}
		return dropList;
	}

}
