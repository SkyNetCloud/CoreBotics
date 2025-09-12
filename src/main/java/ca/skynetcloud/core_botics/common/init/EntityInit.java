package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.entity.mobs.HelperBotEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public class EntityInit {

    public static final EntityType<HelperBotEntity> HELPER_BOT_ENTITY_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            id("helper_box_entity"),
            EntityType.Builder.<HelperBotEntity>create(HelperBotEntity::new, SpawnGroup.MISC)
                    .dimensions(2.5f, 3.0f)
                    .maxTrackingRange(10)
                    .alwaysUpdateVelocity(true)
                    .build(null)
    );}
