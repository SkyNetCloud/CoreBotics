package ca.skynetcloud.core_botics.client;

import ca.skynetcloud.core_botics.client.init.KeyBindingInit;
import ca.skynetcloud.core_botics.client.init.RendererInit;
import ca.skynetcloud.core_botics.client.init.ScreenHandlerInit;
import ca.skynetcloud.core_botics.common.entity.mobs.QuadToBikeEntity;
import ca.skynetcloud.core_botics.common.network.payloads.RideModeTogglePayload;
import ca.skynetcloud.core_botics.common.network.payloads.VehicleInputPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


public class CoreBoticsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RendererInit.initialize();
        ScreenHandlerInit.initialize();
        KeyBindingInit.initialize();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeyBindingInit.MODE_SWITCH.wasPressed()) {
                if (client.player != null && client.player.getVehicle() instanceof QuadToBikeEntity mount) {
                    RideModeTogglePayload payload = new RideModeTogglePayload(mount.getId());
                    ClientPlayNetworking.send(payload);
                }
            }

            if (client.player != null && client.player.getVehicle() instanceof QuadToBikeEntity mount) {
                boolean ascendPressed = KeyBindingInit.ascendKey.isPressed();
                boolean descendPressed = KeyBindingInit.descendKey.isPressed();

                VehicleInputPayload payload = new VehicleInputPayload(mount.getId(), ascendPressed, descendPressed);
                ClientPlayNetworking.send(payload);
            }
        });
    }



}
