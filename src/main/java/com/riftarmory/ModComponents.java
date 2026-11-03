package com.riftarmory;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<Integer> STABILITY = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(RiftArmory.MOD_ID, "stability"),
            DataComponentType.<Integer>builder()
                    .codec(Codec.INT)
                    .packetCodec(PacketCodecs.VAR_INT)
                    .build());

    public static final ComponentType<Integer> GHOST_PHASE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(RiftArmory.MOD_ID, "ghost_phase"),
            DataComponentType.<Integer>builder()
                    .codec(Codec.INT)
                    .packetCodec(PacketCodecs.VAR_INT)
                    .build());

    public static void init() {
    }
}
