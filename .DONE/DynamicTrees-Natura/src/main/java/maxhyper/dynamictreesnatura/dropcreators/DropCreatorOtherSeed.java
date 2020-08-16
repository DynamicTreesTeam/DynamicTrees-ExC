package maxhyper.dynamictreesnatura.dropcreators;

import com.ferreusveritas.dynamictrees.ModConfigs;
import com.ferreusveritas.dynamictrees.event.SeedVoluntaryDropEvent;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dynamictreesnatura.DynamicTreesNatura;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DropCreatorOtherSeed extends DropCreator {

	private final Item seed;
	private final int seedMeta;
	private float rarity = 1f;

	public DropCreatorOtherSeed(ItemStack seedStack) {
		super(new ResourceLocation(DynamicTreesNatura.MODID, Objects.requireNonNull(seedStack.getItem().getRegistryName()).getResourcePath()));
		this.seed = seedStack.getItem();
		this.seedMeta = seedStack.getMetadata();
	}

	public DropCreatorOtherSeed setRarity(float rarity) {
		this.rarity = rarity;
		return this;
	}

	//Provided for customization via override
	protected float getHarvestRarity() {
		return rarity;
	}

	//Provided for customization via override
	protected float getVoluntaryRarity() {
		return rarity;
	}

	//Provided for customization via override
	protected float getLeavesRarity() {
		return rarity;
	}

	@Override
	public List<ItemStack> getHarvestDrop(World world, Species species, BlockPos leafPos, Random random, List<ItemStack> dropList, int soilLife, int fortune) {
		if((1 / 64f) * getHarvestRarity() > random.nextFloat()) {//1 in 64 chance to drop a seed on destruction..
			dropList.add(new ItemStack(seed, 1, seedMeta));
		}
		return dropList;
	}

	@Override
	public List<ItemStack> getVoluntaryDrop(World world, Species species, BlockPos rootPos, Random random, List<ItemStack> dropList, int soilLife) {
		if(getVoluntaryRarity() * ModConfigs.seedDropRate > random.nextFloat()) {
			dropList.add(new ItemStack(seed, 1, seedMeta));
			SeedVoluntaryDropEvent seedDropEvent = new SeedVoluntaryDropEvent(world, rootPos, species, dropList);
			MinecraftForge.EVENT_BUS.post(seedDropEvent);
			if(seedDropEvent.isCanceled()) {
				dropList.clear();
			}
		}
		return dropList;
	}

	@Override
	public List<ItemStack> getLeavesDrop(IBlockAccess access, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, int fortune) {
		int chance = (int)(20/rarity); //See BlockLeaves#getSaplingDropChance(state);
		//Hokey fortune stuff here to match Vanilla logic.
		if (fortune > 0) {
			chance -= 2 << fortune;
			if (chance < 10) {
				chance = 10;
			}
		}

		if(random.nextInt((int) (chance / getLeavesRarity())) == 0) {
			dropList.add(new ItemStack(seed, 1, seedMeta));
		}

		return dropList;
	}
}