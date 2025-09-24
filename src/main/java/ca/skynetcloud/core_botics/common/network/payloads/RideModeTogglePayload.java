package ca.skynetcloud.core_botics.common.network.payloads;


import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public record  RideModeTogglePayload(int entityId) implements CustomPayload {

    public static final Identifier TOGGLE_MODE_PAYLOAD_ID = id("ride_mode_toggle");
    public static final CustomPayload.Id<RideModeTogglePayload> ID = new CustomPayload.Id<>(TOGGLE_MODE_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, RideModeTogglePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, RideModeTogglePayload::entityId, RideModeTogglePayload::new);


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
