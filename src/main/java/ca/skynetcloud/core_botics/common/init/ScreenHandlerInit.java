package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.screen.BiorayCollectorScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class ScreenHandlerInit {

    public static final ScreenHandlerType<BiorayCollectorScreenHandler> ENTROPY_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MODID, "bio_screen"), new ScreenHandlerType<>(BiorayCollectorScreenHandler::new, FeatureSet.empty()));

}

