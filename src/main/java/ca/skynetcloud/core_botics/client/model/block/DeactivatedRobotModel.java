package ca.skynetcloud.core_botics.client.model.block;

import ca.skynetcloud.core_botics.common.entity.block.DectivatedRobotEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class DeactivatedRobotModel extends GeoModel<DectivatedRobotEntity> {

    private final Identifier model = Identifier.of(MODID, "block/deactivated_robot");
    private final Identifier animations = Identifier.of(MODID, "deactivated_robot");
    private final Identifier texture = Identifier.of(MODID, "textures/block/deactivated_robot.png");


    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState geoRenderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(DectivatedRobotEntity helperBotEntity) {
        return this.animations;
    }
}
