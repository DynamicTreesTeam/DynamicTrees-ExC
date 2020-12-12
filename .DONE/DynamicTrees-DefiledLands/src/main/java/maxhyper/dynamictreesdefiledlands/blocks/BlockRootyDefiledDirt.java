package maxhyper.dynamictreesdefiledlands.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import lykrast.defiledlands.common.init.ModBlocks;
import maxhyper.dynamictreesdefiledlands.ModContent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockRootyDefiledDirt extends BlockRooty {

	static String name = "rootydefileddirt";

	public BlockRootyDefiledDirt(boolean isTileEntity) {
		this(name + (isTileEntity ? "species" : ""), isTileEntity);
	}

	public BlockRootyDefiledDirt(String name, boolean isTileEntity) {
		super(name, Material.GROUND, isTileEntity);
	}

	///////////////////////////////////////////
	// BLOCKSTATES
	///////////////////////////////////////////

	public static final Material[] materialOrder = {Material.GRASS, Material.GROUND};
	@Override
	public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
		return MimicProperty.getGenericMimic(access, pos, materialOrder, DirtHelper.getSoilFlags(ModContent.CORRUPTDIRTLIKE), ModBlocks.dirtDefiled.getDefaultState());
	}

	///////////////////////////////////////////
	// RENDERING
	///////////////////////////////////////////

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.SOLID;
	}
	
}
