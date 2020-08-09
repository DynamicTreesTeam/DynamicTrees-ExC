package maxhyper.dynamictreestconstruct.worldgen;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import maxhyper.dynamictreestconstruct.DynamicTreesTConstruct;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Random;

public class DynamicSlimeTreeGenerator implements IWorldGenerator {

    static final Species blue = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimeblue"));
    static final Species purple = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimepurple"));
    static final Species magma = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesTConstruct.MODID, "slimemagma"));
    public final Species slimeTree;
    public final slimeType typeOfSlime;

    static int maxRadiusToCheck = 6;
    static int tooClose = 3;
    static int maxHeightToCheck = 10;
    static int maxAttempts = 3;
    static int currentAttempt;
    static int radiusForNextAttempt = 4;

    public enum slimeType {
        BLUE,
        PURPLE,
        MAGMA
    }

    DynamicSlimeTreeGenerator(slimeType type, int attemptNumber){
        currentAttempt = attemptNumber;
        typeOfSlime = type;
        switch (type){
            case PURPLE:
                slimeTree = purple;
                break;
            case MAGMA:
                slimeTree = magma;
                break;
            default:
                slimeTree = blue;
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) { };

    public void generateTree(Random random, World world, BlockPos pos) {
        int height = random.nextInt(4) + 5;
        int maxRad = 2+random.nextInt(7);
        pos = findGround(world, pos);
        if(pos.getY() < 0) {
            return;
        }

        for (int y=0;y<=maxHeightToCheck;y++){
            for (int x=-maxRadiusToCheck;x<=maxRadiusToCheck;x++){
                for (int z=-maxRadiusToCheck;z<=maxRadiusToCheck;z++){
                    BlockPos checkPos = pos.add(x,y,z);
                    if (world.getBlockState(checkPos) instanceof BlockBranch){
                        maxRad = Math.min(maxRad, Math.min(Math.abs(x),Math.abs(z)));
                        if (maxRad <= tooClose) {
                            AttemptAgain(random, world, pos);
                            return;
                        }
                    }
                }
            }
        }

        int yPos = pos.getY();

        if(yPos >= 1 && yPos + height + 1 <= 256) {
            IBlockState state = world.getBlockState(pos.down());
            boolean isSoil = slimeTree.isAcceptableSoil(world, pos.down(), state);
            if(isSoil) {
                slimeTree.generate(world, pos.down(), world.getBiome(pos), random,  maxRad, SafeChunkBounds.ANY);
            } else {
                AttemptAgain(random, world, pos);
            }
        } else {
            AttemptAgain(random, world, pos);
        }
    }

    public void AttemptAgain(Random random, World world, BlockPos pos) {
        if (currentAttempt > maxAttempts){
            return;
        }
        BlockPos newPos = pos.add(radiusForNextAttempt+random.nextInt(2*radiusForNextAttempt), 0, radiusForNextAttempt+random.nextInt(2*radiusForNextAttempt));
        DynamicSlimeTreeGenerator newTree = new DynamicSlimeTreeGenerator(typeOfSlime, currentAttempt + 1);
        newTree.generateTree(random,world,newPos);

    }

    BlockPos findGround(World world, BlockPos pos) {
        do {
            IBlockState state = world.getBlockState(pos);
            Block heightID = state.getBlock();
            IBlockState up = world.getBlockState(pos.up());
            if((heightID == TinkerWorld.slimeDirt || heightID == TinkerWorld.slimeGrass) && !up.getBlock().isOpaqueCube(up)) {
                return pos.up();
            }
            pos = pos.down();
        } while(pos.getY() > 0);

        return pos;
    }

}
