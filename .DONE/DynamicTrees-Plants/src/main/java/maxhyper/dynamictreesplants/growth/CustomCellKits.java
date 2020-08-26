package maxhyper.dynamictreesplants.growth;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.*;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dynamictreesplants.DynamicTreesPlants;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class CustomCellKits extends CellKits {

    public static void preInit() {
        new CustomCellKits();
    }

    public CustomCellKits(){
        super();
        TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesPlants.MODID, "crystal"), crystal);
        TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesPlants.MODID, "nether"), nether);
    }

    private final ICellKit crystal = new ICellKit() {

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
        private final ICell coniferTopBranch = new CellConiferTopBranch();

        private final BasicSolver deciduousSolver = new BasicSolver(new short[]{0x0514, 0x0423, 0x0322, 0x0411, 0x0311, 0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return normalCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? coniferTopBranch : CellNull.NULLCELL;
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

    private final ICellKit nether = new ICellKit() {

        private final ICell acaciaBranch = new ICell() {
            @Override
            public int getValue() {
                return 5;
            }

            final int map[] = {2, 2, 5, 5, 5, 5};

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

        private final BasicSolver deciduousSolver = new BasicSolver(new short[]{0x0514, 0x0422, 0x0322, 0x0411, 0x0311, 0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return acaciaLeafCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? acaciaBranch : CellNull.NULLCELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return LeafClusters.acacia;
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
