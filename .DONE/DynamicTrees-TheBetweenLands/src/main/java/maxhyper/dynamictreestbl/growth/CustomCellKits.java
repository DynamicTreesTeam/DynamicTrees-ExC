package maxhyper.dynamictreestbl.growth;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.CellKits;
import com.ferreusveritas.dynamictrees.cells.CellNormal;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dynamictreestbl.DynamicTreesTBL;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CustomCellKits extends CellKits {

    public static void preInit() {
        new CustomCellKits();
    }

    public CustomCellKits(){
        super();
        TreeRegistry.registerCellKit(new ResourceLocation(DynamicTreesTBL.MODID, "sapTree"), sap);
    }

    public static final SimpleVoxmap sapVoxmap = new SimpleVoxmap(3, 3, 3, new byte[] {
            0, 0, 0,
            0, 1, 0,
            0, 0, 0,

            0, 1, 0,
            1, 0, 1,
            0, 1, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0,
    }).setCenter(new BlockPos(1, 1, 1));



    private final ICellKit sap = new ICellKit() {

        private final ICell normalCell = new CellNormal(1);

        private final ICell branchCell = new CellNormal(5);

        private final BasicSolver sapSolver = new BasicSolver(new short[]{0x0511});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return normalCell;
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? branchCell : CellNull.NULLCELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return sapVoxmap;
        }

        @Override
        public ICellSolver getCellSolver() {
            return sapSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 1;
        }

    };

}
