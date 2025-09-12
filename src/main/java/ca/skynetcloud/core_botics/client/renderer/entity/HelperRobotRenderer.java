package ca.skynetcloud.core_botics.client.renderer.entity;

import ca.skynetcloud.core_botics.client.model.entity.HelperBotModel;
import ca.skynetcloud.core_botics.common.entity.mobs.HelperBotEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class HelperRobotRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<HelperBotEntity, R> {
    public HelperRobotRenderer(EntityRendererFactory.Context context) {
        super(context, new HelperBotModel());
    }
}
