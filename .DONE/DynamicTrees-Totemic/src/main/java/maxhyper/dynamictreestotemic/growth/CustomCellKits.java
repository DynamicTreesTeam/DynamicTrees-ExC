package maxhyper.dynamictreestotemic.growth;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.*;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dynamictreestotemic.DynamicTreesTotemic;
import net.minecraft.util.ResourceLocation;

public class CustomCellKits extends CellKits {

    public static void preInit() {
        new CustomCellKits();
    }

    public CustomCellKits (){
        super();
        TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesTotemic.MODID, "redCedar"), redCedar);
    }

    private final ICellKit redCedar = new ICellKit() {

        private final ICell normalCells[] = {
                CellNull.NULLCELL,
                new CellNormal(1),
                new CellNormal(2),
                new CellNormal(3),
                new CellNormal(4),
                new CellNormal(5),
                new CellNormal(6),
                new CellNormal(7)
        };

        /** Typical branch with hydration 5 */
        private final ICell branchCell = new CellNormal(5);

        private final ICell coniferTopBranch = new CellConiferTopBranch();

        private final BasicSolver deciduousSolver = new BasicSolver(new short[]{0x0514, 0x0423, 0x0322, 0x0411, 0x0311, 0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return normalCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if(meta == CellMetadata.CONIFERTOP) {
                return coniferTopBranch;
            }
            return radius == 1 ? branchCell : CellNull.NULLCELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return LeafClusters.deciduous;
        }

        @Override
        public ICellSolver getCellSolver() {
            return deciduousSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

}
