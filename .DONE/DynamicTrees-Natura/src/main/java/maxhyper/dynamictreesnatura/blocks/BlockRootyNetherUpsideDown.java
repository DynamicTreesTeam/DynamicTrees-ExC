package maxhyper.dynamictreesnatura.blocks;

import com.ferreusveritas.dynamictrees.api.IAgeable;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import com.ferreusveritas.dynamictrees.blocks.MimicProperty;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class BlockRootyNetherUpsideDown extends BlockRootyUpsideDown {

	static String name = "rootynetherdirtupsidedown";

	public BlockRootyNetherUpsideDown(boolean isTileEntity) {
		this(name + (isTileEntity ? "species" : ""), isTileEntity);
	}

	public BlockRootyNetherUpsideDown(String name, boolean isTileEntity) {
		super(name, Material.ROCK, isTileEntity);
		setSoundType(SoundType.STONE);
	}

	///////////////////////////////////////////
	// BLOCKSTATES
	///////////////////////////////////////////

	public static final Material[] materialOrder = {Material.ROCK, Material.SAND, Material.GROUND, Material.GRASS};
	@Override
	public IBlockState getMimic(IBlockAccess access, BlockPos pos) {
		return MimicProperty.getGenericMimic(access, pos, materialOrder, DirtHelper.getSoilFlags(DirtHelper.NETHERLIKE,DirtHelper.DIRTLIKE), Blocks.NETHERRACK.getDefaultState());
	}

	///////////////////////////////////////////
	// INTERACTION
	///////////////////////////////////////////

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.NETHERRACK);
	}
}
