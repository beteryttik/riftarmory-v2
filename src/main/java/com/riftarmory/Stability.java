package com.riftarmory;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class Stability {
    public static final int MAX = 100;
    public static final int COST = 40;

    private Stability() {
    }

    public static int get(ItemStack stack) {
        Integer v = stack.get(ModComponents.STABILITY);
        return v == null ? MAX : v;
    }

    public static void set(ItemStack stack, int value) {
        stack.set(ModComponents.STABILITY, Math.max(0, Math.min(MAX, value)));
    }

    public static boolean tryUse(ServerPlayerEntity player, ItemStack stack) {
        int v = get(stack);
        if (v < COST) {
            player.sendMessage(Text.translatable("message.riftarmory.no_stability"), true);
            return false;
        }
        set(stack, v - COST);
        return true;
    }
}
