package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.entity.mobs.HelperBotEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public class EntityInit {

    public static final EntityType<HelperBotEntity> HELPER_BOT_ENTITY = Registry.register(Registries.ENTITY_TYPE, id("helper_bot_entity"), EntityType.Builder.<HelperBotEntity>create(HelperBotEntity::new, SpawnGroup.AMBIENT).dimensions(2, 4).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, id("helper_bot_entity"))));

    public static void initialize() {
    }

}
