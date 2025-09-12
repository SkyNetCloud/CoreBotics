package ca.skynetcloud.core_botics.common.entity.mobs;

import ca.skynetcloud.core_botics.common.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.HappyGhastEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

public class HelperBotEntity extends HappyGhastEntity implements GeoEntity {

    public HelperBotEntity(World world) {
        super(EntityInit.HELPER_BOT_ENTITY_ENTITY_TYPE, world);
    }

    public HelperBotEntity(EntityType<HelperBotEntity> helperBotEntityEntityType, World world) {
        super(helperBotEntityEntityType, world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }
}
