package maxhyper.dynamictreestheaether.growth;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellKit;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.cells.*;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.util.ResourceLocation;

public class CustomCellKits extends CellKits {

    public static void preInit() {
        new CustomCellKits();
    }

    public CustomCellKits (){
        super();
        TreeRegistry.registerCellKit(new ResourceLocation(maxhyper.dynamictreestheaether.DynamicTreesTheAether.MODID, "sugi"), sugi);
    }

    private final ICellKit sugi = new ICellKit() {

        private final ICell sugiBranch = new CellConiferBranch();

        private final ICell coniferLeafCells[] = {
                CellNull.NULLCELL,
                new CellConiferLeaf(1),
                new CellConiferLeaf(2),
                new CellConiferLeaf(3),
                new CellConiferLeaf(4)
        };

        private final BasicSolver coniferSolver = new BasicSolver(new short[]{0x0514, 0x0413, 0x0312, 0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return coniferLeafCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if(radius <= 3) {
                return sugiBranch;
            } else {
                return CellNull.NULLCELL;
            }
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return LeafClusters.conifer;
        }

        @Override
        public ICellSolver getCellSolver() {
            return coniferSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

}
