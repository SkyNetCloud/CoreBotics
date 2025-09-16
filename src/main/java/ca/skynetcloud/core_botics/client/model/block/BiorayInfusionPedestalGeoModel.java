package ca.skynetcloud.core_botics.client.model.block;


import ca.skynetcloud.core_botics.common.entity.block.machine.PedestalBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;


public class BiorayInfusionPedestalGeoModel extends GeoModel<PedestalBlockEntity> {

    private final Identifier model = Identifier.of(MODID, "block/infusion_pedestal");
    private final Identifier animations = Identifier.of(MODID, "block/infusion_pedestal");
    private final Identifier texture = Identifier.of(MODID, "textures/block/infusion_pedestal.png");


    @Override
    public Identifier getModelResource(GeoRenderState geoRenderState) {
        return this.model;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState geoRenderState) {
        return this.texture;
    }

    @Override
    public Identifier getAnimationResource(PedestalBlockEntity biorayInfusionMatrixEntity) {
        return this.animations;
    }
}
