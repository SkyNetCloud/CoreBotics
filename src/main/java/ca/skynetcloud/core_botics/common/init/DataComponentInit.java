package ca.skynetcloud.core_botics.common.init;

import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public class DataComponentInit {


    public static void initialize() {}

    public static final ComponentType<Integer> BIORAY_CHARGE  = register("bioray_charge", builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));


    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id(id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }

}
