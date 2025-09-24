package ca.skynetcloud.core_botics.common.network.payloads;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public record VehicleInputPayload(int entityId, boolean ascend, boolean descend) implements CustomPayload {

    public static final Identifier TOGGLE_MODE_PAYLOAD_ID = id("vehicle_input");
    public static final CustomPayload.Id<VehicleInputPayload> ID = new CustomPayload.Id<>(TOGGLE_MODE_PAYLOAD_ID);
    public static final PacketCodec<PacketByteBuf, VehicleInputPayload> CODEC = PacketCodec.of(
            (payload, buf) -> {
                buf.writeInt(payload.entityId);
                buf.writeBoolean(payload.ascend);
                buf.writeBoolean(payload.descend);
            },
            buf -> new VehicleInputPayload(buf.readInt(), buf.readBoolean(), buf.readBoolean())
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
