package maxhyper.dynamictreesnatura.feautregen;

import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.CellAcaciaLeaf;
import com.ferreusveritas.dynamictrees.cells.CellKits;
import com.ferreusveritas.dynamictrees.cells.LeafClusters;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CustomCellKits extends CellKits {
    public CustomCellKits (){
        super();
        TreeRegistry.registerCellKit(new ResourceLocation(ModConstants.MODID, "bloodwood"), bloodwood);
    }
    private final ICellKit bloodwood = new ICellKit() {

        private final ICell bloodwoodBranch = new ICell() {
            @Override
            public int getValue() {
                return 5;
            }

            final int map[] = {3, 0, 5, 5, 5, 5};

            @Override
            public int getValueFromSide(EnumFacing side) {
                return map[side.ordinal()];
            }

        };

        private final ICell acaciaLeafCells[] = {
                CellNull.NULLCELL,
                new CellAcaciaLeaf(1),
                new CellAcaciaLeaf(2),
                new CellAcaciaLeaf(3),
                new CellAcaciaLeaf(4)
        };

        private final BasicSolver bloodwoodSolver = new BasicSolver(new short[]{0x0514, 0x0423, 0x0412, 0x0312, 0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return acaciaLeafCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? bloodwoodBranch : CellNull.NULLCELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return LeafClusters.darkoak;
        }

        @Override
        public ICellSolver getCellSolver() {
            return bloodwoodSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };
}
