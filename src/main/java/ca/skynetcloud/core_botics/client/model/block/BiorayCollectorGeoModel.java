package ca.skynetcloud.core_botics.client.model.block;

import ca.skynetcloud.core_botics.common.entity.block.BiorayCollectorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;


public class BiorayCollectorGeoModel extends GeoModel<BiorayCollectorEntity> {

    private final Identifier model = Identifier.of(MODID, "bioray_collector");
    private final Identifier animations = Identifier.of(MODID, "bioray_collector");
    private final Identifier texture = Identifier.of(MODID, "textures/block/bioray_collector.png");


    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState geoRenderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(BiorayCollectorEntity entropyCollectorEntity) {
        return this.animations;
    }
}
