package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.screen.EntropyScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class ScreenHandlerInit {

    public static final ScreenHandlerType<EntropyScreenHandler> ENTROPY_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MODID, "entropy_screen"), new ScreenHandlerType<>(EntropyScreenHandler::new, FeatureSet.empty()));

}

