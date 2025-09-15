package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.entity.mobs.HelperBotEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class AttributeInit {

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(EntityInit.HELPER_BOT_ENTITY, HelperBotEntity.createHelperRobotAttributes());
    }
}
