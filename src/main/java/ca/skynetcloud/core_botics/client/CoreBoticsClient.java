package ca.skynetcloud.core_botics.client;

import ca.skynetcloud.core_botics.client.renderer.block.BiorayCollectorBlockEntityRenderer;
import ca.skynetcloud.core_botics.client.renderer.block.BiorayInfsuinMatrixBlockEntityRenderer;
import ca.skynetcloud.core_botics.client.renderer.block.InfusionPedestalEntityRenderer;
import ca.skynetcloud.core_botics.client.renderer.entity.HelperRobotRenderer;
import ca.skynetcloud.core_botics.client.screen.machine.BiorayCollectorInfoScreen;
import ca.skynetcloud.core_botics.client.screen.machine.BiorayInfusionMatrixScreen;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.EntityInit;
import ca.skynetcloud.core_botics.common.init.ScreenHandlerInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CoreBoticsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(BlockEntityInit.BIORAY_COLLECTOR_ENTITY,  BiorayCollectorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.INFUSION_MATRIX_ENTITY,  BiorayInfsuinMatrixBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.INFUSION_PEDESTAL_ENTITY,  InfusionPedestalEntityRenderer::new);
        EntityRendererRegistry.register(EntityInit.HELPER_BOT_ENTITY, HelperRobotRenderer::new);
        HandledScreens.register(ScreenHandlerInit.BIORAY_COLLECTOR_SCREEN_HANDLER, BiorayCollectorInfoScreen::new);
        HandledScreens.register(ScreenHandlerInit.BIORAY_INFUSION_MATRIX_SCREEN_HANDLER, BiorayInfusionMatrixScreen::new);

    }



}
