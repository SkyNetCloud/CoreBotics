package ca.skynetcloud.core_botics.client.model.block;

import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;


public class BiorayInfusionMatrixGeoModel extends GeoModel<BiorayInfusionMatrixEntity> {

    private final Identifier model = Identifier.of(MODID, "block/bioray_infusion_matrix");
    private final Identifier animations = Identifier.of(MODID, "block/bioray_infusion_matrix");
    private final Identifier texture = Identifier.of(MODID, "textures/block/bioray_infusion_matrix.png");


    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState geoRenderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(BiorayInfusionMatrixEntity biorayInfusionMatrixEntity) {
        return this.animations;
    }
}
