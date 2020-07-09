package maxhyper.dynamictreesttf.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;

/*
  @Author the-realest-stu
 */

/** Place cobwebs in the trees leaves */
public class FeatureGenWeb implements IPostGenFeature {
	
	protected float verSpread = 60;
	protected float rayDistance = 4;
	protected float extraCount;
	protected Species species;
	protected Block webBlock = Blocks.WEB;
	
	public FeatureGenWeb(Species species, float countMultiplier) {
		extraCount = countMultiplier;
		this.species = species;
	}
	
	public FeatureGenWeb setVerSpread(float verSpread) {
		this.verSpread = verSpread;
		return this;
	}
	
	public FeatureGenWeb setRayDistance(float rayDistance) {
		this.rayDistance = rayDistance;
		return this;
	}
	
	@Override
	public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
		if (!endPoints.isEmpty()) {
			int qty = (int) (endPoints.size() * ((world.rand.nextFloat() * 0.5f) + extraCount));
			
			for (int i = 0; i < qty; i++) {
				BlockPos endPoint = endPoints.get(world.rand.nextInt(endPoints.size()));
				addWeb(world, species, rootPos, endPoint, safeBounds);
			}
			return true;
		}
		return false;
	}
	
	protected void addWeb(World world, Species species, BlockPos treePos, BlockPos branchPos, SafeChunkBounds safeBounds) {
		RayTraceResult result = CoordUtils.branchRayTrace(world, species, treePos, branchPos, 90, verSpread, rayDistance, safeBounds);
		
		if (result != null) {
			BlockPos webPos = result.getBlockPos().offset(result.sideHit);
			if (webPos != BlockPos.ORIGIN && world.isAirBlock(webPos)) {
				world.setBlockState(webPos, webBlock.getDefaultState());
			}
		}
	}
	
}
