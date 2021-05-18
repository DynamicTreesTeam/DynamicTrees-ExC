package maxhyper.dtforbiddenarcanus.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.ThickBranchBlock;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.families.NetherFungusFamily;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class MysterywoodFamily extends Family {
    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(MysterywoodFamily::new);

    public MysterywoodFamily(ResourceLocation name) {
        super(name);
    }

    @OnlyIn(Dist.CLIENT)
    private static void branchAnimateTick(BlockState state, World world, BlockPos pos, Random random) {
        int radius = TreeHelper.getRadius(world, pos);
        if (world.rand.nextFloat() <= radius/8f){
            double d0 = ((float)pos.getX() + random.nextFloat());
            double d2 = ((float)pos.getY() + random.nextFloat());
            double d3 = ((float)pos.getZ() + random.nextFloat());
            double d4 = ((double)random.nextFloat() - 0.5D) * 0.3D;
            double d5 = ((double)random.nextFloat() - 0.5D) * 0.3D;
            double d6 = ((double)random.nextFloat() - 0.5D) * 0.3D;
            world.addParticle(ParticleTypes.END_ROD, d0, d2, d3, d4, d5, d6);
        }
    }

    @Override
    protected BranchBlock createBranchBlock() {
        BasicBranchBlock branch = this.isThick() ? new ThickBranchBlock(this.getProperties()){
            @Override public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
                branchAnimateTick(stateIn, worldIn, pos, rand);
            }
        } : new BasicBranchBlock(this.getProperties()){
            @Override public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
                branchAnimateTick(stateIn, worldIn, pos, rand);
            }
        };
        if (this.isFireProof()) branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }
}
