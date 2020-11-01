package maxhyper.dynamictreesplants.blocks;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import maxhyper.dynamictreesplants.ModContent;
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
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import shadows.plants2.init.ModRegistry;

import java.util.Random;

public class BlockRootyCrystal extends BlockRooty {

	static String name = "rootycrystaldirt";

	public BlockRootyCrystal(boolean isTileEntity) {
		this(name + (isTileEntity ? "species" : ""), isTileEntity);
	}

	public BlockRootyCrystal(String name, boolean isTileEntity) {
		super(name, Material.GROUND, isTileEntity);
		setSoundType(SoundType.GLASS);
	}

	///////////////////////////////////////////
	// BLOCKSTATES
	///////////////////////////////////////////

	public static final Material[] materialOrder = {Material.GLASS, Material.ROCK, Material.GROUND};
	@Override
	public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
		return MimicProperty.getGenericMimic(access, pos, materialOrder, DirtHelper.getSoilFlags(ModContent.CRYSTALLIKE), ModRegistry.GROUNDCOVER.getDefaultState());
	}

	///////////////////////////////////////////
	// INTERACTION
	///////////////////////////////////////////

	public IBlockState getDecayBlockState(IBlockAccess access, BlockPos pos) {
		return getMimic(access, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.DIRT);
	}

	///////////////////////////////////////////
	// RENDERING
	///////////////////////////////////////////

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.SOLID;
	}
	
}
