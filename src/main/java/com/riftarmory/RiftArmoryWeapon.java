package com.riftarmory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public interface RiftArmoryWeapon {
    void onHit(ServerPlayerEntity player, LivingEntity target, ItemStack stack, float amount);
}
