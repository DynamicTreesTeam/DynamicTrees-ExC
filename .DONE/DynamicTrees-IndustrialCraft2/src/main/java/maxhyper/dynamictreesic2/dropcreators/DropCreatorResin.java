package maxhyper.dynamictreesic2.dropcreators;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesic2.DynamicTreesIC2;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class DropCreatorResin extends DropCreator {

	private final Item resin;
	private final int resinMeta;
	private float dropCount;

	public DropCreatorResin(ItemStack resinStack, float dropCount) {
		super(new ResourceLocation(DynamicTreesIC2.MODID, resinStack.getItem().getRegistryName().getResourcePath()));
		this.resin = resinStack.getItem();
		this.resinMeta = resinStack.getMetadata();
		this.dropCount = dropCount;
	}

	@Override
	public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
		return getLogDrops(world, species, breakPos, random, dropList, volume);
	}

	protected List<ItemStack> getLogDrops(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
		dropList.add(new ItemStack(resin, (int) (volume * dropCount), resinMeta));
		return dropList;
	}

}
