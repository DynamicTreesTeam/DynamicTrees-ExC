package maxhyper.dynamictreesforestry.event;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import maxhyper.dynamictreesforestry.ModContent;
import maxhyper.dynamictreesforestry.models.BakedModelBlockFruitLeaves;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class EventListenerForestry {

    private static class LeavesFruitBundle {
        public ILeavesProperties leaves;
        public ILeavesProperties[] fruityLeaves;
        public LeavesFruitBundle(ILeavesProperties leaves, ILeavesProperties[] fruityLeaves){
            this.leaves = leaves;
            this. fruityLeaves = fruityLeaves;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {

        LeavesFruitBundle[] fruitLeaves = {
                new LeavesFruitBundle(ModContent.oakLeavesProperties, ModContent.fruitAppleLeavesProperties),
                new LeavesFruitBundle(ModContent.walnutLeavesProperties, ModContent.fruitWalnutLeavesProperties),
                new LeavesFruitBundle(ModContent.chestnutLeavesProperties, ModContent.fruitChestnutLeavesProperties),
                new LeavesFruitBundle(ModContent.cherryLeavesProperties, ModContent.fruitCherryLeavesProperties),
                new LeavesFruitBundle(ModContent.lemonLeavesProperties, ModContent.fruitLemonLeavesProperties),
                new LeavesFruitBundle(ModContent.plumLeavesProperties, ModContent.fruitPlumLeavesProperties)
        };

        for(LeavesFruitBundle leaves: fruitLeaves) {
            ResourceLocation registryName = Objects.requireNonNull(leaves.fruityLeaves[0].getDynamicLeavesState().getBlock().getRegistryName());
            IBakedModel leavesObject = event.getModelRegistry().getObject(new ModelResourceLocation(registryName, "normal"));
            if (leavesObject != null) {
                BakedModelBlockFruitLeaves leavesModel = new BakedModelBlockFruitLeaves(leavesObject, leaves.leaves.getDynamicLeavesState());
                event.getModelRegistry().putObject(new ModelResourceLocation(registryName, "normal"), leavesModel);
            }
        }
    }

}