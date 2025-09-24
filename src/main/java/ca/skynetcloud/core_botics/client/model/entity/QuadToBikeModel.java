package ca.skynetcloud.core_botics.client.model.entity;

import ca.skynetcloud.core_botics.common.entity.mobs.QuadToBikeEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class QuadToBikeModel extends GeoModel<QuadToBikeEntity>{

    private final Identifier model = Identifier.of(MODID, "entity/quad_bike");
    private final Identifier animations = Identifier.of(MODID, "entity/quad_bike");
    private final Identifier texture = Identifier.of(MODID, "textures/entity/quad_bike.png");


    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return texture;
    }

    @Override
    public Identifier getAnimationResource(QuadToBikeEntity animatable) {
        return animations;
    }

}
