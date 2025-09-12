package ca.skynetcloud.core_botics.client;

import ca.skynetcloud.core_botics.client.renderer.block.BiorayCollectorBlockEntityRenderer;
import ca.skynetcloud.core_botics.client.renderer.block.DeactivatedRobotRenderer;
import ca.skynetcloud.core_botics.client.renderer.entity.HelperRobotRenderer;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.EntityInit;
import ca.skynetcloud.core_botics.common.init.ScreenHandlerInit;
import ca.skynetcloud.core_botics.common.screen.BiorayCollectorInfoScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CoreBoticsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(BlockEntityInit.ENTROPY_COLLECTOR_ENTITY,  BiorayCollectorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.DEACTIVATED_ROBOT_ENTITY,  DeactivatedRobotRenderer::new);
        EntityRendererRegistry.register(EntityInit.HELPER_BOT_ENTITY_ENTITY_TYPE, HelperRobotRenderer::new);
        HandledScreens.register(ScreenHandlerInit.ENTROPY_SCREEN_HANDLER, BiorayCollectorInfoScreen::new);

    }



}
