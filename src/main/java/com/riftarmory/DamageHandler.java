package com.riftarmory;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class DamageHandler {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((target, source, amount) -> {
            LivingEntity attacker = source.getAttacker();
            if (attacker instanceof ServerPlayerEntity player) {
                ItemStack stack = player.getMainHandStack();
                if (stack.getItem() instanceof RiftArmoryWeapon weapon) {
                    weapon.onHit(player, target, stack, amount);
                }
            }
            return true;
        });
    }
}
