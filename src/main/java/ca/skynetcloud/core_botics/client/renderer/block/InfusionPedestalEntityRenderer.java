package ca.skynetcloud.core_botics.client.renderer.block;

import ca.skynetcloud.core_botics.CoreBoticsMain;
import ca.skynetcloud.core_botics.client.model.block.BiorayCollectorGeoModel;
import ca.skynetcloud.core_botics.client.model.block.BiorayInfusionMatrixGeoModel;
import ca.skynetcloud.core_botics.client.model.block.BiorayInfusionPedestalGeoModel;
import ca.skynetcloud.core_botics.common.entity.PedestalBlockEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.minecraft.item.ItemDisplayContext.GROUND;

public class InfusionPedestalEntityRenderer extends GeoBlockRenderer<PedestalBlockEntity> {

    public InfusionPedestalEntityRenderer(BlockEntityRendererFactory.Context ignoredContext) {
        super(new BiorayInfusionPedestalGeoModel());
    }

    @Override
    public void render(PedestalBlockEntity pedestal, float partialTick, MatrixStack poseStack,
                       VertexConsumerProvider bufferSource, int packedLight, int packedOverlay, Vec3d cameraPosition) {

        super.render(pedestal, partialTick, poseStack, bufferSource, packedLight, packedOverlay, cameraPosition);

        BlockEntity be = pedestal.getWorld().getBlockEntity(pedestal.getPos());
        if (!(be instanceof PedestalBlockEntity freshPedestal)) return;

        SimpleInventory inv = freshPedestal.getInventory();
        ItemStack stack = ItemStack.EMPTY;
        for (ItemStack s : inv.getHeldStacks()) {
            if (!s.isEmpty()) {
                stack = s;
                break;
            }
        }

        if (stack.isEmpty()) return;

        poseStack.push();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        float scale = stack.getItem() instanceof BlockItem ? 0.95F : 0.75F;
        poseStack.scale(scale, scale, scale);
        double tick = System.currentTimeMillis() / 800.0D;
        poseStack.translate(0.0D, Math.sin(tick % (2 * Math.PI)) * 0.065D, 0.0D);

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, GROUND,
                packedLight, packedOverlay, poseStack, bufferSource, pedestal.getWorld(), 0);

        poseStack.pop();
    }


}

