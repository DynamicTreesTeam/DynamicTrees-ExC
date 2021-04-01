package maxhyper.dynamictreesdefiledlands.cells;

import com.ferreusveritas.dynamictrees.api.cells.ICell;
import net.minecraft.util.EnumFacing;

public class CellSparseBranch implements ICell {
	
	@Override
	public int getValue() {
		return 2;
	}

	static final int[] map = {0, 2, 2, 2, 2, 2};
	
	@Override
	public int getValueFromSide(EnumFacing side) {
		return map[side.ordinal()];
	}
	
}
