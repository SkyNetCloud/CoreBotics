package ca.skynetcloud.core_botics.client.renderer.entity;

import ca.skynetcloud.core_botics.client.model.entity.QuadToBikeModel;
import ca.skynetcloud.core_botics.common.entity.mobs.QuadToBikeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;


public class QuadToBikeRenderer <R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<QuadToBikeEntity, R> {
    public QuadToBikeRenderer(EntityRendererFactory.Context context) {
        super(context, new QuadToBikeModel());
    }
}
