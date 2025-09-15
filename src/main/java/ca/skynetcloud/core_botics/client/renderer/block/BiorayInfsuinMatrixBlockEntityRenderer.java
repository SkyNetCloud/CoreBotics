package ca.skynetcloud.core_botics.client.renderer.block;

import ca.skynetcloud.core_botics.client.model.block.BiorayCollectorGeoModel;
import ca.skynetcloud.core_botics.client.model.block.BiorayInfusionMatrixGeoModel;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BiorayInfsuinMatrixBlockEntityRenderer extends GeoBlockRenderer<BiorayInfusionMatrixEntity> {

    public BiorayInfsuinMatrixBlockEntityRenderer(BlockEntityRendererFactory.Context ignoredContext) {
        super(new BiorayInfusionMatrixGeoModel());

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

}
