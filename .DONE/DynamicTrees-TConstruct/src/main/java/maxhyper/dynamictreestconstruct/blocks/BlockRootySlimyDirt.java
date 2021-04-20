package maxhyper.dynamictreestconstruct.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Random;

public class BlockRootySlimyDirt extends BlockRooty {

	static String name = "rootyslimydirt";

	public BlockRootySlimyDirt(boolean isTileEntity) {
		this(name + (isTileEntity ? "species" : ""), isTileEntity);
	}

	public BlockRootySlimyDirt(String name, boolean isTileEntity) {
		super(name, Material.GROUND, isTileEntity);
		setSoundType(SoundType.SLIME);
	}

	///////////////////////////////////////////
	// BLOCKSTATES
	///////////////////////////////////////////

	public static final Material[] materialOrder = {Material.GRASS, Material.GROUND};
	@Override
	public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
		return MimicProperty.getGenericMimic(access, pos, materialOrder, DirtHelper.getSoilFlags(DirtHelper.SLIMELIKE), TinkerWorld.slimeDirt.getDefaultState());
	}

	///////////////////////////////////////////
	// INTERACTION
	///////////////////////////////////////////

	public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
		return getMimic(access, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(TinkerWorld.slimeDirt);
	}

	///////////////////////////////////////////
	// RENDERING
	///////////////////////////////////////////

	@Override
	public boolean isOpaqueCube(IBlockState s) {
		return false;
	}
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.CUTOUT_MIPPED;
	}
	
}
