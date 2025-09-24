package ca.skynetcloud.core_botics.client.renderer.block;

import ca.skynetcloud.core_botics.client.model.block.BiorayInfusionPedestalGeoModel;
import ca.skynetcloud.core_botics.common.entity.block.machine.PedestalBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import static net.minecraft.item.ItemDisplayContext.GROUND;

public class InfusionPedestalEntityRenderer extends GeoBlockRenderer<PedestalBlockEntity> {

    public InfusionPedestalEntityRenderer(BlockEntityRendererFactory.Context ignoredContext) {
        super(new BiorayInfusionPedestalGeoModel());

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public void render(PedestalBlockEntity pedestal, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay, Vec3d cameraPosition) {

        super.render(pedestal, partialTick, poseStack, bufferSource, packedLight, packedOverlay, cameraPosition);

        BlockEntity be = pedestal.getWorld().getBlockEntity(pedestal.getPos());
        if (!(be instanceof PedestalBlockEntity freshPedestal)) return;


        if (pedestal.getStack().isEmpty()) return;

        poseStack.push();
        poseStack.translate(0.5f, 0.29f, 0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(pedestal.getRenderingRotation()));

        MinecraftClient.getInstance().getItemRenderer().renderItem(pedestal.getStack(), GROUND, packedLight, packedOverlay, poseStack, bufferSource, pedestal.getWorld(), 0);

        poseStack.pop();
    }


}

