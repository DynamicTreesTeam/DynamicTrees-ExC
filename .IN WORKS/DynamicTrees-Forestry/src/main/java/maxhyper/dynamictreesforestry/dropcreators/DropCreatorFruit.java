package maxhyper.dynamictreesforestry.dropcreators;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesforestry.DynamicTreesForestry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

//public class DropCreatorFruit extends DropCreator {
//
//	private final Item fruit;
//	private final int fruitMeta;
//	private float rarity = 1f;
//
//	public DropCreatorFruit(ItemStack fruitStack1) {
//		super(new ResourceLocation(DynamicTreesForestry.MODID, fruitStack1.getItem().getRegistryName().getResourcePath()));
//		this.fruit = fruitStack1.getItem();
//		this.fruitMeta = fruitStack1.getMetadata();
//	}
//
//	public DropCreatorFruit setRarity(float rarity) {
//		this.rarity = rarity;
//		return this;
//	}
//
//	@Override
//	public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
//		int chance = (int) (200 / rarity);
//		if (fortune > 0) {
//			chance -= 10 << fortune;
//			if (chance < 40) {
//				chance = 40;
//			}
//		}
//
//		if (random.nextInt(chance) == 0) {
//			dropList.add(new ItemStack(fruit, 1, fruitMeta));
//		}
//		return dropList;
//	}
//}
