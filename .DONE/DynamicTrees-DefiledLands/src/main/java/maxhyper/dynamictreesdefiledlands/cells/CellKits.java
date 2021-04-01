package maxhyper.dynamictreesdefiledlands.cells;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.CellNormal;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dynamictreesdefiledlands.DynamicTreesDefiledLands;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CellKits {
	
	public static void init() {
		new CellKits();
	}
	
	public CellKits() {
		TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesDefiledLands.MODID, "sparse"), sparse);
	}

	private final ICellKit sparse = new ICellKit() {

		private final ICell sparseBranch = new CellSparseBranch();
		private final ICell sparseLeaves = new CellNormal(1);

		private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {0x0211});

		@Override
		public ICell getCellForLeaves(int hydro) {
			return hydro > 0 ? sparseLeaves : CellNull.NULLCELL;
		}

		@Override
		public ICell getCellForBranch(int radius, int meta) {
			return radius == 1 ? sparseBranch : CellNull.NULLCELL;
		}

		@Override
		public SimpleVoxmap getLeafCluster() {
			return sparseCluster;
		}

		@Override
		public ICellSolver getCellSolver() {
			return solver;
		}

		@Override
		public int getDefaultHydration() {
			return 1;
		}

	};

	public static final SimpleVoxmap sparseCluster = new SimpleVoxmap(3, 2, 3, new byte[] {
			0, 1, 0,
			1, 0, 1,
			0, 1, 0,

			0, 0, 0,
			0, 1, 0,
			0, 0, 0,
	}).setCenter(new BlockPos(1, 0, 1));
	
}
