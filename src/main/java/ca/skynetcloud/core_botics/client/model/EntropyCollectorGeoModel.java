package ca.skynetcloud.core_botics.client.model;

import ca.skynetcloud.core_botics.common.entity.block.EntropyCollectorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;


public class EntropyCollectorGeoModel extends GeoModel<EntropyCollectorEntity> {

    private final Identifier model = Identifier.of(MODID, "entropy_collector");
    private final Identifier animations = Identifier.of(MODID, "entropy_collector");
    private final Identifier texture = Identifier.of(MODID, "textures/block/entropy_collector.png");


    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState geoRenderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(EntropyCollectorEntity entropyCollectorEntity) {
        return this.animations;
    }
}
