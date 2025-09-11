package ca.skynetcloud.core_botics.client.renderer;

import ca.skynetcloud.core_botics.client.model.EntropyCollectorGeoModel;
import ca.skynetcloud.core_botics.common.entity.block.EntropyCollectorEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class EntropyCollectorBlockEntityRenderer extends GeoBlockRenderer<EntropyCollectorEntity> {

    public EntropyCollectorBlockEntityRenderer(BlockEntityRendererFactory.Context ignoredContext) {
        super(new EntropyCollectorGeoModel());
    }
}
