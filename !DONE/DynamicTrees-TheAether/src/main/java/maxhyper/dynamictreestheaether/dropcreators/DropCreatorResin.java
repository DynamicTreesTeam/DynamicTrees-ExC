package maxhyper.dynamictreestheaether.dropcreators;

import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
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
	private int dropCount = 1;

	public DropCreatorResin(ItemStack resinStack) {
		super(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, resinStack.getItem().getRegistryName().getResourcePath()));
		this.resin = resinStack.getItem();
		this.resinMeta = resinStack.getMetadata();
		this.dropCount = resinStack.getCount();
	}

	@Override
	public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
		return getLogDrops(world, species, breakPos, random, dropList, volume);
	}

	protected List<ItemStack> getLogDrops(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
			dropList.add(new ItemStack(resin, (random.nextInt(1 + (int) volume * dropCount*2)), resinMeta));
			return dropList;
	}

}
