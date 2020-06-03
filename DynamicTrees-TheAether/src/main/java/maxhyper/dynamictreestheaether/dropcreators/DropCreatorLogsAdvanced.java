package maxhyper.dynamictreestheaether.dropcreators;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.systems.dropcreators.DropCreator;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class DropCreatorLogsAdvanced extends DropCreator {

	public DropCreatorLogsAdvanced() {
		super(new ResourceLocation(ModConstants.MODID, "logs"));
	}

	@Override
	public List<ItemStack> getLogsDrop(World world, Species species, BlockPos breakPos, Random random, List<ItemStack> dropList, float volume) {
		Species.LogsAndSticks las = species.getLogsAndSticks(volume);

		int numLogs = las.logs;
		while(numLogs > 0) {
			dropList.add(species.getFamily().getPrimitiveLogItemStack(numLogs >= 64 ? 64 : numLogs));
			numLogs -= 64;
		}
		int numSticks = las.sticks;
		if(numSticks > 0) {
			dropList.add(species.getFamily().getStick(numSticks));
		}
		return dropList;
	}

}
