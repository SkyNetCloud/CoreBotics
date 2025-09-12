package ca.skynetcloud.core_botics.client.renderer.block;

import ca.skynetcloud.core_botics.client.model.block.DeactivatedRobotModel;
import ca.skynetcloud.core_botics.common.entity.block.DectivatedRobotEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DeactivatedRobotRenderer extends GeoBlockRenderer<DectivatedRobotEntity> {

    public DeactivatedRobotRenderer(BlockEntityRendererFactory.Context ignoredContext) {
        super(new DeactivatedRobotModel());
    }

    @Override
    protected void rotateBlock(Direction facing, MatrixStack poseStack) {
        switch (facing) {
            case SOUTH -> poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
            case WEST -> poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            case NORTH -> poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(0));
            case EAST -> poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270));
            default -> {}
        }
    }
}
