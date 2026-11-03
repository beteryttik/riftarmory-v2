package com.riftarmory;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class StabilityTicker {
    private static int counter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            counter++;
            if (counter % 10 != 0) {
                return;
            }
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack stack = player.getInventory().getStack(i);
                    if (!stack.isEmpty() && stack.contains(ModComponents.STABILITY)) {
                        int v = Stability.get(stack);
                        if (v < Stability.MAX) {
                            Stability.set(stack, v + 1);
                        }
                    }
                }
                // Поддержание фазы неосязаемости, пока в руке Булава Призрака
                ItemStack hand = player.getMainHandStack();
                if (hand.getItem() instanceof GhostMaceItem) {
                    Integer phase = hand.get(ModComponents.GHOST_PHASE);
                    if (phase != null && phase > 0) {
                        hand.set(ModComponents.GHOST_PHASE, phase - 1);
                        player.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.INVISIBILITY, 60, 0, false, false, true));
                    }
                }
            }
        });
    }
}
