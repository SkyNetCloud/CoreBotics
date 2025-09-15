package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.client.screen.handler.BiorayCollectorScreenHandler;
import ca.skynetcloud.core_botics.client.screen.handler.BiorayInfusionMatrixScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class ScreenHandlerInit {

    public static final ScreenHandlerType<BiorayCollectorScreenHandler> BIORAY_COLLECTOR_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MODID, "bio_screen"), new ScreenHandlerType<>(BiorayCollectorScreenHandler::new, FeatureSet.empty()));
    public static final ScreenHandlerType<BiorayInfusionMatrixScreenHandler> BIORAY_INFUSION_MATRIX_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MODID, "bioray_matrix_screen"), new ExtendedScreenHandlerType<>(BiorayInfusionMatrixScreenHandler::new, BlockPos.PACKET_CODEC));



}

