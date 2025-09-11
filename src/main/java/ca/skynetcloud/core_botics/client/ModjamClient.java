package ca.skynetcloud.core_botics.client;

import ca.skynetcloud.core_botics.client.renderer.EntropyCollectorBlockEntityRenderer;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.ScreenHandlerInit;
import ca.skynetcloud.core_botics.common.screen.EntropyInfoScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ModjamClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(BlockEntityInit.ENTROPY_COLLECTOR_ENTITY,  EntropyCollectorBlockEntityRenderer::new);
        HandledScreens.register(ScreenHandlerInit.ENTROPY_SCREEN_HANDLER, EntropyInfoScreen::new);

    }



}
