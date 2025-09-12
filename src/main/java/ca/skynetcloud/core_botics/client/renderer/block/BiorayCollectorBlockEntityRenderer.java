package ca.skynetcloud.core_botics.client.renderer.block;

import ca.skynetcloud.core_botics.client.model.block.BiorayCollectorGeoModel;
import ca.skynetcloud.core_botics.common.entity.block.BiorayCollectorEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BiorayCollectorBlockEntityRenderer extends GeoBlockRenderer<BiorayCollectorEntity> {

    public BiorayCollectorBlockEntityRenderer(BlockEntityRendererFactory.Context ignoredContext) {
        super(new BiorayCollectorGeoModel());
    }
}
