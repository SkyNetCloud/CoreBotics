package ca.skynetcloud.core_botics.client.init;

import ca.skynetcloud.core_botics.client.renderer.block.BiorayCollectorBlockEntityRenderer;
import ca.skynetcloud.core_botics.client.renderer.block.BiorayInfsuinMatrixBlockEntityRenderer;
import ca.skynetcloud.core_botics.client.renderer.block.InfusionPedestalEntityRenderer;
import ca.skynetcloud.core_botics.client.renderer.entity.HelperRobotRenderer;
import ca.skynetcloud.core_botics.client.renderer.entity.QuadToBikeRenderer;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.EntityInit;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class RendererInit {

    public static void initialize() {
        BlockEntityRendererFactories.register(BlockEntityInit.BIORAY_COLLECTOR_ENTITY, BiorayCollectorBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.INFUSION_MATRIX_ENTITY, BiorayInfsuinMatrixBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntityInit.INFUSION_PEDESTAL_ENTITY, InfusionPedestalEntityRenderer::new);
        EntityRendererRegistry.register(EntityInit.HELPER_BOT_ENTITY, HelperRobotRenderer::new);
        EntityRendererRegistry.register(EntityInit.BIKE_ENTITY, QuadToBikeRenderer::new);

    }
}
