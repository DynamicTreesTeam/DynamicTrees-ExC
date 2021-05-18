package maxhyper.dynamictreesjurassicraft.growth;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.CellKits;
import com.ferreusveritas.dynamictrees.cells.CellNormal;
import com.ferreusveritas.dynamictrees.cells.LeafClusters;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dynamictreesjurassicraft.DynamicTreesJurassiCraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CustomCellKits extends CellKits {

    public static void preInit() {
        new CustomCellKits();
    }

    public CustomCellKits(){
        super();
        TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesJurassiCraft.MODID, "ginkgo"), ginkgo);
        TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesJurassiCraft.MODID, "calamites"), calamites);
    }

    private final ICellKit ginkgo = new ICellKit() {

        public final SimpleVoxmap ginkgoCluster = new SimpleVoxmap(3, 3, 3, new byte[] {
                //Layer 0 (Bottom)
                0, 1, 0,
                1, 2, 1,
                0, 1, 0,

                //Layer 1
                1, 2, 1,
                2, 0, 2,
                1, 2, 1,

                //Layer 2(Top)
                0, 1, 0,
                1, 2, 1,
                0, 1, 0,

        }).setCenter(new BlockPos(1, 1, 1));

        private final ICell[] normalCells = {
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

        private final BasicSolver deciduousSolver = new BasicSolver(new short[]{0x0512, 0x0221});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return normalCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? branchCell : CellNull.NULLCELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return ginkgoCluster;
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

    private final ICellKit calamites = new ICellKit() {

        public final SimpleVoxmap calamitesCluster = new SimpleVoxmap(3, 3, 3, new byte[] {
                //Layer 0 (Bottom)
                0, 0, 0,
                0, 1, 0,
                0, 0, 0,

                //Layer 1
                0, 1, 0,
                1, 0, 1,
                0, 1, 0,

                //Layer 2(Top)
                0, 0, 0,
                0, 1, 0,
                0, 0, 0,

        }).setCenter(new BlockPos(1, 1, 1));

        private final ICell[] normalCells = {
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

        /** hydrations for the sides of branches */
        private final ICell branchSideCell = new CellNormal(2);

        private final BasicSolver deciduousSolver = new BasicSolver(new short[]{0x0511, 0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return normalCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (radius == 1) return branchCell;
            if (radius == 2) return branchSideCell;
            return CellNull.NULLCELL;
        }

        @Override
        public ICellSolver getCellSolver() {
            return deciduousSolver;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return calamitesCluster;
        }

        @Override
        public int getDefaultHydration() {
            return 1;
        }
    };

}
