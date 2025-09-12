package ca.skynetcloud.core_botics.client.model.entity;

import ca.skynetcloud.core_botics.common.entity.mobs.HelperBotEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class HelperBotModel extends GeoModel<HelperBotEntity> {

    private final Identifier model = Identifier.of(MODID, "entity/helper_robot");
    private final Identifier animations = Identifier.of(MODID, "entity/helper_robot");
    private final Identifier texture = Identifier.of(MODID, "textures/entity/helper_robot.png");



    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return texture;
    }

    @Override
    public Identifier getAnimationResource(HelperBotEntity animatable) {
        return animations;
    }
}
